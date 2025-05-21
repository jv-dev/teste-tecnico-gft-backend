package com.teste.tokio.service;

import com.teste.tokio.dto.UserRequest;
import com.teste.tokio.dto.UserResponse;
import com.teste.tokio.model.Role;
import com.teste.tokio.model.User;
import com.teste.tokio.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        UserRequest request = new UserRequest();
        request.setName("João Victor Cardoso de Souza");
        request.setEmail("joao@email.com");
        request.setPassword("123456");

        User savedUser = User.builder()
                .id(1L)
                .name("João Victor Cardoso de Souza")
                .email("joao@email.com")
                .password("encoded123")
                .role(Role.USER)
                .build();

        when(passwordEncoder.encode("123456")).thenReturn("encoded123");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = userService.create(request);

        assertNotNull(response);
        assertEquals("João Victor Cardoso de Souza", response.getName());
        assertEquals("joao@email.com", response.getEmail());
        assertEquals(Role.USER, response.getRole());

        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("123456");
    }

    @Test
    void shouldReturnUserWhenIdExists() {
        User user = User.builder()
                .id(1L)
                .name("João Victor Cardoso de Souza")
                .email("joao@email.com")
                .password("encoded")
                .role(Role.USER)
                .build();

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        UserResponse response = userService.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("João Victor Cardoso de Souza", response.getName());
        assertEquals(Role.USER, response.getRole());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(java.util.Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userService.findById(99L);
        });

        assertEquals("User not found", ex.getMessage());
        verify(userRepository, times(1)).findById(99L);
    }

    @Test
    void shouldReturnPagedUsersCorrectly() {
        User user1 = User.builder().id(1L).name("João Victor Cardoso de Souza").email("joao@email.com").role(Role.USER).build();
        User user2 = User.builder().id(2L).name("Afonso Abreu").email("afonso@email.com").role(Role.USER).build();

        Pageable pageable = PageRequest.of(0, 2);
        var page = new PageImpl<>(List.of(user1, user2), pageable, 2);

        when(userRepository.findAll(pageable)).thenReturn(page);

        var response = userService.findAllPaged(pageable);

        assertNotNull(response);
        assertEquals(2, response.getContent().size());
        assertEquals(0, response.getPage());
        assertEquals(2, response.getSize());
        assertEquals(2, response.getTotalElements());
        assertEquals(1, response.getTotalPages());

        verify(userRepository, times(1)).findAll(pageable);
    }
}
