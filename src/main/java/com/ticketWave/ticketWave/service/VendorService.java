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
            int totalTickets = configurationService.getConfiguration().getTotalTickets();

            while (!Thread.currentThread().isInterrupted()) {
                synchronized (ticketPool) {
                    try {
                        while (true) {
                            if (ticketCount <= maxTicketCapacity) {
                                if (totalTickets - ticketCount >= 0) {
                                    if (ticketPool.getSynTicketList().size() + ticketCount > maxTicketCapacity) {
                                        wait(); // Wait if the pool is full
                                    } else {
                                        for (int i = 0; i < ticketCount; i++) {
                                            TicketDTO ticket = ticketService.setVendor(vendorID, ticketDTO);
                                            ticketPool.getSynTicketList().add(ticket);
                                            Thread.sleep(ticketReleaseRate);
                                        }
                                        System.out.println("Vendor " + vendorID + " released " + ticketCount + " tickets");
                                        vendorThreads.remove(Thread.currentThread());
                                        Thread.currentThread().interrupt();
                                        ticketPool.notifyAll();
                                        break;
                                    }
                                } else {
                                    System.out.println("No more tickets available");
                                    Thread.currentThread().interrupt();
                                    break;
                                }
                            } else {
                                System.out.println("Trying to release tickets more than ticket pool capacity");
                                Thread.currentThread().interrupt();
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
        User user;
        try {
            user = userRepo.findUser(vendorID, "VENDOR");
            if (user != null) {
                VendorDTO vendorDTO = modelMapper.map(user, VendorDTO.class);
                vendorDTO.setPassword("");
                return vendorDTO;
            }
        } catch (Exception e) {
            System.out.println("Vendor " + vendorID + " not found.");
        }
        return null;
    }

    public VendorDTO registerVendor(VendorDTO vendorDTO) {
        vendorDTO.setRole("VENDOR");
        Vendor vendor = modelMapper.map(vendorDTO, Vendor.class);
        userRepo.save(vendor);
        return modelMapper.map(vendor, VendorDTO.class);
    }

    public VendorDTO updateVendor(VendorDTO vendorDTO) {
        User user;
        try {
            user = userRepo.findUser(Math.toIntExact(vendorDTO.getId()), "VENDOR");
            if (user != null) {
                userRepo.save(modelMapper.map(vendorDTO, Vendor.class));
                return modelMapper.map(vendorDTO, VendorDTO.class);
            }
        } catch (Exception e) {
            System.out.println("Vendor " + vendorDTO.getId() + " not found.");
        }
        return null;

    }
}