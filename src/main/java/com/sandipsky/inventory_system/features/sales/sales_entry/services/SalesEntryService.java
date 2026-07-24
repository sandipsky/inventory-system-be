package com.sandipsky.inventory_system.features.sales.sales_entry.services;
import com.sandipsky.inventory_system.features.sales.sales_entry.dtos.SalesEntryDTO;
import com.sandipsky.inventory_system.features.sales.sales_entry.entities.SalesEntry;
import com.sandipsky.inventory_system.features.sales.sales_entry.repositories.SalesEntryRepository;
import com.sandipsky.inventory_system.features.sales.sales_entry.repositories.MasterSalesEntryRepository;
import com.sandipsky.inventory_system.features.sales.sales_entry.dtos.MasterSalesEntryDTO;
import com.sandipsky.inventory_system.features.sales.sales_entry.entities.MasterSalesEntry;
import com.sandipsky.inventory_system.features.settings.document_numbering.services.DocumentNumberingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sandipsky.inventory_system.common.dto.filter.RequestDTO;
import com.sandipsky.inventory_system.features.sales.customer.entities.Customer;
import com.sandipsky.inventory_system.features.product.entities.ProductStock;
import com.sandipsky.inventory_system.common.exception.ResourceNotFoundException;
import com.sandipsky.inventory_system.features.sales.customer.repositories.CustomerRepository;
import com.sandipsky.inventory_system.features.product.repositories.ProductStockRepository;
import com.sandipsky.inventory_system.common.util.SpecificationBuilder;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

@Service
public class SalesEntryService {

    @Autowired
    private MasterSalesEntryRepository repository;

    @Autowired
    private SalesEntryRepository salesEntryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductStockRepository productStockRepository;

    @Autowired
    private DocumentNumberingService documentNumberService;

    private final SpecificationBuilder<MasterSalesEntry> specBuilder = new SpecificationBuilder<>();

    public Page<MasterSalesEntryDTO> getPaginatedMasterSalesEntrysList(RequestDTO request) {
        Pageable pageable = PageRequest.of(
                request.getPagination() != null ? request.getPagination().getPageIndex() : 0,
                request.getPagination() != null ? request.getPagination().getPageSize() : 25,
                specBuilder.buildSort(request.getSortDTO()));

        Specification<MasterSalesEntry> spec = specBuilder.buildSpecification(request.getFilter());
        Page<MasterSalesEntry> productPage = repository.findAll(spec, pageable);
        return productPage.map(this::mapToDTO);
    }

    public MasterSalesEntryDTO getMasterSalesEntryById(int id) {
        MasterSalesEntry masterSalesEntry = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales Entry with Given Id not found"));
        MasterSalesEntryDTO masterSalesEntryDTO = new MasterSalesEntryDTO();
        masterSalesEntryDTO.setId(masterSalesEntry.getId());
        masterSalesEntryDTO.setDate(masterSalesEntry.getDate());
        masterSalesEntryDTO.setSystemEntryNo(masterSalesEntry.getSystemEntryNo());
        masterSalesEntryDTO.setTransactionType(masterSalesEntry.getTransactionType());
        masterSalesEntryDTO.setSubTotal(masterSalesEntry.getSubTotal());
        masterSalesEntryDTO.setDiscount(masterSalesEntry.getDiscount());
        masterSalesEntryDTO.setNonTaxableAmount(masterSalesEntry.getNonTaxableAmount());
        masterSalesEntryDTO.setTaxableAmount(masterSalesEntry.getTaxableAmount());
        masterSalesEntryDTO.setTotalTax(masterSalesEntry.getTotalTax());
        masterSalesEntryDTO.setRounded(masterSalesEntry.isRounded());
        masterSalesEntryDTO.setRounding(masterSalesEntry.getRounding());
        masterSalesEntryDTO.setGrandTotal(masterSalesEntry.getGrandTotal());
        masterSalesEntryDTO.setDiscountType(masterSalesEntry.getDiscountType());
        masterSalesEntryDTO.setRemarks(masterSalesEntry.getRemarks());
        if (masterSalesEntry.getCustomer() != null) {
            masterSalesEntryDTO.setCustomerId(masterSalesEntry.getCustomer().getId());
            masterSalesEntryDTO.setCustomerName(masterSalesEntry.getCustomer().getName());
        }

        if (masterSalesEntry.getSalesEntries() != null) {
            masterSalesEntryDTO.setSalesEntries(
                    masterSalesEntry.getSalesEntries().stream()
                            .map(salesEntry -> {
                                SalesEntryDTO salesEntryDTO = new SalesEntryDTO();
                                salesEntryDTO.setId(salesEntry.getId());
                                salesEntryDTO.setQuantity(salesEntry.getQuantity());
                                salesEntryDTO.setCostPrice(salesEntry.getCostPrice());
                                salesEntryDTO.setSellingPrice(salesEntry.getSellingPrice());
                                salesEntryDTO.setMrp(salesEntry.getMrp());
                                salesEntryDTO.setMasterSalesEntryId(salesEntry.getMasterSalesEntryId());
                                salesEntryDTO.setProductId(salesEntry.getProduct().getId());
                                salesEntryDTO.setProductName(salesEntry.getProduct().getName());
                                return salesEntryDTO;
                            }).toList());
        }
        return masterSalesEntryDTO;
    }

    @Transactional
    public MasterSalesEntry saveMasterSalesEntry(MasterSalesEntryDTO masterSalesEntryDTO) {
        MasterSalesEntry masterSalesEntry = new MasterSalesEntry();
        masterSalesEntry.setDate(masterSalesEntryDTO.getDate());
        masterSalesEntry.setSystemEntryNo(documentNumberService.generateSalesNumber());
        masterSalesEntry.setTransactionType(masterSalesEntryDTO.getTransactionType());
        masterSalesEntry.setSubTotal(masterSalesEntryDTO.getSubTotal());
        masterSalesEntry.setDiscount(masterSalesEntryDTO.getDiscount());
        masterSalesEntry.setNonTaxableAmount(masterSalesEntryDTO.getNonTaxableAmount());
        masterSalesEntry.setTaxableAmount(masterSalesEntryDTO.getTaxableAmount());
        masterSalesEntry.setTotalTax(masterSalesEntryDTO.getTotalTax());
        masterSalesEntry.setRounded(masterSalesEntryDTO.isRounded());
        masterSalesEntry.setRounding(masterSalesEntryDTO.getRounding());
        masterSalesEntry.setGrandTotal(masterSalesEntryDTO.getGrandTotal());
        masterSalesEntry.setDiscountType(masterSalesEntryDTO.getDiscountType());

        Customer customer = customerRepository.findById(masterSalesEntryDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        masterSalesEntry.setCustomer(customer);

        if (masterSalesEntryDTO.getRemarks().isEmpty() || masterSalesEntryDTO.getRemarks() == null) {
            masterSalesEntry.setRemarks("Sold Goods to " + customer.getName());
        } else {
            masterSalesEntry.setRemarks(masterSalesEntryDTO.getRemarks());
        }

        MasterSalesEntry savedEntry = repository.save(masterSalesEntry);

        if (masterSalesEntryDTO.getSalesEntries() != null) {
            for (SalesEntryDTO item : masterSalesEntryDTO.getSalesEntries()) {
                // Saving sales Entry
                SalesEntry salesEntry = new SalesEntry();
                salesEntry.setQuantity(item.getQuantity());
                salesEntry.setCostPrice(item.getCostPrice());
                salesEntry.setSellingPrice(item.getSellingPrice());
                salesEntry.setMrp(item.getMrp());
                salesEntry.setMasterSalesEntryId(savedEntry.getId());
                salesEntryRepository.save(salesEntry);

                // Changing Product Stock
                ProductStock productStock = productStockRepository.findByProductId(item.getProductId());
                Double qty = productStock.getQuantity() - item.getQuantity();
                if (qty > 0) {
                    productStock.setQuantity(qty);
                } else {
                    throw new RuntimeException("Not enough Quantity In Stock");
                }
                productStockRepository.save(productStock);
            }
        }
        return savedEntry;
    }

    @Transactional
    public void cancelMasterSalesEntry(int id, MasterSalesEntryDTO masterSalesEntryDTO) {
        MasterSalesEntry masterSalesEntry = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales Entry with Given Id not found"));

        if (masterSalesEntry.isCancelled() == true) {
            throw new RuntimeException("Sales Entry Already Cancelled");
        }

        for (SalesEntry item : masterSalesEntry.getSalesEntries()) {
            ProductStock productStock = productStockRepository.findByProductId(item.getProduct().getId());
            productStock.setQuantity(productStock.getQuantity() + item.getQuantity());

            productStockRepository.save(productStock);
            salesEntryRepository.deleteById(item.getId());
        }
        masterSalesEntry.setCancelled(true);
        masterSalesEntry.setCancelRemarks(masterSalesEntryDTO.getCancelRemarks());
        repository.save(masterSalesEntry);
    }

    private MasterSalesEntryDTO mapToDTO(MasterSalesEntry entity) {
        MasterSalesEntryDTO masterSalesEntryDTO = new MasterSalesEntryDTO();
        masterSalesEntryDTO.setId(entity.getId());
        masterSalesEntryDTO.setDate(entity.getDate());
        masterSalesEntryDTO.setSystemEntryNo(entity.getSystemEntryNo());
        masterSalesEntryDTO.setTransactionType(entity.getTransactionType());
        masterSalesEntryDTO.setSubTotal(entity.getSubTotal());
        masterSalesEntryDTO.setDiscount(entity.getDiscount());
        masterSalesEntryDTO.setNonTaxableAmount(entity.getNonTaxableAmount());
        masterSalesEntryDTO.setTaxableAmount(entity.getTaxableAmount());
        masterSalesEntryDTO.setTotalTax(entity.getTotalTax());
        masterSalesEntryDTO.setRounded(entity.isRounded());
        masterSalesEntryDTO.setRounding(entity.getRounding());
        masterSalesEntryDTO.setGrandTotal(entity.getGrandTotal());
        masterSalesEntryDTO.setDiscountType(entity.getDiscountType());
        masterSalesEntryDTO.setRemarks(entity.getRemarks());
        if (entity.getCustomer() != null) {
            masterSalesEntryDTO.setCustomerId(entity.getCustomer().getId());
            masterSalesEntryDTO.setCustomerName(entity.getCustomer().getName());
        }
        return masterSalesEntryDTO;
    }

}
