package com.devops.crud.controller;

import com.devops.crud.dto.UserDTO;
import com.devops.crud.exception.DuplicateEmailException;
import com.devops.crud.exception.GlobalExceptionHandler;
import com.devops.crud.exception.UserNotFoundException;
import com.devops.crud.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for UserController class.
 * Tests all REST endpoints using MockMvc.
 */
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        // Setup MockMvc with exception handler
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        userDTO = UserDTO.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .age(30)
                .address("123 Main St")
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/users - should create user")
    void createUser_Success() throws Exception {
        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("john.doe@example.com"));

        verify(userService, times(1)).createUser(any(UserDTO.class));
    }

    @Test
    @DisplayName("POST /api/v1/users - should return 400 for invalid input")
    void createUser_InvalidInput() throws Exception {
        UserDTO invalidDTO = UserDTO.builder()
                .firstName("") // Invalid: blank
                .lastName("Doe")
                .email("invalid-email") // Invalid: not an email
                .age(null) // Invalid: null
                .build();

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("GET /api/v1/users - should return all users")
    void getAllUsers_Success() throws Exception {
        List<UserDTO> users = Arrays.asList(userDTO,
                UserDTO.builder()
                        .id(2L)
                        .firstName("Jane")
                        .lastName("Smith")
                        .email("jane.smith@example.com")
                        .age(25)
                        .build());

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("GET /api/v1/users/{id} - should return user when found")
    void getUserById_Success() throws Exception {
        when(userService.getUserById(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.firstName").value("John"));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    @DisplayName("GET /api/v1/users/{id} - should return 404 when not found")
    void getUserById_NotFound() throws Exception {
        when(userService.getUserById(1L)).thenThrow(new UserNotFoundException(1L));

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    @DisplayName("PUT /api/v1/users/{id} - should update user")
    void updateUser_Success() throws Exception {
        when(userService.updateUser(eq(1L), any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User updated successfully"));

        verify(userService, times(1)).updateUser(eq(1L), any(UserDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/users/{id} - should delete user")
    void deleteUser_Success() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User deleted successfully"));

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    @DisplayName("GET /api/v1/users/search/firstname/{firstName} - should search users")
    void searchByFirstName_Success() throws Exception {
        List<UserDTO> users = Arrays.asList(userDTO);
        when(userService.searchByFirstName("John")).thenReturn(users);

        mockMvc.perform(get("/api/v1/users/search/firstname/John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].firstName").value("John"));

        verify(userService, times(1)).searchByFirstName("John");
    }

    @Test
    @DisplayName("GET /api/v1/users/health - should return UP")
    void healthCheck_Success() throws Exception {
        mockMvc.perform(get("/api/v1/users/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("UP"));
    }

}
