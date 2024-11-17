package com.ticketWave.ticketWave.service;

import com.ticketWave.ticketWave.dto.*;
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
    private final List<Thread> vendorThreads = new ArrayList<>();

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TicketService ticketService;


    public VendorService(TicketPoolDTO ticketPool, ConfigurationService configurationService, UserRepo userRepo) {
        this.userRepo = userRepo;
        this.ticketPool = ticketPool;
        this.configurationService = configurationService;
    }

    public void releaseTickets(int vendorID, int ticketCount, TicketDTO ticketDTO) {
            Thread vendorThread = new Thread(() -> {
                int ticketReleaseRate = configurationService.getConfiguration().getTicketReleaseRate();
                int maxTicketCapacity = configurationService.getConfiguration().getMaxTicketCapacity();

                while (!Thread.currentThread().isInterrupted()) {
                    synchronized (ticketPool) {
                        try {

                            while (true) {
                                if (ticketPool.getSynTicketList().size() + ticketCount > maxTicketCapacity) {
                                    wait(); // Wait if the pool is full
                                } else {
                                    for (int i = 0; i < ticketCount; i++) {
                                        TicketDTO ticket = ticketService.setVendor(vendorID,ticketDTO);
                                        ticketPool.getSynTicketList().add(ticket);
                                        Thread.sleep(ticketReleaseRate);
                                    }
                                    System.out.println("Vendor " + vendorID + " released " + ticketCount + " tickets");
                                    Thread.currentThread().interrupt();
                                    ticketPool.notifyAll();
                                    break;
                                }
                            }

                        } catch (InterruptedException e) {
                            System.out.println("Vendor " + vendorID + " interrupted.");
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            });

            vendorThread.start();
            vendorThreads.add(vendorThread);
    }

    public void stopVendorThreads() {
        for (Thread thread : vendorThreads) {
            thread.interrupt();
        }
        vendorThreads.clear();
    }

    public VendorDTO getVendor(int vendorID) {
        UserDTO userDTO = modelMapper.map(userRepo.findByUserID(vendorID), UserDTO.class);
        userDTO.setPassword("");
        return modelMapper.map(userDTO, VendorDTO.class);
    }

    public VendorDTO registerVendor(VendorDTO vendorDTO) {
        Vendor vendor = modelMapper.map(vendorDTO, Vendor.class);
        userRepo.save(vendor);
        return modelMapper.map(vendor, VendorDTO.class);
    }

    public VendorDTO updateVendor(int vendorID, VendorDTO vendorDTO) {
        User user = userRepo.findByUserID(vendorID);
        if (user != null) {
            userRepo.save(modelMapper.map(vendorDTO, Vendor.class));
            return modelMapper.map(vendorDTO, VendorDTO.class);
        } else {
            System.out.println("Vendor " + vendorID + " not found.");
            return null;
        }

    }
}