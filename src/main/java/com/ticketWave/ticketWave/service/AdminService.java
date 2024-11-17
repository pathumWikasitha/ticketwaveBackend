package com.ticketWave.ticketWave.service;

import com.ticketWave.ticketWave.dto.AdminDTO;
import com.ticketWave.ticketWave.dto.SystemDTO;
import com.ticketWave.ticketWave.model.Admin;
import com.ticketWave.ticketWave.model.User;
import com.ticketWave.ticketWave.repo.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final ModelMapper modelMapper;
    private final UserRepo userRepo;
    private final SystemDTO systemDTO;

    public AdminService(ModelMapper modelMapper, UserRepo userRepo,SystemDTO systemDTO) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.systemDTO = systemDTO;
    }

    public AdminDTO updateAdmin(int adminID, AdminDTO adminDTO) {
        User user = userRepo.findByUserID(adminID);
        if (user != null) {
            Admin admin = userRepo.save(modelMapper.map(adminDTO, Admin.class));
            return modelMapper.map(admin, AdminDTO.class);
        }else {
            return null;
        }
    }

    public AdminDTO registerAdmin(AdminDTO adminDTO) {
        userRepo.save(modelMapper.map(adminDTO, User.class));
        return adminDTO;
    }

    public void startSystem() {
        systemDTO.setRunning(true);
    }

    public AdminDTO getAdminByID(int adminID) {
        User user = userRepo.findByUserID(adminID);
        if (user != null) {
            user.setPassword("");
            return modelMapper.map(user, AdminDTO.class);
        }else {
            return null;
        }
    }
}
