package com.example.order_service.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderType;
import com.example.order_service.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // @PostMapping("/place")
    // public Map<String, Object> placeOrder(@RequestBody Map<String, Object> req) {
    //     String username = (String) req.get("username");
    //     String symbol = (String) req.get("symbol");
    //     String typeStr = (String) req.get("type");
    //     Integer quantity = ((Number) req.get("quantity")).intValue();
    //     Double price = ((Number) req.get("price")).doubleValue();

    //     OrderType orderType = OrderType.valueOf(typeStr.toUpperCase());

    //     Order order = orderService.placeOrder(username, symbol, orderType, quantity, price);

    //     Map<String, Object> response = new HashMap<>();
    //     response.put("orderId", order.getOrderId());
    //     response.put("status", order.getStatus());
    //     response.put("message", "Order placed successfully");
    //     return response;
    // }
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping("/place")
    public Map<String, Object> placeOrder(@RequestBody Map<String, Object> req) {
        String username = (String) req.get("username");
        String symbol = (String) req.get("symbol");
        String typeStr = (String) req.get("type");
        Integer quantity = ((Number) req.get("quantity")).intValue();
        Double price = ((Number) req.get("price")).doubleValue();

        OrderType orderType = OrderType.valueOf(typeStr.toUpperCase());

        // 1️⃣ 先存進 DB
        Order order = orderService.placeOrder(username, symbol, orderType, quantity, price);

        // 2️⃣ 再發送 Kafka 訊息
        Map<String, Object> kafkaMessage = new HashMap<>();
        kafkaMessage.put("orderId", order.getOrderId());
        kafkaMessage.put("username", username);
        kafkaMessage.put("symbol", symbol);
        kafkaMessage.put("type", orderType.toString());
        kafkaMessage.put("quantity", quantity);
        kafkaMessage.put("price", price);
        kafkaMessage.put("status", order.getStatus());
        kafkaMessage.put("timestamp", order.getTimestamp());

        kafkaTemplate.send("order-topic", kafkaMessage);

        // 3️⃣ 回傳 API 給前端
        Map<String, Object> response = new HashMap<>();
        response.put("orderId", order.getOrderId());
        response.put("status", order.getStatus());
        response.put("message", "Order placed successfully and sent to Kafka");
        return response;
    }

    @GetMapping("/history")
    public Map<String, Object> getHistory(@RequestParam String username) {
        List<Order> orders = orderService.getHistory(username);

        Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("orders", orders);
        return response;
    }
}
