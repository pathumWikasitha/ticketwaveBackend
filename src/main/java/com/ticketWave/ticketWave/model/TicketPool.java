package com.ticketWave.ticketWave.model;

import com.ticketWave.ticketWave.service.ConfigurationService;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TicketPool {
    private final ConfigurationService configurationService;
    private int remainingTickets;
    private final List<Ticket> ticketList;
    private static final Logger logger = Logger.getLogger(TicketPool.class);

    public TicketPool(ConfigurationService configurationService,int remainingTickets) {
        this.configurationService = configurationService;
        this.ticketList = Collections.synchronizedList(new LinkedList<>());
        this.remainingTickets = remainingTickets;
    }

    public synchronized void addTickets(int vendorID,Ticket ticket) throws InterruptedException {
        int maxTicketCapacity = configurationService.getConfiguration().getMaxTicketCapacity();
        int ticketReleaseRate = configurationService.getConfiguration().getTicketReleaseRate();
        if (remainingTickets <= 0) {
            logger.info("Vendor " + vendorID + ": No more tickets to release.");
            return;
        }

        int ticketsToRelease = Math.min(ticketReleaseRate, remainingTickets);

        while (ticketList.size() + ticketReleaseRate > maxTicketCapacity) {
            logger.info("Ticket Pool is full. Vendor " + vendorID + " is waiting.");
            wait();
        }

        for (int i = 0; i < ticketsToRelease; i++) {
            ticketList.add(ticket);
        }

        remainingTickets -= ticketsToRelease;
        logger.info("Vendor " + vendorID + " released " + ticketsToRelease + " tickets. Remaining tickets: " + remainingTickets);
        notifyAll();
    }
}
