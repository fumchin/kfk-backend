package com.example.market_data_service.service;

import com.example.market_data_service.entity.MarketData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;

@Service
public class MarketDataProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MarketDataProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startSending() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            MarketData data = new MarketData("AAPL", 100 + Math.random() * 10, System.currentTimeMillis());
            try {
                String json = objectMapper.writeValueAsString(data);
                kafkaTemplate.send("market-data", json);
                System.out.println("Produced: " + json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
}