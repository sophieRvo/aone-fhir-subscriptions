package com.athenahealth.eventing.partner.api;

import com.athenahealth.eventing.partner.configuration.ApplicationConstants;
import com.athenahealth.eventing.partner.dto.ReceivedEvent;
import com.athenahealth.eventing.partner.exception.SignatureAuthenticationException;
import com.athenahealth.eventing.partner.service.EventStoreService;
import com.athenahealth.eventing.partner.service.PartnerExecutorService;
import com.athenahealth.eventing.partner.util.HmacUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class PartnerController {

    @Autowired
    private PartnerExecutorService partnerExecutorService;
    
    @Autowired
    private EventStoreService eventStoreService;

    @PostMapping(path = "process-event",
        consumes = ApplicationConstants.PAYLOAD_MIME_TYPE)
    public ResponseEntity<String> testController(@RequestBody(required = false) String request,
                               @RequestHeader(required = true, name = ApplicationConstants.X_HUB_SIGNATURE) String signatureHeader)  {
        if (request == null || request.isEmpty()) {
            log.error("Request body is null or empty");
            throw new SignatureAuthenticationException();
        }
        
        // Use the request body directly - Spring already parsed it as UTF-8 String
        String expectedSignature = HmacUtil.calculateHmac(request, ApplicationConstants.HMAC_LOCAL_TEST_SECRET);
        String signatureMethod = signatureHeader.substring(0, signatureHeader.indexOf("="));
        String actualSignature = signatureHeader.substring(signatureHeader.indexOf("=")+1);

        log.info("Request body length: {}", request.length());
        log.info("Request body ends with: {}", request.length() > 10 ? "..." + request.substring(request.length() - 10) : request);
        log.info("Expected signature: {}", expectedSignature);
        log.info("Actual signature: {}", actualSignature);

        if (!signatureMethod.equals("sha256")) {
            log.error("Unexpected signature method: {}", signatureMethod);
            throw new SignatureAuthenticationException();
        } else if(!HmacUtil.compareHmac(actualSignature, expectedSignature, ApplicationConstants.HMAC_LOCAL_TEST_SECRET)) {
            log.error("Signature Authentication failed. Expected: {}, Actual: {}", expectedSignature, actualSignature);
            throw new SignatureAuthenticationException();
        } else {
            log.info("Signature Authentication success.");
            log.info("Message Received: " + request);
            
            // Return 200 OK immediately after signature validation succeeds
            // Process the payload asynchronously to avoid blocking the webhook response
            try {
                partnerExecutorService.inflatePayload(request);
            } catch (Exception e) {
                // Log the error but don't fail the webhook - the event was received successfully
                log.error("Error processing payload (but webhook received successfully): ", e);
            }
            
            return ResponseEntity.ok().build();
            /*
             * Note: in this example code we are inflating the payload here within the webhook for simplicity.
             * For production use at scale, we recommend that you decouple acknowledgement of events from the
             * inflation and processing step.  Best practice is to keep your webhook as lightweight as possible,
             * ideally just persisting the event payload to a durable queue to be processed asynchronously.  See
             * the README documentation for more details on this topic.
             */
        }

    }
    
    @GetMapping(path = "events")
    public ResponseEntity<List<ReceivedEvent>> getAllEvents() {
        List<ReceivedEvent> events = eventStoreService.getAllEvents();
        return ResponseEntity.ok(events);
    }
    
    @GetMapping(path = "events/count")
    public ResponseEntity<Integer> getEventCount() {
        return ResponseEntity.ok(eventStoreService.getEventCount());
    }
    
    @DeleteMapping(path = "events")
    public ResponseEntity<String> clearEvents() {
        eventStoreService.clearEvents();
        return ResponseEntity.ok("Events cleared");
    }

}
