package com.teste.tokio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teste.tokio.dto.AddressRequest;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private String userToken;
    private String adminToken;
    private Long userId;

    @BeforeEach
    void setup() throws Exception {
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

        String userLoginResponse = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUser)))
                .andReturn().getResponse().getContentAsString();

        userToken = "Bearer " + userLoginResponse;
        userId = userRepository.findByEmail("joao@email.com").orElseThrow().getId();

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

        String adminLoginResponse = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginAdmin)))
                .andReturn().getResponse().getContentAsString();

        adminToken = "Bearer " + adminLoginResponse;

        AddressRequest address = new AddressRequest();
        address.setStreet("Rua A");
        address.setNumber("123");
        address.setComplement("Apto 1");
        address.setDistrict("Centro");
        address.setCity("São Paulo");
        address.setState("SP");
        address.setZipCode("01000-000");

        mockMvc.perform(post("/addresses")
                .header("Authorization", userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(address)));
    }

    @Test
    void shouldReturnPagedAddressesSortedByCreatedAt() throws Exception {
        mockMvc.perform(get("/addresses/user/" + userId)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "createdAt")
                        .param("direction", "desc")
                        .header("Authorization", userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].createdAt").exists());
    }

    @Test
    void adminShouldAccessOtherUserAddresses() throws Exception {
        mockMvc.perform(get("/addresses/user/" + userId)
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void userShouldNotAccessOtherUserAddresses() throws Exception {
        UserRequest stranger = new UserRequest();
        stranger.setName("Curioso");
        stranger.setEmail("curioso@email.com");
        stranger.setPassword("123456");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stranger)));

        AuthRequest strangerLogin = new AuthRequest();
        strangerLogin.setEmail("curioso@email.com");
        strangerLogin.setPassword("123456");

        String strangerToken = "Bearer " + mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(strangerLogin)))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(get("/addresses/user/" + userId)
                        .header("Authorization", strangerToken))
                .andExpect(status().isForbidden());
    }
}
