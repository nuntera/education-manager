package com.mindera.mindswap.education_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Education Manager application.
 * This Spring Boot application manages students and courses in an educational context.
 * 
 * The application provides REST APIs for:
 * - Student management (CRUD operations)
 * - Course management (CRUD operations)
 * - Student enrollment in courses
 * 
 * Features include:
 * - PostgreSQL database integration
 * - OpenAPI documentation
 * - REST API endpoints
 * - Data validation
 * 
 * @author Mindera
 * @version 1.0.0
 */
@SpringBootApplication
public class EducationManagerApplication {

	/**
	 * Main method that starts the Spring Boot application.
	 * 
	 * @param args Command line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(EducationManagerApplication.class, args);
	}
}
