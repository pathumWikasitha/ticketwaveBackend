package com.ticketWave.ticketWave.controller;

import com.ticketWave.ticketWave.dto.AdminDTO;
import com.ticketWave.ticketWave.dto.ConfigurationDTO;
import com.ticketWave.ticketWave.dto.SystemDTO;
import com.ticketWave.ticketWave.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/admin")
public class AdminController {
    private static final Logger logger = Logger.getLogger(AdminController.class);

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
            return ResponseEntity.ok(null);
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
    @PutMapping("/updateConfiguration")
    public ResponseEntity<ConfigurationDTO> updateConfiguration(@RequestBody ConfigurationDTO configurationDTO) {
        ConfigurationDTO config = configurationService.updateConfiguration(configurationDTO);
        if (config == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(config);
        }
    }

    @DeleteMapping("/deleteConfiguration")
    public ResponseEntity<String> deleteConfiguration() {
        Boolean deleteStatus = configurationService.deleteConfiguration();
        if (deleteStatus) {
            return ResponseEntity.ok("Configuration deleted successfully");
        }else {
            return ResponseEntity.badRequest().build();
        }
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


    @PutMapping("/update")
    public ResponseEntity<AdminDTO> updateAdmin(@RequestBody AdminDTO adminDTO) {
        AdminDTO updatedAdmin = adminService.updateAdmin(adminDTO);
        if (updatedAdmin == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedAdmin);
    }

    @PostMapping("/start")
    public ResponseEntity<String> startSystem() {
        if (systemDTO.isRunning() && configurationService.getConfiguration() == null) {
            return ResponseEntity.badRequest().build();
        }else if (systemDTO.isRunning()) {
            return ResponseEntity.ok("System is already running");
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
            logger.info("System stop successfully");
            return ResponseEntity.ok("System stopped..");
        }
        return ResponseEntity.ok("System not running");
    }

    @GetMapping("/systemStatus")
    public ResponseEntity<SystemDTO> getSystemStatus() {
        logger.info(systemDTO.isRunning()?"Get System Status: System Is Running":"Get System Status: System is not Running");
        return ResponseEntity.ok(systemDTO);

    }
}

