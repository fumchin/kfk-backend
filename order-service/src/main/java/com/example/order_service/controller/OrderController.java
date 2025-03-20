// 此控制器負責處理與訂單相關的 HTTP 請求，包括下單與查詢歷史訂單。

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
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping("/place")
    public Map<String, Object> placeOrder(@RequestBody Map<String, Object> req) {
        // 解析請求參數
        String username = (String) req.get("username");
        String symbol = (String) req.get("symbol");
        String typeStr = (String) req.get("type");
        Integer quantity = ((Number) req.get("quantity")).intValue();
        Double price = ((Number) req.get("price")).doubleValue();

        // 將訂單類型轉換為枚舉
        OrderType orderType = OrderType.valueOf(typeStr.toUpperCase());

        // 1️⃣ 儲存訂單到資料庫
        Order order = orderService.placeOrder(username, symbol, orderType, quantity, price);

        // 2️⃣ 發送 Kafka 訊息
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
        response.put("message", "訂單已成功下單並發送至 Kafka");
        return response;
    }

    @GetMapping("/history")
    public Map<String, Object> getHistory(@RequestParam String username) {
        // 查詢使用者的歷史訂單
        List<Order> orders = orderService.getHistory(username);

        // 將結果封裝為回應
        Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("orders", orders);
        return response;
    }
}
