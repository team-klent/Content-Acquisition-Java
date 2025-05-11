package com.innodata.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class ApplicationService {
	
	public static void main(String[] args) {
		SpringApplication.run(ApplicationService.class, args);
	}

}
