package com.example.namesplitter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The NamesplitterApplication class is the entry point of the Spring Boot application.
 */
@SpringBootApplication
public class NamesplitterApplication {

    /**
     * The main method uses Spring Boot's SpringApplication.run() method to launch an application.
     * @param args The command line arguments
     */
	public static void main(String[] args) {
		SpringApplication.run(NamesplitterApplication.class, args);
	}

}