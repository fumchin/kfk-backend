// This is the main class for the Order Service application, responsible for bootstrapping the Spring Boot application.
// 此類別是訂單服務應用程式的主類別，負責啟動 Spring Boot 應用程式。

package com.example.order_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args); // 啟動應用程式
    }

}
