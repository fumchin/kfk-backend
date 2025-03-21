// 這個檔案是應用程式的主入口，負責啟動整個訂單服務。

// 這是應用程式的入口，負責啟動整個訂單服務。

package com.example.order_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderServiceApplication {

    /**
     * 主方法，啟動應用程式
     * @param args 命令列參數
     */
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args); // 啟動應用程式
    }

}
