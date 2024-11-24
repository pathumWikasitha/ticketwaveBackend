package com.ticketWave.ticketWave.controller;

import com.ticketWave.ticketWave.dto.AdminDTO;
import com.ticketWave.ticketWave.dto.ConfigurationDTO;
import com.ticketWave.ticketWave.dto.SystemDTO;
import com.ticketWave.ticketWave.service.AdminService;
import com.ticketWave.ticketWave.service.ConfigurationService;
import com.ticketWave.ticketWave.service.CustomerService;
import com.ticketWave.ticketWave.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private SystemDTO systemDTO;

    @GetMapping("/getConfiguration")
    public ResponseEntity<ConfigurationDTO> getConfiguration() {
        ConfigurationDTO configurationDTO = configurationService.getConfiguration();
        if (configurationDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(configurationDTO);
    }

    @PostMapping("/saveConfiguration")
    public ResponseEntity<String> setConfiguration(@RequestBody ConfigurationDTO configurationDTO) {
        ConfigurationDTO config = configurationService.setConfiguration(configurationDTO);
        if (config == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok("Configuration saved successfully");
        }
    }

    @DeleteMapping("/deleteConfiguration")
    public ResponseEntity<String> deleteConfiguration() {
        configurationService.deleteConfiguration();
        return ResponseEntity.ok("Configuration deleted successfully");
    }

    @GetMapping("/{adminID}")
    public ResponseEntity<AdminDTO> getAdminByID(@PathVariable int adminID) {
        AdminDTO adminDTO = adminService.getAdminByID(adminID);
        if (adminDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adminDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerAdmin(@RequestBody AdminDTO adminDTO) {
        AdminDTO admin = adminService.registerAdmin(adminDTO);
        if (admin == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok("Admin registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AdminDTO> loginAdmin(@RequestBody AdminDTO adminDTO) {
        AdminDTO login = adminService.loginAdmin(adminDTO);
        if (login == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(login);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateAdmin(@RequestBody AdminDTO adminDTO) {
        AdminDTO updatedAdmin = adminService.updateAdmin(adminDTO);
        if (updatedAdmin == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Admin updated successfully.");
    }

    @PostMapping("/start")
    public ResponseEntity<String> startSystem() {
        if (systemDTO.isRunning() && configurationService.getConfiguration() == null) {
            return ResponseEntity.badRequest().build();
        }
        adminService.startSystem();
        return ResponseEntity.ok("System started...");
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopThreads() {
        if (systemDTO.isRunning()) {
            vendorService.stopVendorThreads();
            customerService.stopCustomerThreads();
            systemDTO.setRunning(false);
            return ResponseEntity.ok("System stopped..");
        }
        return ResponseEntity.ok("System not running");
    }
}

