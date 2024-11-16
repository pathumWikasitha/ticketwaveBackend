package com.ticketWave.ticketWave.controller;

import com.ticketWave.ticketWave.dto.AdminDTO;
import com.ticketWave.ticketWave.dto.ConfigurationDTO;
import com.ticketWave.ticketWave.service.AdminService;
import com.ticketWave.ticketWave.service.ConfigurationService;
import com.ticketWave.ticketWave.service.CustomerService;
import com.ticketWave.ticketWave.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/admin")
public class AdminController {

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/getConfiguration")
    public ConfigurationDTO getConfiguration() {
        return configurationService.getConfiguration();
    }

    @PostMapping("/setConfiguration")
    public void setConfiguration(@RequestBody ConfigurationDTO configurationDTO) {
        configurationService.setConfiguration(configurationDTO);
    }

    @DeleteMapping("/deleteConfiguration")
    public void deleteConfiguration() {
        configurationService.deleteConfiguration();
    }

    @PostMapping("/register")
    public void registerAdmin(@RequestBody AdminDTO adminDTO) {
        adminService.registerAdmin(adminDTO);
    }

    @PutMapping("/update/{adminID}")
    public void updateAdmin(@PathVariable int adminID, @RequestBody AdminDTO adminDTO) {
        adminService.updateAdmin(adminID,adminDTO);
    }

    @PostMapping("/start")
    public void startSystem(){
        adminService.startSystem();
    }

    @PostMapping("/stop")
    public void stopThreads(){
        vendorService.stopVendorThreads();
        customerService.stopCustomerThreads();
    }
}

