package com.redis.redispubsub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class RedisPubsubApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisPubsubApplication.class, args);
	}

}
