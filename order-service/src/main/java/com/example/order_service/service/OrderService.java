// 此服務類別包含管理訂單的商業邏輯。

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

    // // 下單並儲存到資料庫
    // public Order placeOrder(String username, String symbol, OrderType type, int quantity, double price) {
    //     // 這裡可以額外檢查用戶餘額或庫存股票數量
    //     // 簡化：直接儲存為一筆 "PENDING" 訂單
    //     Order order = Order.builder()
    //             .username(username)
    //             .symbol(symbol)
    //             .type(type)
    //             .quantity(quantity)
    //             .price(price)
    //             .status("PENDING") // 設定初始狀態為 PENDING
    //             .timestamp(LocalDateTime.now()) // 設定訂單建立時間
    //             .build();
    //     return orderRepository.save(order); // 儲存訂單到資料庫
    // }
    public Order createOrder(String username, Long userId, String symbol, int type, int quantity, double price) {
        Order order = Order.builder()
                .username(username)
                .userId(userId)
                .symbol(symbol)
                .type(type)
                .quantity(quantity)
                .price(price)
                .timestamp(LocalDateTime.now())
                .build();
        return orderRepository.save(order);
    }

    // 查詢使用者的歷史訂單
    public List<Order> getOrdres(Long userId) {
        return orderRepository.findByUserIdOrderByTimestampDesc(userId); // 按時間倒序排列
    }
}
