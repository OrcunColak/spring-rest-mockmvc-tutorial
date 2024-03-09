package com.colak.springrestmockmvctutorial.noserver;

import com.colak.springrestmockmvctutorial.model.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

/**
 * Instantiate the full Spring application context but without the server.
 */
@SpringBootTest
// Ask MockMvc to be injected by using the @AutoConfigureMockMvc
@AutoConfigureMockMvc
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreate() throws Exception {
        var postedBook = new Book(1L, "John Doe", "123", LocalDate.now());
        createBook(postedBook);
    }

    @Test
    void testGetById() throws Exception {
        // Create a book
        var book = new Book(2L, "John Doe", "123", LocalDate.now());
        var createdBook = createBook(book);

        // Read back the book
        MockHttpServletRequestBuilder requestBuilder1 = MockMvcRequestBuilders.get("/book/" + createdBook.getId());

        mockMvc.perform(requestBuilder1)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdBook.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("John Doe"));
    }

    @Test
    void testUpdate() throws Exception {
        // Create a book
        var book = new Book(3L, "John Doe", "123", LocalDate.now());
        var createdBook = createBook(book);

        createdBook.setAuthor("Jane Doe");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/book/" + createdBook.getId())
                .content(objectMapper.writeValueAsString(createdBook))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("Jane Doe"));
    }

    @Test
    void testDelete() throws Exception {
        // Create a book
        var book = new Book(4L, "John Doe", "123", LocalDate.now());
        var createdBook = createBook(book);

        mockMvc.perform(MockMvcRequestBuilders.delete("/" + createdBook.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/book/" + createdBook.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    private Book createBook(Book postedBook) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/book")
                .content(objectMapper.writeValueAsString(postedBook))
                .contentType(MediaType.APPLICATION_JSON);

        var mvcResponse = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        return objectMapper.readValue(mvcResponse.getResponse().getContentAsString(), Book.class);
    }
}
