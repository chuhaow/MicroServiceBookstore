package com.cwen.bookstore_webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class BookstoreWebappApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookstoreWebappApplication.class, args);
	}

}
