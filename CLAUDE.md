# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

Maven wrapper is checked in — use it instead of a system Maven.

```bash
./mvnw spring-boot:run                                   # run app (embedded Tomcat, port 8080)
./mvnw clean package                                     # build WAR (target/*.war) — packaging is `war`, not `jar`
./mvnw test                                              # run all tests
./mvnw test -Dtest=SpringBootInventorySystemApplicationTests#contextLoads   # run a single test
```

On Windows use `mvnw.cmd ...`.

The project is packaged as a WAR (`<packaging>war</packaging>`) and includes [ServletInitializer.java](src/main/java/com/sandipsky/inventory_system/ServletInitializer.java) so it can be deployed to an external Tomcat. `spring-boot-starter-tomcat` is `provided`-scoped — keep it that way for external deployment to work.

## Required External State

- **SQLite** — the datasource is `jdbc:sqlite:inventory_system.db` (file at repo root, auto-created) with `spring.jpa.hibernate.ddl-auto=update`, so Hibernate creates/updates tables on startup. See [application.properties](src/main/resources/application.properties). [database.sql](database.sql) is the reference schema **and the seed data** — the `account_master` rows (named accounts like `"VAT Purchase"`, `"VAT Sales"`, `"Cash"`, `"Tax"`, `"Adjustment"`), `document_numbering` rows, and `operation` rows must be loaded or transactional saves and `@RequiresOperation` checks will fail at runtime.
- **`uploads/`** directory at repo root — used for user profile images, served back at `/uploads/**` via [WebConfig.java](src/main/java/com/sandipsky/inventory_system/common/config/WebConfig.java).
- On first boot, [DataInitializer.java](src/main/java/com/sandipsky/inventory_system/common/config/DataInitializer.java) seeds an `admin` / `Admin@123` user if none exists.

## Package Naming Gotcha

Maven artifactId is `spring-boot-inventory-system` and the main class is `SpringBootInventorySystemApplication`, but **the actual Java package is `com.sandipsky.inventory_system`** (not `spring_boot_inventory_system`). Tests mirror the main package under `src/test/java/com/sandipsky/inventory_system/`.

## Architecture

### Package-by-feature
Code is organized under `com.sandipsky.inventory_system.features`, one package per feature (some grouped under a parent domain), and inside each feature the classes are split into `controllers/`, `services/`, `repositories/`, `entities/`, and `dtos/` subpackages:

```
features/
├── masters/           # simple lookup masters
│   ├── category/  unit/  taxtype/  packing/
├── purchase/
│   ├── vendor/  purchase_entry/  purchase_return/
├── sales/
│   ├── customer/  sales_entry/  sales_return/
├── inventory/
│   ├── stock_adjustment/  stock_edit/  opening_stock/
├── accounting/
│   ├── journal/  account/  payment/  opening_balance/
├── reports/           # read-only; no entities of their own
│   ├── purchase/  sales/  inventory/  accounting/
├── settings/
│   ├── configuration/  document_numbering/
├── product/  user/  role_operations/  auth/
```

Cross-cutting code lives in:
- `common/` — `common/dto` (`ApiResponse`), `common/exception`, `common/util` (`ResponseUtil`, `SpecificationBuilder`, `QueryParamUtil`), `common/config` (`WebConfig`, `DataInitializer`), `common/dropdown` (the cross-feature dropdown endpoint — controller, service, and `DropdownDTO` together)
- `security/` — JWT filter chain, `WebSecurityConfig`, `JwtUtil`, `@RequiresOperation` + its aspect

When adding a new domain, create a new feature package under `features/` with the same layer subfolders. Java package names can't contain hyphens, so multi-word features use underscores (`purchase_entry`, `sales_entry`, `role_operations`, `document_numbering`). Note: repository JPQL uses fully-qualified constructor expressions (e.g. `SELECT new com.sandipsky.inventory_system.common.dropdown.dtos.DropdownDTO(...)`) — the compiler does not check these strings, so they must be updated by hand on any package move or they fail at runtime.

### Master/Detail pattern for transactional entries
Purchase Entry/Return, Sales Entry/Return, and Journal modules all follow the same shape:
- `Master*` (header: date, totals, party, transaction type) `@OneToMany` → list of line items.
- DTO mirror: `Master*DTO` contains a `List<*DTO>` of line items.
- Save/update flow rewrites the line items and adjusts derived state (stock, journal entries) in a single `@Transactional` method. See [PurchaseEntryService.java](src/main/java/com/sandipsky/inventory_system/features/purchase/purchase_entry/services/PurchaseEntryService.java) as the reference implementation — the others follow the same pattern.
- Sales Entry is the one exception on lifecycle: instead of hard delete it has **cancel** (`POST /sales/{id}` sets `isCancelled` + `cancelRemarks`, restores stock, removes journal entries; cancelled entries can't be updated).

### Inventory + Accounting are coupled
Saving a Master purchase/sales entry or return does three things atomically:
1. Persists the master + line items.
2. Updates `ProductStock` quantities — purchase / sales-return = +qty; sale / purchase-return = −qty (purchase entry also updates `Product` master prices). Any operation that would push stock below zero is rejected (`"Not enough Quantity In Stock"` / `"Product Stock is Already Used"`).
3. Calls `createJournalEntries(...)` which deletes any prior `MasterJournalEntry` linked to that master (via the `master_*_id` link columns on `master_journal_entry`) and writes new balanced debit/credit `JournalEntry` rows against named accounts in `account_master` — purchases use `"VAT Free Purchase"` / `"VAT Purchase"`, sales use `"VAT Free Sales"` / `"VAT Sales"`, both use `"Tax"`, `"Adjustment"`, and `"Cash"` or the party's own account (looked up by `vendorId` / `customerId`). Returns post the mirror image (debits and credits swapped). **These named accounts must exist in `account_master` or the save will throw `ResourceNotFoundException`.**

When changing the columns/totals on a Master entry, also update `createJournalEntries` so the books balance.

Related invariants that hang off the same save flows:
- **Due invoices**: non-Cash (credit) purchase/sales entries maintain an `AmountDueInvoice` row keyed by `systemEntryNo`. Payments (`/payment`) and payment adjustments (`/payment-adjustment`) allocate against these rows and post their own two-line journal (party account vs payment-mode account). Deleting/cancelling an entry with recorded payments is rejected.
- **Journal ↔ source links**: `master_journal_entry` has nullable `master_*_id` FK columns linking each auto-posted journal to its source (purchase/sales entry, returns, payment). These are mapped `@ManyToOne` on purpose — `@OneToOne` would emit UNIQUE FK columns which SQLite cannot add via `ALTER TABLE`, silently breaking `ddl-auto=update`. Manual journal numbering (`findTopByOrderByIdDescJournal`) only counts journals with all links null and skips the fixed `OPENING-BALANCE` entry.
- **Opening balance** (`/opening-balance`) is stored as a single journal entry with the fixed `systemEntryNo` `OPENING-BALANCE`; saving replaces it wholesale and requires debits = credits.
- **Stock Adjustment** (`/stock-adjustment`, numbered `|SA-`) moves `ProductStock` In/Out with no journal; **Stock Edit** (`/stock-edit`) and **Opening Stock** (`/opening-stock`) write `ProductStock` directly (edit corrects figures, opening-stock only creates a missing row).

### Reports
`features/reports/` is read-only — services reuse feature entities via their own `@Query` repositories (no entities of their own). **All report endpoints are unpaginated `GET`s** taking optional `fromDate`/`toDate` plus `dateType` (`AD` default; `BS` is rejected until Bikram Sambat support lands — see `ReportDateUtil.validateDateType`). Endpoints: `/reports/purchase` (register), `/reports/purchase/vendor`, `/reports/purchase/product`; `/reports/sales`, `/reports/sales/customer`, `/reports/sales/product` (aggregates exclude cancelled entries); `/reports/inventory/stock` (no date params); `/reports/accounting/journal`, `/ledger/{accountId}` (running balance + opening balance), `/trial-balance`, and `/due-amount` (outstanding credit-sales invoices from `AmountDueInvoice`, no date params). Dates are TEXT and compared as strings, so date-range params must use the same format the entries store.

### Document numbering
Human-readable numbers (e.g. `PUR0001`, `SAL0001`, `JV0001`) are generated by [DocumentNumberingService.java](src/main/java/com/sandipsky/inventory_system/features/settings/document_numbering/services/DocumentNumberingService.java) per module, using prefix + zero-padded sequence. The next number is derived from the **latest existing master row's `systemEntryNo`**, not from a counter column — so manual DB edits to that field can break sequencing. A `DocumentNumbering` row per module (`Purchase`, `Sales`, `Journal`) must exist with `prefix`, `startNumber`, `endNumber`, `length` configured.

### Paginated list endpoints
Every domain exposes `GET /<resource>/view` taking query params: `pageIndex` (default 0), `pageSize` (default 25), `sort` (`field:asc|desc`, comma-separated for multi-sort, e.g. `sort=name:asc,code:desc`), and **every other query param is treated as a filter** (`?name=abc&party.name=xyz`). Controllers take `@RequestParam Map<String, String> params` and pass it straight to the service, which uses [QueryParamUtil.java](src/main/java/com/sandipsky/inventory_system/common/util/QueryParamUtil.java): `toPageable(params)` builds the `Pageable` (page, size, sort) and `toFilterParams(params)` strips the reserved params and blank values, leaving the filter map. The generic [SpecificationBuilder.java](src/main/java/com/sandipsky/inventory_system/common/util/SpecificationBuilder.java) turns that map into a JPA `Specification` doing **case-insensitive `LIKE` on every filter field** (cast to string + lowercased). It supports nested fields via dot notation (e.g. `party.name`). Unknown fields are silently skipped. Repositories must extend `JpaSpecificationExecutor<T>` to plug in. (`ProductService` additionally pulls a `productType` pseudo-filter out of the map and applies it as its own boolean spec.)

When adding a new domain, copy this controller pattern: `GET /view` (paginated list, `@RequestParam Map<String, String> params`), `GET /` (full list, where applicable), `GET /{id}`, `POST /`, `PUT /{id}`, `DELETE /{id}`.

### Response envelope
Mutating endpoints return `ResponseEntity<ApiResponse<T>>` built via [ResponseUtil.success(id, message)](src/main/java/com/sandipsky/inventory_system/common/util/ResponseUtil.java). `ApiResponse.data` is an `int` (the affected entity's ID) serialized as `post_data_id` in JSON — this is intentional, frontend depends on that field name. Read endpoints return DTOs / `Page<DTO>` directly without the envelope.

### Errors
Throw the typed exceptions in [common/exception/](src/main/java/com/sandipsky/inventory_system/common/exception/) — [GlobalExceptionHandler.java](src/main/java/com/sandipsky/inventory_system/common/exception/GlobalExceptionHandler.java) maps them to HTTP codes (`ResourceNotFoundException` → 404, `DuplicateResourceException` → 409, `BadCredentialsException` → 401, `AccountLockException` / `AccessDeniedException` / `ExpiredJwtException` → 403, anything else → 500). All responses go through `ResponseUtil.error(...)`.

### Security / Auth
- JWT-based, stateless. Login at `POST /login` returns a token; subsequent requests send `Authorization: Bearer <token>`. [AuthTokenFilter.java](src/main/java/com/sandipsky/inventory_system/security/AuthTokenFilter.java) runs before `UsernamePasswordAuthenticationFilter` and populates `SecurityContextHolder` if the token is valid.
- Account lockout: 5 failed login attempts (`auth.maxFailedAttempts`) lock the account for 1 hour (`auth.lockTimeDurationMs`). The unlock check happens on the next login attempt, in [AuthService.authenticate](src/main/java/com/sandipsky/inventory_system/features/auth/services/AuthService.java).
- `WebSecurityConfig` permits only `POST /login` and `GET /configurations`; every other endpoint requires a valid Bearer token (`anyRequest().authenticated()`). Unauthenticated requests get the 401 JSON from `AuthEntryPoint`.
- CORS is hardcoded to `http://localhost:8005` in [WebSecurityConfig.java](src/main/java/com/sandipsky/inventory_system/security/WebSecurityConfig.java).

### Multipart endpoints
User create/update accept `multipart/form-data` with a `user` JSON part and an optional `image` file part — see [UserController.java](src/main/java/com/sandipsky/inventory_system/features/user/controllers/UserController.java) for the pattern (`@RequestPart("user") UserDTO`, `@RequestPart("image") MultipartFile`). Image is stored to `uploads/` and the path persisted on `User.imageUrl`.

### Dropdown endpoint
[DropdownController.java](src/main/java/com/sandipsky/inventory_system/common/dropdown/controllers/DropdownController.java) exposes `GET /dropdown/<resource>/...` returning lightweight `{id, name}` lists for select widgets, with status/type filters baked into the URL path.

## Conventions

- Lombok `@Getter` / `@Setter` everywhere — don't hand-write accessors. Annotation processor is wired in [pom.xml](pom.xml).
- IDs are `int` (not `Integer` / `Long`) across entities, DTOs, and path variables — keep that consistent.
- Each service instantiates its own `SpecificationBuilder<EntityType>` as a `private final` field; reuse that instead of creating a new builder per request.
- DTO ↔ Entity mapping is hand-rolled inside services (no MapStruct / ModelMapper). When adding a field, update `mapToDTO`, `mapDtoToEntity`, the save method, the update method, and the `getById` method — they each map fields independently.
