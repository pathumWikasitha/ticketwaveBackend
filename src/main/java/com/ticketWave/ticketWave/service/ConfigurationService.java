package com.ticketWave.ticketWave.service;

import com.ticketWave.ticketWave.dto.ConfigurationDTO;
import com.ticketWave.ticketWave.model.Configuration;
import com.ticketWave.ticketWave.repo.ConfigurationRepo;
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

    public void setConfiguration(ConfigurationDTO configuration) {
        configurationRepo.save(modelMapper.map(configuration, Configuration.class));
    }


    public ConfigurationDTO getConfiguration() {
        return modelMapper.map(configurationRepo.findAll().getLast(), ConfigurationDTO.class);
    }

    public void deleteConfiguration() {
        configurationRepo.deleteAll();
    }


}
