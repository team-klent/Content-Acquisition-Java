package com.innodata.application;

/*This enable database interactions via JPA (Java Persistence API) and 
 * is used to tell where to find database-related components. 
 * */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.innodata.application.repository")
public class ApplicationService {
	
	public static void main(String[] args) {
		SpringApplication.run(ApplicationService.class, args);
	}

}
