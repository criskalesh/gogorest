package com.gogorest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GogorestApplication {

	public static void main(String[] args) {
		SpringApplication.run(GogorestApplication.class, args);
	}
}
