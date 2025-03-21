// 此介面提供與資料庫互動的方法，用於執行訂單的 CRUD 操作。

package com.example.order_service.repository;

import com.example.order_service.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // 根據使用者名稱查詢訂單，並按時間倒序排列
    List<Order> findByUsernameOrderByTimestampDesc(String username);
    List<Order> findByUserIdOrderByTimestampDesc(Long userId);
}
