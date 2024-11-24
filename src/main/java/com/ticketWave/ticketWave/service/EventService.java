package com.ticketWave.ticketWave.service;

import com.ticketWave.ticketWave.dto.EventDTO;
import com.ticketWave.ticketWave.model.Event;
import com.ticketWave.ticketWave.repo.EventRepo;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
public class EventService {
    private final EventRepo eventRepo;
    private final ModelMapper modelMapper;
    private static final Logger logger = LogManager.getLogger(CustomerService.class);

    public EventService(EventRepo eventRepo, ModelMapper modelMapper) {
        this.eventRepo = eventRepo;
        this.modelMapper = modelMapper;
    }

    public EventDTO getEvent() {
        Event event;
        try {
            event = eventRepo.findAll().getFirst();
            if (event != null) {
                logger.info("Get Event successful");
                return modelMapper.map(event, EventDTO.class);
            }
        } catch (Exception e) {
            logger.error("No events in the system");
        }
        return null;

    }
    public EventDTO updateEvent(EventDTO eventDTO) {
        Event event;
        try {
            event = eventRepo.findAll().getFirst();
            if (event != null) {
                Event updatedEvent = eventRepo.save(modelMapper.map(eventDTO, Event.class));
                logger.info("Event" + eventDTO.getEventId() + " updated Successfully");
                return modelMapper.map(updatedEvent, EventDTO.class);
            }
        } catch (Exception e) {
            logger.error("Event not found");

        }
        return null;
    }


    public EventDTO createEvent(EventDTO eventDTO) {
        try {
            eventRepo.deleteAll();
            Event event = modelMapper.map(eventDTO, Event.class);
            eventRepo.save(event);
            logger.info("Event created successfully");
            return eventDTO;
        }catch (Exception e) {
            logger.error("Error while creating event");
        }
        return null;
    }

    public EventDTO deleteEvent(EventDTO eventDTO){
        try {
            eventRepo.deleteAll();
            logger.info("Event deleted successfully");
            return eventDTO;
        }catch (Exception e) {
            logger.error("Error while deleting event");
        }
        return null;
    }
}
