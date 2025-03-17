package com.example.transactionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.example.transactionservice", "com.example.webhook" })
public class WebhookAutomationApplication {
	public static void main(String[] args) {
		SpringApplication.run(WebhookAutomationApplication.class, args);
	}
}
