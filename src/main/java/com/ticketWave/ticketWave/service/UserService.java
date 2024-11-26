package com.ticketWave.ticketWave.service;

import com.ticketWave.ticketWave.dto.AdminDTO;
import com.ticketWave.ticketWave.dto.UserDTO;
import com.ticketWave.ticketWave.model.User;
import com.ticketWave.ticketWave.repo.UserRepo;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final ModelMapper modelMapper;
    private final UserRepo userRepo;
    private static final Logger logger = Logger.getLogger(UserService.class);

    public UserService(ModelMapper modelMapper, UserRepo userRepo) {
        this.modelMapper = modelMapper;
        this.userRepo = userRepo;
    }


    public UserDTO loginUser(UserDTO userDTO) {
        try {
            User user = userRepo.loginUser(userDTO.getEmail(), userDTO.getPassword());
            if (user != null) {
                logger.info(user.getUsername()+" ("+user.getRole()+") "+ " logged in successfully");
                return modelMapper.map(user, AdminDTO.class);
            } else {
                logger.error("Invalid username or password");
            }
        } catch (Exception e) {
            logger.error("Login error" + e.getMessage());
        }
        return null;
    }
}
