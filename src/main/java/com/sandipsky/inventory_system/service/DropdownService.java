package com.sandipsky.inventory_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sandipsky.inventory_system.dto.DropdownDTO;
import com.sandipsky.inventory_system.repository.AccountMasterRepository;
import com.sandipsky.inventory_system.repository.CategoryRepository;
import com.sandipsky.inventory_system.repository.PackingRepository;
import com.sandipsky.inventory_system.repository.PartyRepository;
import com.sandipsky.inventory_system.repository.ProductRepository;
import com.sandipsky.inventory_system.repository.TaxTypeRepository;
import com.sandipsky.inventory_system.repository.UnitRepository;
import com.sandipsky.inventory_system.repository.UserRepository;

import java.util.List;

@Service
public class DropdownService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private AccountMasterRepository accountRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private PackingRepository packingRepository;

    @Autowired
    private TaxTypeRepository taxTypeRepository;

    @Autowired
    private UserRepository userRepository;

    public List<DropdownDTO> getProductsDropdown(String serviceType, String type, String status) {
        Boolean isService = switch (serviceType.toLowerCase()) {
            case "service" -> true;
            case "inventory" -> false;
            default -> null; // "all"
        };
        Boolean isPurchasable = "purchasable".equalsIgnoreCase(type) ? true : null;
        Boolean isSellable = "sellable".equalsIgnoreCase(type) ? true : null;
        Boolean isActive = "active".equalsIgnoreCase(status) ? true : null;
        return productRepository.findFilteredDropdown(isService, isPurchasable, isSellable, isActive);
    }

    public List<DropdownDTO> getPartyDropdown(String type, String status) {
        String productType = switch (type.toLowerCase()) {
            case "customer" -> "Customer";
            case "vendor" -> "Vendor";
            default -> null; // "all"
        };
        Boolean isActive = "active".equalsIgnoreCase(status) ? true : null;
        return partyRepository.findFilteredDropdown(productType, isActive);
    }

    public List<DropdownDTO> getAccountMasterDropdown(String type, String partyType, String status) {
        Boolean atype = switch (type.toLowerCase()) {
            case "party" -> true;
            case "nonparty" -> false;
            default -> null; // "all"
        };
        String ptype = switch (partyType.toLowerCase()) {
            case "customer" -> "Customer";
            case "vendor" -> "Vendor";
            default -> null; // "all"
        };
        Boolean isActive = "active".equalsIgnoreCase(status) ? true : null;
        return accountRepository.findFilteredDropdown(atype, ptype, isActive);
    }

    public List<DropdownDTO> getUnitsDropdown(String status) {
        Boolean isActive = "active".equalsIgnoreCase(status) ? true : null;
        return unitRepository.findFilteredDropdown(isActive);
    }

    public List<DropdownDTO> getPackingDropdown(String status) {
        Boolean isActive = "active".equalsIgnoreCase(status) ? true : null;
        return packingRepository.findFilteredDropdown(isActive);
    }

    public List<DropdownDTO> getTaxTypeDropdown(String status) {
        Boolean isActive = "active".equalsIgnoreCase(status) ? true : null;
        return taxTypeRepository.findFilteredDropdown(isActive);
    }

    public List<DropdownDTO> getCategoryDropdown(String status) {
        Boolean isActive = "active".equalsIgnoreCase(status) ? true : null;
        return categoryRepository.findFilteredDropdown(isActive);
    }

    public List<DropdownDTO> getUserDropdown(String status) {
        Boolean isActive = "active".equalsIgnoreCase(status) ? true : null;
        return userRepository.findFilteredDropdown(isActive);
    }

}
