package com.ticketWave.ticketWave.service;

import com.ticketWave.ticketWave.dto.AdminDTO;
import com.ticketWave.ticketWave.dto.SystemDTO;
import com.ticketWave.ticketWave.model.Admin;
import com.ticketWave.ticketWave.model.User;
import com.ticketWave.ticketWave.repo.UserRepo;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final ModelMapper modelMapper;
    private final UserRepo userRepo;
    private final SystemDTO systemDTO;
    private static final Logger logger = Logger.getLogger(AdminService.class);

    public AdminService(ModelMapper modelMapper, UserRepo userRepo, SystemDTO systemDTO) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.systemDTO = systemDTO;
    }

    public AdminDTO updateAdmin(AdminDTO adminDTO) {
        User user;
        try {
            user = userRepo.findUser(Math.toIntExact(adminDTO.getId()), "ADMIN");
            if (user != null) {
                Admin admin = userRepo.save(modelMapper.map(adminDTO, Admin.class));
                logger.info("Admin" + adminDTO.getId() + " updated successfully");
                return modelMapper.map(admin, AdminDTO.class);
            }
        } catch (Exception e) {
            logger.error("Admin" + adminDTO.getId() + " not found");
        }
        return null;
    }

    public AdminDTO registerAdmin(AdminDTO adminDTO) {
        adminDTO.setRole("ADMIN");
        Admin admin = modelMapper.map(adminDTO, Admin.class);
        userRepo.save(admin);
        logger.info("Admin " + adminDTO.getId() + " created successfully");
        return adminDTO;
    }

    public AdminDTO loginAdmin(AdminDTO adminDTO) {
        try {
            User user = userRepo.loginUser(adminDTO.getEmail(), adminDTO.getPassword(), adminDTO.getRole());
            if (user != null) {
                logger.info("Admin" + user.getId() + " logged in successfully");
                return modelMapper.map(user, AdminDTO.class);
            } else {
                logger.error("Invalid username or password");
            }
        } catch (Exception e) {
            logger.error("Login error" + e.getMessage());
        }
        return null;
    }

    public void startSystem() {
        systemDTO.setRunning(true);
        logger.info("System started successfully");
    }

    public AdminDTO getAdminByID(int adminID) {
        User user;
        try {
            user = userRepo.findUser(adminID, "ADMIN");
            if (user != null) {
                user.setPassword("");
                logger.info("Get admin" + adminID + " successfully");
                return modelMapper.map(user, AdminDTO.class);
            }
        } catch (Exception e) {
            logger.error("Get admin" + adminID + " not found");
        }
        return null;
    }

}
