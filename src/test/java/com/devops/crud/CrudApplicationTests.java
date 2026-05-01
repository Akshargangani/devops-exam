package com.devops.crud;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Main test class for the Spring Boot application.
 * Verifies that the application context loads successfully.
 */
@SpringBootTest
@ActiveProfiles("test")
class CrudApplicationTests {

    @Test
    void contextLoads() {
        // Test that the Spring context loads successfully
    }

}
