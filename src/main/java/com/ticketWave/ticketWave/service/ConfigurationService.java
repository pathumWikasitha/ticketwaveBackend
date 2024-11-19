package com.ticketWave.ticketWave.service;

import com.ticketWave.ticketWave.dto.ConfigurationDTO;
import com.ticketWave.ticketWave.model.Configuration;
import com.ticketWave.ticketWave.repo.ConfigurationRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ConfigurationService {
    private final ConfigurationRepo configurationRepo;
    private final ModelMapper modelMapper;


    public ConfigurationService(ConfigurationRepo configurationRepo, ModelMapper modelMapper) {
        this.configurationRepo = configurationRepo;
        this.modelMapper = modelMapper;
    }

    public ConfigurationDTO setConfiguration(ConfigurationDTO configuration) {
        configurationRepo.deleteAll();
        configurationRepo.save(modelMapper.map(configuration, Configuration.class));
        return modelMapper.map(configuration, ConfigurationDTO.class);
    }


    public ConfigurationDTO getConfiguration() {
        Configuration configuration;
        try {
            configuration = configurationRepo.findAll().getFirst();
            if (configuration != null) {
                return modelMapper.map(configuration, ConfigurationDTO.class);
            }

        } catch (Exception e) {
            System.out.println("Configuration not found");
        }
        return null;

    }

    public void deleteConfiguration() {
        configurationRepo.deleteAll();
    }


}
