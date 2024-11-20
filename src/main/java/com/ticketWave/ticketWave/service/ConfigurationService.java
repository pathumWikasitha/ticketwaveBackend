package com.ticketWave.ticketWave.service;

import com.ticketWave.ticketWave.dto.ConfigurationDTO;
import com.ticketWave.ticketWave.dto.SystemDTO;
import com.ticketWave.ticketWave.model.Configuration;
import com.ticketWave.ticketWave.repo.ConfigurationRepo;
import jakarta.transaction.Transactional;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ConfigurationService {
    private final ConfigurationRepo configurationRepo;
    private final ModelMapper modelMapper;
    private final SystemDTO systemDTO;
    private static final Logger logger = LogManager.getLogger(ConfigurationService.class);


    public ConfigurationService(ConfigurationRepo configurationRepo, ModelMapper modelMapper, SystemDTO systemDTO) {
        this.configurationRepo = configurationRepo;
        this.modelMapper = modelMapper;
        this.systemDTO = systemDTO;
    }

    public ConfigurationDTO setConfiguration(ConfigurationDTO configuration) {
        configurationRepo.deleteAll();
        configurationRepo.save(modelMapper.map(configuration, Configuration.class));
        logger.info("Configuration saved");
        return modelMapper.map(configuration, ConfigurationDTO.class);
    }


    public ConfigurationDTO getConfiguration() {
        Configuration configuration;
        try {
            configuration = configurationRepo.findAll().getFirst();
            if (configuration != null) {
                logger.info("Configuration found");
                return modelMapper.map(configuration, ConfigurationDTO.class);
            }

        } catch (Exception e) {
            logger.error("Configuration not found");
        }
        return null;

    }

    public void deleteConfiguration() {
        configurationRepo.deleteAll();
        systemDTO.setRunning(Boolean.FALSE);
        logger.info("Configuration deleted.");
    }


}
