package com.post.application.Post.Application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class PostApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(PostApplication.class, args);
	}



	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}







}
