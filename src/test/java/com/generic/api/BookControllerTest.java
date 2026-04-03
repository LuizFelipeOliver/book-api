package com.generic.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.generic.api.auth.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * BookControllerTest
 */
public class BookControllerTest extends AbstractIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;
    private String token;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        userRepository.deleteAll();
        bookRepository.deleteAll();

        String registerBody = """
                {
                  "name": "Test User",
                  "email": "test@email.com",
                  "password": "123456"
                }
                """;

        String response = mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerBody))
                .andReturn()
                .getResponse()
                .getContentAsString();

        token = new ObjectMapper().readTree(response).get("token").asText();
    }

    @Test
    void getBookAll_whenNoBooksExist_returnsEmptyList() throws Exception {
        mockMvc.perform(get("/book")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getBookAll_whenBooksExist_returnsAllBooks() throws Exception {
        bookRepository.save(new Book("Clean Code", "Robert Martin", true));
        bookRepository.save(new Book("The Pragmatic Programmer", "David Thomas", false));

        mockMvc.perform(get("/book")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Clean Code"))
                .andExpect(jsonPath("$[1].name").value("The Pragmatic Programmer"));
    }

    @Test
    void getBookAll_filterByName_returnsMatchingBooks() throws Exception {
        bookRepository.save(new Book("Clean Code", "Robert Martin", true));
        bookRepository.save(new Book("Clean Architecture", "Robert Martin", true));
        bookRepository.save(new Book("The Pragmatic Programmer", "David Thomas", false));

        mockMvc.perform(get("/book").param("name", "Clean")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getBookAll_filterByAuthor_returnsMatchingBooks() throws Exception {
        bookRepository.save(new Book("Clean Code", "Robert Martin", true));
        bookRepository.save(new Book("Clean Architecture", "Robert Martin", true));
        bookRepository.save(new Book("The Pragmatic Programmer", "David Thomas", false));

        mockMvc.perform(get("/book").param("author", "Robert")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getBookAll_filterByNameAndAuthor_returnsMatchingBooks() throws Exception {
        bookRepository.save(new Book("Clean Code", "Robert Martin", true));
        bookRepository.save(new Book("Clean Architecture", "Robert Martin", true));
        bookRepository.save(new Book("The Pragmatic Programmer", "David Thomas", false));

        mockMvc.perform(get("/book").param("name", "Clean").param("author", "Robert")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getBookAll_filterWithNoMatch_returnsEmptyList() throws Exception {
        bookRepository.save(new Book("Clean Code", "Robert Martin", true));

        mockMvc.perform(get("/book").param("name", "nonexistent")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getById_whenBookExists_returnsBook() throws Exception {
        Book saved = bookRepository.save(new Book("Clean Code", "Robert Martin", true));

        mockMvc.perform(get("/book/{id}", saved.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Clean Code"))
                .andExpect(jsonPath("$.author").value("Robert Martin"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void getById_whenBookDoesNotExist_returns404() throws Exception {
        mockMvc.perform(get("/book/{id}", 999L)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_withValidRequest_returnsCreatedBook() throws Exception {
        String body = """
                {
                  "name": "Clean Code",
                  "author": "Robert Martin",
                  "available": true
                }
                """;

        mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Clean Code"))
                .andExpect(jsonPath("$.author").value("Robert Martin"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void create_withAvailableNull_defaultsFalse() throws Exception {
        String body = """
                {
                  "name": "Clean Code",
                  "author": "Robert Martin"
                }
                """;

        mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false));
    }

    @Test
    void create_withBlankName_returns400() throws Exception {
        String body = """
                {
                  "name": "",
                  "author": "Robert Martin",
                  "available": true
                }
                """;

        mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_withBlankAuthor_returns400() throws Exception {
        String body = """
                {
                  "name": "Clean Code",
                  "author": "",
                  "available": true
                }
                """;

        mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_withNameTooShort_returns400() throws Exception {
        String body = """
                {
                  "name": "AB",
                  "author": "Robert Martin",
                  "available": true
                }
                """;

        mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_withAuthorTooShort_returns400() throws Exception {
        String body = """
                {
                  "name": "Clean Code",
                  "author": "RR",
                  "available": true
                }
                """;

        mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_whenBookExists_returnsUpdatedBook() throws Exception {
        Book saved = bookRepository.save(new Book("Old Name", "Old Author", false));

        String body = """
                {
                  "name": "New Name",
                  "author": "New Author",
                  "available": true
                }
                """;

        mockMvc.perform(put("/book/{id}", saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.author").value("New Author"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void update_whenBookDoesNotExist_returns404() throws Exception {
        String body = """
                {
                  "name": "New Name",
                  "author": "New Author",
                  "available": true
                }
                """;

        mockMvc.perform(put("/book/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_withInvalidRequest_returns400() throws Exception {
        Book saved = bookRepository.save(new Book("Old Name", "Old Author", false));

        String body = """
                {
                  "name": "",
                  "author": "New Author",
                  "available": true
                }
                """;

        mockMvc.perform(put("/book/{id}", saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_whenBookExists_returns204() throws Exception {
        Book saved = bookRepository.save(new Book("Clean Code", "Robert Martin", true));

        mockMvc.perform(delete("/book/{id}", saved.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_whenBookDoesNotExist_returns404() throws Exception {
        mockMvc.perform(delete("/book/{id}", 999L)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void anyRequest_withoutToken_returns401() throws Exception {
        mockMvc.perform(get("/book")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/book/1")).andExpect(status().isUnauthorized());
        mockMvc.perform(post("/book").contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isUnauthorized());
        mockMvc.perform(put("/book/1").contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/book/1")).andExpect(status().isUnauthorized());
    }
}
