package com.example.order_service.service;

import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderType;
import com.example.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public Order placeOrder(String username, String symbol, OrderType type, int quantity, double price) {
        // 這裡可以額外檢查用戶餘額或庫存股票數量(若要實做真實檢查，需呼叫UserService)
        // 目前簡化：直接儲存為一筆 "PENDING" or "COMPLETED" 訂單
        Order order = Order.builder()
                .username(username)
                .symbol(symbol)
                .type(type)
                .quantity(quantity)
                .price(price)
                .status("PENDING")
                .timestamp(LocalDateTime.now())
                .build();
        return orderRepository.save(order);
    }

    public List<Order> getHistory(String username) {
        return orderRepository.findByUsernameOrderByTimestampDesc(username);
    }
}
