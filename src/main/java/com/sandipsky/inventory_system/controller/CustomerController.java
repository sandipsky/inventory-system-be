package com.sandipsky.inventory_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sandipsky.inventory_system.dto.ApiResponse;
import com.sandipsky.inventory_system.dto.CustomerDTO;
import com.sandipsky.inventory_system.dto.filter.RequestDTO;
import com.sandipsky.inventory_system.entity.Customer;
import com.sandipsky.inventory_system.service.CustomerService;
import com.sandipsky.inventory_system.util.ResponseUtil;

import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @GetMapping()
    public List<CustomerDTO> getCustomers() {
        return service.getCustomers();
    }

    @PostMapping("/view")
    public Page<CustomerDTO> getPaginatedCustomersList(@RequestBody RequestDTO request) {
        return service.getPaginatedCustomersList(request);
    }

    @GetMapping("/{id}")
    public CustomerDTO getCustomer(@PathVariable int id) {
        return service.getCustomerById(id);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<Customer>> createCustomer(@RequestBody CustomerDTO customer) {
        Customer res = service.saveCustomer(customer);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "Customer Added successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Customer>> updateCustomer(@PathVariable int id,
            @RequestBody CustomerDTO customer) {
        Customer res = service.updateCustomer(id, customer);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "Customer Updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Customer>> deleteCustomer(@PathVariable int id) {
        service.deleteCustomer(id);
        return ResponseEntity.ok(ResponseUtil.success(id, "Customer Deleted successfully"));
    }
}
