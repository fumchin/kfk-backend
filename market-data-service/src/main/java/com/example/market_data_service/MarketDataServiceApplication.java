package com.example.market_data_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * 應用程式主類別，負責啟動市場數據服務應用程式。
 */
@SpringBootApplication
public class MarketDataServiceApplication {

	/**
	 * 應用程式的進入點。
	 * 
	 * @param args 命令列參數
	 */
	public static void main(String[] args) {
		SpringApplication.run(MarketDataServiceApplication.class, args);
	}

}
