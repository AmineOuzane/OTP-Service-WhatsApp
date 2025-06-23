package org.sid.otpwhatsapp.web;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/webhook")
@AllArgsConstructor
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @PostMapping()
    public ResponseEntity<?> handleWebhook(@RequestBody Map<String, Object> payload) {
        logger.info("Webhook received!");
        logger.debug("Full payload: {}", payload);

        Object entryObj = payload.get("entry");
        if (entryObj instanceof List) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> entries = (List<Map<String, Object>>) entryObj;
            for (Map<String, Object> entry : entries) {

                Object changesObj = entry.get("changes");
                if (changesObj instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> changes = (List<Map<String, Object>>) changesObj;
                    for (Map<String, Object> change : changes) {

                        Object valueObj = change.get("value");
                        if (valueObj instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> value = (Map<String, Object>) valueObj;

                            Object messagesObj = value.get("messages");
                            if (messagesObj instanceof List) {
                                @SuppressWarnings("unchecked")
                                List<Map<String, Object>> messages = (List<Map<String, Object>>) messagesObj;
                                for (Map<String, Object> message : messages) {

                                    String phoneNumber = (String) message.get("from");
                                    if (phoneNumber != null) {
                                        phoneNumber = phoneNumber.replaceAll("[^0-9+]", "");
                                        if (!phoneNumber.startsWith("+")) {
                                            phoneNumber = "+" + phoneNumber;
                                        }
                                    }
                                    String messageType = (String) message.get("type");
                                    logger.debug("Message type: {}", messageType);

                                    }
                                }
                            }
                        }
                    }
                }
            }
        return null;
    }

}

