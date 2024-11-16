package com.ticketWave.ticketWave.service;

import com.ticketWave.ticketWave.dto.SystemDTO;
import com.ticketWave.ticketWave.dto.UserDTO;
import com.ticketWave.ticketWave.dto.VendorDTO;
import com.ticketWave.ticketWave.model.Ticket;
import com.ticketWave.ticketWave.dto.TicketPoolDTO;
import com.ticketWave.ticketWave.model.User;
import com.ticketWave.ticketWave.model.Vendor;
import com.ticketWave.ticketWave.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class VendorService {
    private final TicketPoolDTO ticketPool;
    private final ConfigurationService configurationService;
    private final SystemDTO systemDTO;
    private final List<Thread> vendorThreads = new ArrayList<>();

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;


    public VendorService(TicketPoolDTO ticketPool, ConfigurationService configurationService, UserRepo userRepo,SystemDTO systemDTO) {
        this.userRepo = userRepo;
        this.ticketPool = ticketPool;
        this.configurationService = configurationService;
        this.systemDTO = systemDTO;
    }

    public void releaseTickets(int vendorID, Ticket ticket) {
        if (systemDTO.isRunning()) {
            Thread vendorThread = new Thread(() -> {
                int ticketReleaseRate = configurationService.getConfiguration().getTicketReleaseRate();
                int maxTicketCapacity = configurationService.getConfiguration().getMaxTicketCapacity();

                while (!Thread.currentThread().isInterrupted()) {
                    synchronized (ticketPool) {
                        try {
                            // Wait if the pool is full
                            while (ticketPool.getSynTicketList().size() + ticketReleaseRate > maxTicketCapacity) {
                                ticketPool.wait();
                            }

                            // Add tickets to the pool
                            for (int i = 0; i < ticketReleaseRate; i++) {
                                if (ticketPool.getSynTicketList().size() < maxTicketCapacity) {
                                    ticketPool.getSynTicketList().add(ticket);
                                } else {
                                    break;
                                }
                            }

                            System.out.println("Vendor " + vendorID + " released " + ticketReleaseRate + " tickets.");
                            Thread.currentThread().interrupt();
                            ticketPool.notifyAll(); // Notify waiting customers
                        } catch (InterruptedException e) {
                            System.out.println("Vendor " + vendorID + " interrupted.");
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            });

            vendorThread.start();
            vendorThreads.add(vendorThread);
        }else {
            System.out.println("System not running.");
        }
    }

    public void stopVendorThreads() {
        for (Thread thread : vendorThreads) {
            thread.interrupt();
        }
        vendorThreads.clear();
    }

    public UserDTO getVendor(int vendorID) {
        UserDTO userDTO = modelMapper.map(userRepo.findByUserID(vendorID), UserDTO.class);
        userDTO.setPassword("");
        return userDTO;
    }

    public void registerVendor(VendorDTO vendorDTO) {
        Vendor vendor = modelMapper.map(vendorDTO, Vendor.class);
        userRepo.save(vendor);
    }

    public void updateVendor(int vendorID, VendorDTO vendorDTO) {
        User user = userRepo.findByUserID(vendorID);
        if (user != null) {
            userRepo.save(modelMapper.map(vendorDTO, Vendor.class));
        }else {
            System.out.println("Vendor " + vendorID + " not found.");
        }

    }
}