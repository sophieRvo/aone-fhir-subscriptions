package com.athenahealth.eventing.partner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceivedEvent {
    private String id;
    private LocalDateTime receivedAt;
    private String rawPayload;
    private String topic;
    private String subscriptionId;
    private Integer eventsInNotification;
}
