package com.ticketWave.ticketWave.service;

import com.ticketWave.ticketWave.dto.*;
import com.ticketWave.ticketWave.model.User;
import com.ticketWave.ticketWave.model.Vendor;
import com.ticketWave.ticketWave.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.apache.log4j.Logger;
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
    private static final Logger logger = Logger.getLogger(VendorService.class);

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

    public void releaseTickets(int vendorID, int ticketCount, EventDTO eventDTO) {

        Thread vendorThread = new Thread(() -> {
            int ticketReleaseRate = configurationService.getConfiguration().getTicketReleaseRate();
            int maxTicketCapacity = configurationService.getConfiguration().getMaxTicketCapacity();
            int totalTickets = configurationService.getConfiguration().getTotalTickets();

            while (!Thread.currentThread().isInterrupted()) {
                synchronized (ticketPool) {
                    try {
                        while (true) {
                            if (ticketCount <= maxTicketCapacity) {
                                if (totalTickets - ticketCount >= 0) {
                                    if (ticketPool.getSynTicketList().size() + ticketCount > maxTicketCapacity) {
                                        logger.info("Vendor waiting pool becomes available");
                                        wait(); // Wait if the pool is full
                                    } else {
                                        for (int i = 0; i < ticketCount; i++) {
                                            TicketDTO ticket = ticketService.setVendor(vendorID, eventDTO);
                                            ticketPool.getSynTicketList().add(ticket);
                                            Thread.sleep(ticketReleaseRate);
                                        }
                                        logger.info("Vendor " + vendorID + " released " + ticketCount + " tickets");
                                        vendorThreads.remove(Thread.currentThread());
                                        Thread.currentThread().interrupt();
                                        ticketPool.notifyAll();
                                        break;
                                    }
                                } else {
                                    logger.error("No more tickets available to release");
                                    Thread.currentThread().interrupt();
                                    break;
                                }
                            } else {
                                logger.error("Trying to release tickets more than ticket pool capacity");
                                Thread.currentThread().interrupt();
                                break;
                            }

                        }

                    } catch (InterruptedException e) {
                        logger.error("Vendor " + vendorID + " interrupted.");
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
        logger.info("Vendor threads stopped");
    }

    public VendorDTO getVendor(int vendorID) {
        User user;
        try {
            user = userRepo.findUser(vendorID, "VENDOR");
            if (user != null) {
                VendorDTO vendorDTO = modelMapper.map(user, VendorDTO.class);
                vendorDTO.setPassword("");
                logger.info("Vendor" + vendorID + " found");
                return vendorDTO;
            }
        } catch (Exception e) {
            logger.error("Vendor " + vendorID + " not found.");
        }
        return null;
    }

    public VendorDTO registerVendor(VendorDTO vendorDTO) {
        vendorDTO.setRole("VENDOR");
        Vendor vendor = modelMapper.map(vendorDTO, Vendor.class);
        userRepo.save(vendor);
        logger.info("Vendor " + vendorDTO.getUsername() + " registered successfully");
        return modelMapper.map(vendor, VendorDTO.class);
    }

    public VendorDTO loginVendor(VendorDTO vendorDTO) {
        try {
            User user = userRepo.loginUser(vendorDTO.getEmail(), vendorDTO.getPassword(), vendorDTO.getRole());
            if (user != null) {
                logger.info("Vendor" + user.getId() + " logged in successfully");
                return modelMapper.map(user, VendorDTO.class);
            }else {
                logger.error("Invalid username or password");
            }
        } catch (Exception e) {
            logger.error("Login error"+e.getMessage());
        }
        return null;
    }

    public VendorDTO updateVendor(VendorDTO vendorDTO) {
        User user;
        try {
            user = userRepo.findUser(Math.toIntExact(vendorDTO.getId()), "VENDOR");
            if (user != null) {
                userRepo.save(modelMapper.map(vendorDTO, Vendor.class));
                logger.info("Vendor" + vendorDTO.getId() + " updated successfully");
                return modelMapper.map(vendorDTO, VendorDTO.class);
            }
        } catch (Exception e) {
            logger.error("Vendor " + vendorDTO.getId() + " not found.");
        }
        return null;

    }
}