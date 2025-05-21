package com.teste.tokio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teste.tokio.dto.AuthRequest;
import com.teste.tokio.dto.UserRequest;
import com.teste.tokio.model.Role;
import com.teste.tokio.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private String userToken;
    private String adminToken;

    @BeforeEach
    void setupTokens() throws Exception {
        UserRequest user = new UserRequest();
        user.setName("João Victor Cardoso de Souza");
        user.setEmail("joao@email.com");
        user.setPassword("123456");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        AuthRequest loginUser = new AuthRequest();
        loginUser.setEmail("joao@email.com");
        loginUser.setPassword("123456");

        String loginUserResponse = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUser)))
                .andReturn().getResponse().getContentAsString();

        userToken = "Bearer " + loginUserResponse;

        UserRequest admin = new UserRequest();
        admin.setName("Afonso Abreu");
        admin.setEmail("afonso@email.com");
        admin.setPassword("123456");
        admin.setRole(Role.ADMIN);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(admin)));

        AuthRequest loginAdmin = new AuthRequest();
        loginAdmin.setEmail("afonso@email.com");
        loginAdmin.setPassword("123456");

        String loginAdminResponse = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginAdmin)))
                .andReturn().getResponse().getContentAsString();

        adminToken = "Bearer " + loginAdminResponse;
    }

    @Test
    void shouldReturnPagedUsersSortedByCreatedAt() throws Exception {
        mockMvc.perform(get("/users")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sortBy", "createdAt")
                        .param("direction", "desc")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].createdAt").exists());
    }

    @Test
    void shouldUpdateOwnUserData() throws Exception {
        Long userId = userRepository.findByEmail("joao@email.com").orElseThrow().getId();

        UserRequest update = new UserRequest();
        update.setName("João Atualizado");
        update.setEmail("joao@email.com");
        update.setPassword("novaSenha");

        mockMvc.perform(put("/users/" + userId)
                        .header("Authorization", userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("João Atualizado"));
    }

    @Test
    void shouldDeleteUserAsAdmin() throws Exception {
        UserRequest novo = new UserRequest();
        novo.setName("Para Deletar");
        novo.setEmail("delete@email.com");
        novo.setPassword("123456");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novo)));

        Long id = userRepository.findByEmail("delete@email.com").orElseThrow().getId();

        mockMvc.perform(delete("/users/" + id)
                        .header("Authorization", adminToken))
                .andExpect(status().isNoContent());
    }
}
