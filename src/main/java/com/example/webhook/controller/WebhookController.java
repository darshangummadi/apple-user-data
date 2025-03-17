package com.example.webhook.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @PostMapping
    public ResponseEntity<String> receiveWebhook(@RequestBody Map<String, Object> payload) {
        logger.info("🔹 Received Webhook Data: {}", payload);

        try {
            // Extract name (String)
            String name = extractStringValue(payload.get("Name"));

            // Safe extraction of other values
            int steps = extractIntValue(payload.get("Steps"));
            int activeEnergy = extractIntValue(payload.get("Active Energy"));
            int exerciseMinutes = extractExerciseMinutes(payload.get("Exercise Minutes")); // Special handling for
                                                                                           // minutes

            // Log extracted values
            logger.info("👤 User: {}", name);
            logger.debug("📌 Steps: {}", steps);
            logger.debug("🔥 Active Energy: {}", activeEnergy);
            logger.debug("💪 Exercise Minutes: {}", exerciseMinutes);

            return ResponseEntity.ok("✅ Webhook received successfully!");
        } catch (Exception e) {
            logger.error("❌ Error processing webhook payload: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payload format.");
        }
    }

    // Extract String value
    private String extractStringValue(Object value) {
        if (value instanceof String) {
            return (String) value;
        } else {
            logger.warn("Unexpected data type for Name field: {}", value);
            return "Unknown"; // Default name if missing
        }
    }

    // Handles Steps and Active Energy
    private int extractIntValue(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof String) {
            try {
                return Integer.parseInt(((String) value).replaceAll("[^0-9]", ""));
            } catch (NumberFormatException e) {
                logger.warn("Invalid number format for value: {}", value);
                return 0;
            }
        } else {
            logger.warn("Unexpected data type for value: {}", value);
            return 0;
        }
    }

    // Handles Exercise Minutes (splitting and summing)
    private int extractExerciseMinutes(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof String) {
            try {
                String[] parts = ((String) value).split("\\s+"); // Split by whitespace/newlines
                int sum = 0;
                for (String part : parts) {
                    sum += Integer.parseInt(part.trim());
                }
                return sum;
            } catch (NumberFormatException e) {
                logger.warn("Invalid number format for Exercise Minutes: {}", value);
                return 0;
            }
        } else {
            logger.warn("Unexpected type for Exercise Minutes: {}", value);
            return 0;
        }
    }
}
