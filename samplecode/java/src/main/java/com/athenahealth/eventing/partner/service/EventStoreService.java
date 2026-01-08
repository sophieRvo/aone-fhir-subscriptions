package com.athenahealth.eventing.partner.service;

import com.athenahealth.eventing.partner.dto.ReceivedEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class EventStoreService {
    
    // Thread-safe list to store events
    private final List<ReceivedEvent> events = new CopyOnWriteArrayList<>();
    
    public void storeEvent(String rawPayload, String topic, String subscriptionId, Integer eventsInNotification) {
        ReceivedEvent event = ReceivedEvent.builder()
                .id(java.util.UUID.randomUUID().toString())
                .receivedAt(LocalDateTime.now())
                .rawPayload(rawPayload)
                .topic(topic)
                .subscriptionId(subscriptionId)
                .eventsInNotification(eventsInNotification)
                .build();
        events.add(event);
    }
    
    public List<ReceivedEvent> getAllEvents() {
        // Return a copy in reverse order (most recent first)
        List<ReceivedEvent> result = new ArrayList<>(events);
        Collections.reverse(result);
        return result;
    }
    
    public void clearEvents() {
        events.clear();
    }
    
    public int getEventCount() {
        return events.size();
    }
}
