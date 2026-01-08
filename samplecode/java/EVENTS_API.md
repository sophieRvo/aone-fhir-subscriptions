# Events API - View Received Notification Events

This document describes the endpoints available to view and manage received notification events.

## Overview

The application now stores all received webhook events in memory. You can view these events using the REST endpoints described below.

**Note:** Events are stored in memory and will be lost when the application restarts. For production use, consider persisting to a database.

## Endpoints

### 1. Get All Events

Retrieve all received notification events (most recent first).

**Endpoint:** `GET /events`

**Example:**
```bash
curl http://localhost:8080/events
```

**Response:**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "receivedAt": "2026-01-08T13:18:20",
    "rawPayload": "{...full JSON payload...}",
    "topic": "https://api.platform.athenahealth.com/fhir/r4/SubscriptionTopic/Appointment.schedule",
    "subscriptionId": "62865e96-3fe4-3f6f-b476-b8eb87b90892",
    "eventsInNotification": 1
  },
  ...
]
```

### 2. Get Event Count

Get the total number of events received.

**Endpoint:** `GET /events/count`

**Example:**
```bash
curl http://localhost:8080/events/count
```

**Response:**
```json
5
```

### 3. Clear All Events

Delete all stored events from memory.

**Endpoint:** `DELETE /events`

**Example:**
```bash
curl -X DELETE http://localhost:8080/events
```

**Response:**
```
Events cleared
```

## Using Swagger UI

You can also test these endpoints using Swagger UI:

1. Navigate to: http://localhost:8080/swagger-ui/index.html
2. Look for the `partner-controller` section
3. You'll see the new endpoints:
   - `GET /events` - Get all events
   - `GET /events/count` - Get event count
   - `DELETE /events` - Clear events

## Event Information

Each stored event contains:

- **id**: Unique identifier for the stored event
- **receivedAt**: Timestamp when the event was received
- **rawPayload**: Complete JSON payload as received from Athena
- **topic**: The subscription topic (e.g., `Appointment.schedule`, `Patient.update`)
- **subscriptionId**: The subscription ID this event belongs to
- **eventsInNotification**: Number of events in this notification bundle

## Example Workflow

1. **Send a test notification:**
   ```bash
   ./send-appointment-notification.sh
   ```

2. **View all received events:**
   ```bash
   curl http://localhost:8080/events | jq
   ```

3. **Check how many events you've received:**
   ```bash
   curl http://localhost:8080/events/count
   ```

4. **Clear events when done testing:**
   ```bash
   curl -X DELETE http://localhost:8080/events
   ```

## Notes

- Events are stored in memory only (lost on restart)
- Events are stored in the order received, but returned in reverse order (newest first)
- The `rawPayload` contains the complete FHIR Bundle as received
- For production, consider persisting to a database or message queue
