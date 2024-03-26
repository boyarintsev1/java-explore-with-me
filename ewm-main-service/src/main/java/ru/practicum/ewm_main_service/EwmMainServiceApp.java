package ru.practicum.ewm_main_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages =  {"ru.practicum.ewm_stats_client", "ru.practicum.ewm_main_service"})
public class EwmMainServiceApp {
	public static void main(String[] args) {
		SpringApplication.run(EwmMainServiceApp.class, args);
	}
}
