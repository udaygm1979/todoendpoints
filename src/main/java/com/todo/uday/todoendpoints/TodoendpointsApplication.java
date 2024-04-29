package com.todo.uday.todoendpoints;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import lombok.extern.slf4j.Slf4j;

/**
 * Application starting point
 */
@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class TodoendpointsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoendpointsApplication.class, args);
		log.info("--------------TodoendpointsApplication is up and running-----------");
	}
}
