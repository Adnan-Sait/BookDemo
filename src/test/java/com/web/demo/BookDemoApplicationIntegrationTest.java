package com.web.demo;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.demo.model.Book;
import com.web.demo.service.BookService;

@SpringBootTest
@ExtendWith({SpringExtension.class})
@AutoConfigureMockMvc
@TestPropertySource(
		  locations = "classpath:application-integrationtest.properties")
public class BookDemoApplicationIntegrationTest {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private ObjectMapper objMapper; 
	
	@MockBean
	private BookService bookService;
	
	@Test
	public void addBook() throws Exception {
		Book bookRequest = new Book();
		bookRequest.setTitle("new book");
		
		Book bookResponse = new Book();
		bookResponse.setTitle(bookRequest.getTitle());
		bookResponse.setId(1L);
		
		given(bookService.createBook(Mockito.any())).willReturn(bookResponse);
		
		mvc.perform(post("/api/book")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objMapper.writeValueAsString(bookRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id", is(not(nullValue())))
				);
		verify(bookService, VerificationModeFactory.times(1)).createBook(Mockito.any());
		reset(bookService);
	}
	
	@Test
	public void getAllBooks() throws Exception {
		Book prideAndPrejudice = new Book();
		prideAndPrejudice.setId(1L);
		prideAndPrejudice.setTitle("Pride and Prejudice");
		
		Book wutheringHeights = new Book();
		wutheringHeights.setId(2L);
		wutheringHeights.setTitle("Wuthering Heights");
		
		List<Book> allBooks = Arrays.asList(prideAndPrejudice, wutheringHeights);
		
		given(bookService.getAllBooks()).willReturn(allBooks);
		mvc.perform(get("/api/book"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data", is(not(nullValue()))));
		verify(bookService, VerificationModeFactory.times(1)).getAllBooks();
		reset(bookService);
	}
	
	@Test
	public void getBookById() throws Exception {
		Book prideAndPrejudice = new Book();
		prideAndPrejudice.setId(1L);
		prideAndPrejudice.setTitle("Pride and Prejudice");
		
		given(bookService.getBookById(1L)).willReturn(prideAndPrejudice);
		given(bookService.getBookById(2L)).willReturn(null);
		mvc.perform(get("/api/book/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id", is(1)));
		verify(bookService, VerificationModeFactory.times(1)).getBookById(1L);
		reset(bookService);
		
		mvc.perform(get("/api/book/2"))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void searchBook() throws Exception {
		Book prideAndPrejudice = new Book();
		prideAndPrejudice.setId(1L);
		prideAndPrejudice.setTitle("Pride and Prejudice");
		prideAndPrejudice.setAuthor("Jane Austen");
		
		Book senseAndSensibility = new Book();
		senseAndSensibility.setId(2L);
		senseAndSensibility.setTitle("Sense and Sensibility");
		senseAndSensibility.setAuthor("Jane Austen");
		
		List<Book> searchedBooks = Arrays.asList(prideAndPrejudice, senseAndSensibility);
		
		given(bookService.searchBooksByField(null, "Jane", null, null, null)).willReturn(searchedBooks);
		mvc.perform(get("/api/book/search?author=Jane"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data", is(not(nullValue()))))
				.andExpect(jsonPath("$.data[0].author", is(prideAndPrejudice.getAuthor())));
		verify(bookService, VerificationModeFactory.times(1)).searchBooksByField(null, "Jane", null, null, null);
		reset(bookService);
	}
}
