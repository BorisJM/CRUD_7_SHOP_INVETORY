package com.boris.shopinventorycrud.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendProductAddedNotification(String name) {

        messagingTemplate.convertAndSend(
                "/topic/products",
                "New product added: " + name
        );
    }
}