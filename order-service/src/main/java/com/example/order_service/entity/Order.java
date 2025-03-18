package com.example.order_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="orders") // 'order' 是關鍵字，有時候會跟 DB 衝突
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private String username;
    private String symbol;
    
    @Enumerated(EnumType.STRING)
    private OrderType type; // BUY or SELL

    private Integer quantity;
    private Double price;

    private String status;  // e.g. "PENDING", "COMPLETED"
    private LocalDateTime timestamp;
}
