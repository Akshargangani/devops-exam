package com.devops.crud.service;

import com.devops.crud.dto.UserDTO;
import com.devops.crud.entity.User;
import com.devops.crud.exception.DuplicateEmailException;
import com.devops.crud.exception.UserNotFoundException;
import com.devops.crud.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserService class.
 * Uses Mockito to mock the UserRepository dependency.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .age(30)
                .address("123 Main St")
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
    @DisplayName("Create user - should save and return user")
    void createUser_Success() {
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.createUser(userDTO);

        assertNotNull(result);
        assertEquals(userDTO.getEmail(), result.getEmail());
        assertEquals(userDTO.getFirstName(), result.getFirstName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Create user - should throw exception when email exists")
    void createUser_DuplicateEmail() {
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> userService.createUser(userDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Get user by ID - should return user when found")
    void getUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Get user by ID - should throw exception when not found")
    void getUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Get all users - should return list of users")
    void getAllUsers_Success() {
        User user2 = User.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .age(25)
                .build();

        when(userRepository.findAll()).thenReturn(Arrays.asList(user, user2));

        List<UserDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Update user - should update and return user")
    void updateUser_Success() {
        UserDTO updateDTO = UserDTO.builder()
                .id(1L)
                .firstName("Johnny")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("9876543210")
                .age(31)
                .address("456 Oak St")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.updateUser(1L, updateDTO);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Update user - should throw exception when user not found")
    void updateUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, userDTO));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Delete user - should delete user when found")
    void deleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Delete user - should throw exception when not found")
    void deleteUser_NotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
        verify(userRepository, never()).deleteById(1L);
    }

    @Test
    @DisplayName("Search by first name - should return matching users")
    void searchByFirstName_Success() {
        when(userRepository.findByFirstNameContainingIgnoreCase("John"))
                .thenReturn(Arrays.asList(user));

        List<UserDTO> result = userService.searchByFirstName("John");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(userRepository, times(1)).findByFirstNameContainingIgnoreCase("John");
    }

    @Test
    @DisplayName("Exists by ID - should return true when user exists")
    void existsById_True() {
        when(userRepository.existsById(1L)).thenReturn(true);

        boolean result = userService.existsById(1L);

        assertTrue(result);
        verify(userRepository, times(1)).existsById(1L);
    }

    @Test
    @DisplayName("Exists by ID - should return false when user does not exist")
    void existsById_False() {
        when(userRepository.existsById(1L)).thenReturn(false);

        boolean result = userService.existsById(1L);

        assertFalse(result);
        verify(userRepository, times(1)).existsById(1L);
    }

}
