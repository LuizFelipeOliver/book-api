package com.generic.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.generic.api.auth.User;
import com.generic.api.auth.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * AuthControllerTest
 */
public class AuthControllerTest extends AbstractIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        userRepository.deleteAll();
    }

    @Test
    void register_withValidRequest_returns200WithToken() throws Exception {
        String body = """
                {
                  "name": "John Doe",
                  "email": "john@email.com",
                  "password": "123456"
                }
                """;

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void register_withDuplicateEmail_returns409() throws Exception {
        userRepository.save(new User("John Doe", "john@email.com", "encoded", "USER"));

        String body = """
                {
                  "name": "John Doe",
                  "email": "john@email.com",
                  "password": "123456"
                }
                """;

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isConflict());
    }

    @Test
    void register_withInvalidEmail_returns400() throws Exception {
        String body = """
                {
                  "name": "John Doe",
                  "email": "not-an-email",
                  "password": "123456"
                }
                """;

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_withValidCredentials_returns200WithToken() throws Exception {
        String registerBody = """
                {
                  "name": "John Doe",
                  "email": "john@email.com",
                  "password": "123456"
                }
                """;

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerBody));

        String loginBody = """
                {
                  "name": "John Doe",
                  "email": "john@email.com",
                  "password": "123456"
                }
                """;

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void login_whenEmailNotRegistered_returns404() throws Exception {
        String body = """
                {
                  "name": "John Doe",
                  "email": "notfound@email.com",
                  "password": "123456"
                }
                """;

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void login_withWrongPassword_returns401() throws Exception {
        String registerBody = """
                {
                  "name": "John Doe",
                  "email": "john@email.com",
                  "password": "123456"
                }
                """;

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerBody));

        String loginBody = """
                {
                  "name": "John Doe",
                  "email": "john@email.com",
                  "password": "wrong-password"
                }
                """;

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_withInvalidEmail_returns400() throws Exception {
        String body = """
                {
                  "name": "John Doe",
                  "email": "not-an-email",
                  "password": "123456"
                }
                """;

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest());
    }
}
