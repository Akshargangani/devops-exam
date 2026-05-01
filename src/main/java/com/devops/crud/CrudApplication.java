package com.devops.crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Spring Boot CRUD Application.
 * This application demonstrates a complete REST API with layered architecture
 * including Controller, Service, Repository, and Entity layers.
 */
@SpringBootApplication
public class CrudApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrudApplication.class, args);
    }

}
