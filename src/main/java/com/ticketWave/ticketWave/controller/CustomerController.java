package com.ticketWave.ticketWave.controller;

import com.ticketWave.ticketWave.dto.CustomerDTO;
import com.ticketWave.ticketWave.dto.SystemDTO;
import com.ticketWave.ticketWave.dto.UserDTO;
import com.ticketWave.ticketWave.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/customer")
@CrossOrigin
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SystemDTO systemDTO;

    @GetMapping("/{customerID}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable int customerID) {
        CustomerDTO customer =  customerService.getCustomer(customerID);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@RequestBody CustomerDTO customerDTO) {
        CustomerDTO customer = customerService.registerCustomer(customerDTO);
        if (customer == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{customerID}")
    public ResponseEntity<String> updateCustomer(@PathVariable int customerID, @RequestBody CustomerDTO customerDTO) {
        CustomerDTO updatedCustomer = customerService.updateCustomer(customerID, customerDTO);
        if (updatedCustomer != null) {
            return ResponseEntity.ok("Customer has been updated");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{customerID}/purchaseTicket/{count}")
    public ResponseEntity<String> purchaseTicket(@PathVariable int customerID, @PathVariable int count) {
        if (systemDTO.isRunning()) {
            customerService.purchaseTicket(customerID, count);
            return ResponseEntity.accepted().build();
        } else {
            System.out.println("System is not running");
            return ResponseEntity.badRequest().build();
        }
    }
}
