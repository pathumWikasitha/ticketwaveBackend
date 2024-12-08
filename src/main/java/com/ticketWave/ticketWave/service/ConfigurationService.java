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
        configurationRepo.save(modelMapper.map(configuration, Configuration.class));
        logger.info("Configuration saved");
        return modelMapper.map(configuration, ConfigurationDTO.class);
    }

    public ConfigurationDTO updateConfiguration(ConfigurationDTO configuration) {
        int configurationID = configurationRepo.findAll().getLast().getConfigurationId();
        configuration.setConfigurationId(configurationID);
        configurationRepo.save(modelMapper.map(configuration, Configuration.class));
        logger.info("Configuration Updated");
        return modelMapper.map(configuration, ConfigurationDTO.class);
    }


    public ConfigurationDTO getConfiguration() {
        Configuration configuration;
        try {
            configuration = configurationRepo.findAll().getLast();
            if (configuration != null) {
                logger.info("Configuration found");
                return modelMapper.map(configuration, ConfigurationDTO.class);
            }

        } catch (Exception e) {
            logger.error("Configuration not found");
        }
        return null;

    }

    public Boolean deleteConfiguration() {
        try {
            int configurationId = configurationRepo.findAll().getLast().getConfigurationId();
            configurationRepo.deleteById(configurationId);
            systemDTO.setRunning(Boolean.FALSE);
            logger.info("Configuration deleted.");
            return true;
        } catch (Exception e) {
            logger.error("Configuration not found to delete");
        }
        return false;
    }


}
