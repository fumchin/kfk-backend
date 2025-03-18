package com.example.order_service.consumer;

import com.example.order_service.entity.Order;
import com.example.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    @KafkaListener(topics = "order-topic", groupId = "order-consumer-group")
    public void consumeOrder(Map<String, Object> orderRequest) {
        log.info("Received from Kafka: {}", orderRequest);

        try {
            Long orderId = ((Number) orderRequest.get("orderId")).longValue();
            String username = (String) orderRequest.get("username");
            Double price = ((Number) orderRequest.get("price")).doubleValue();
            Integer quantity = ((Number) orderRequest.get("quantity")).intValue();

            // 1️⃣ 更新訂單狀態
            Optional<Order> optionalOrder = orderRepository.findById(orderId);
            if (optionalOrder.isPresent()) {
                Order order = optionalOrder.get();
                order.setStatus("APPROVED"); // 改為 APPROVED
                orderRepository.save(order);
                log.info("Order {} updated to APPROVED", orderId);
            }

            // 2️⃣ 呼叫 User Service 扣餘額
            double totalAmount = price * quantity;
            String userServiceUrl = "http://user-service:8081/api/user/update-balance";

            Map<String, Object> request = Map.of(
                    "username", username,
                    "amount", -totalAmount // 扣除金額
            );

            restTemplate.postForEntity(userServiceUrl, request, Void.class);
            log.info("Deducted {} from user {}", totalAmount, username);

        } catch (Exception e) {
            log.error("Error processing order: {}", e.getMessage());
        }
    }
}
