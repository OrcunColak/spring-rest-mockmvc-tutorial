package com.colak.springrestmockmvctutorial.controller.weblayeronly;

import com.colak.springrestmockmvctutorial.controller.BookController;
import com.colak.springrestmockmvctutorial.model.Book;
import com.colak.springrestmockmvctutorial.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Instantiate only the web layer rather than the whole context
 */
@WebMvcTest(BookController.class)
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;


    // Post example
    @Test
    void create() throws Exception {
        var resultBook = new Book(1L, "John Doe", "123", LocalDate.now());
        Mockito.when(bookService.create(Mockito.any())).thenReturn(resultBook);

        var sentBook = new Book(1L, "John Doe", "123", LocalDate.now());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/book")
                .content(objectMapper.writeValueAsString(sentBook))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getByIdFound() throws Exception {
        var resultBook = new Book(1L, "John Doe", "123", LocalDate.now());
        Optional<Book> mockBookOptional = Optional.of(resultBook);
        Mockito.when(bookService.getById(Mockito.anyLong())).thenReturn(mockBookOptional);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/book/{bookId}", 1L);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"id\":1,\"author\":\"John Doe\",\"isbn\":\"123\",\"publishedDate\":\"" + LocalDate.now() + "\"}"));
    }

    @Test
    void getByIdNotFound() throws Exception {
        Optional<Book> emptyOptional = Optional.empty();

        Mockito.when(bookService.getById(Mockito.anyLong())).thenReturn(emptyOptional);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/book/{bookId}", 1L);
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateBook() throws Exception {
        // Mocking the service's behavior
        Mockito.when(bookService.update(Mockito.any(Book.class))).thenReturn(true);

        // Creating a sample Book object to send in the request body
        var updatedBook = new Book(1L, "John Doe", "123", LocalDate.now());

        // Performing the PUT request with the updated book in the request body
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBook));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(true)); // Verify the response body is true
    }
}
