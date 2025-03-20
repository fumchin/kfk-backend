package com.example.market_data_service.service;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MarketDataConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    public MarketDataConsumer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(topics = "market-data", groupId = "market-data-group")
    public void consume(String message) {
        System.out.println("Consumed: " + message);
        messagingTemplate.convertAndSend("/topic/market-data", message);
    }
}

