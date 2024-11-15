package com.ticketWave.ticketWave.service;

import com.ticketWave.ticketWave.model.Ticket;
import com.ticketWave.ticketWave.model.TicketPool;

import java.util.List;

public class VendorService {
    private final TicketPool ticketPool;
    private final ConfigurationService configurationService;
    private List<Thread> vendorThreads;

    public VendorService(TicketPool ticketPool, ConfigurationService configurationService) {
        this.ticketPool = ticketPool;
        this.configurationService = configurationService;
    }

    public void releseTickets(int vendorID,Ticket ticket) {
        int ticketReleaseRate = configurationService.getConfiguration().getTicketReleaseRate();

        Thread thread =new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        ticketPool.addTickets(vendorID,ticket);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        vendorThreads.add(thread);

    }
}
