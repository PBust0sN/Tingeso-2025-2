package com.example.ms_users_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MsUsersServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsUsersServiceApplication.class, args);
	}

}
