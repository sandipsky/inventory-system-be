package com.sandipsky.inventory_system.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sandipsky.inventory_system.common.dto.ApiResponse;
import com.sandipsky.inventory_system.common.dto.filter.RequestDTO;
import com.sandipsky.inventory_system.security.RequiresOperation;
import com.sandipsky.inventory_system.common.util.ResponseUtil;

import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @GetMapping()
    @RequiresOperation("ViewCustomer")
    public List<CustomerDTO> getCustomers() {
        return service.getCustomers();
    }

    @PostMapping("/view")
    @RequiresOperation("ViewCustomer")
    public Page<CustomerDTO> getPaginatedCustomersList(@RequestBody RequestDTO request) {
        return service.getPaginatedCustomersList(request);
    }

    @GetMapping("/{id}")
    @RequiresOperation("ViewCustomer")
    public CustomerDTO getCustomer(@PathVariable int id) {
        return service.getCustomerById(id);
    }

    @PostMapping()
    @RequiresOperation("CreateCustomer")
    public ResponseEntity<ApiResponse<Customer>> createCustomer(@RequestBody CustomerDTO customer) {
        Customer res = service.saveCustomer(customer);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "Customer Added successfully"));
    }

    @PutMapping("/{id}")
    @RequiresOperation("EditCustomer")
    public ResponseEntity<ApiResponse<Customer>> updateCustomer(@PathVariable int id,
            @RequestBody CustomerDTO customer) {
        Customer res = service.updateCustomer(id, customer);
        return ResponseEntity.ok(ResponseUtil.success(res.getId(), "Customer Updated successfully"));
    }

    @DeleteMapping("/{id}")
    @RequiresOperation("DeleteCustomer")
    public ResponseEntity<ApiResponse<Customer>> deleteCustomer(@PathVariable int id) {
        service.deleteCustomer(id);
        return ResponseEntity.ok(ResponseUtil.success(id, "Customer Deleted successfully"));
    }
}
