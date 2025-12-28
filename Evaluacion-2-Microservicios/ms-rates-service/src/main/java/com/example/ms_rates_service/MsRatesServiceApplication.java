package com.example.ms_rates_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class MsRatesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsRatesServiceApplication.class, args);
	}

}
