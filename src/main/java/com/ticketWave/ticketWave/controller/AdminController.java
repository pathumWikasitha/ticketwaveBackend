package com.ticketWave.ticketWave.controller;

import com.ticketWave.ticketWave.dto.ConfigurationDTO;
import com.ticketWave.ticketWave.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1")
public class AdminController {

    @Autowired
    private ConfigurationService configurationService;

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
}

