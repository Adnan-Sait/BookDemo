package com.web.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import com.web.demo.domain.BookEntity;
import com.web.demo.exception.ForeignKeyViolationException;
import com.web.demo.model.Book;
import com.web.demo.repository.BookRepository;
import com.web.demo.service.BookService;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestPropertySource(
		  locations = "classpath:application-integrationtest.properties")
public class BookServiceUnitTest {
	
	@Mock
	private BookRepository bookRepository;
	
	@Mock
	private EntityManager em;
	
	private BookService bookService;
	
	@BeforeEach
	void initUnitTest() {
		bookService = new BookService(em, bookRepository);
	}
	
	@Test
	void addBookSuccess() throws ForeignKeyViolationException {
		Book prideAndPrejudice = new Book();
		
		prideAndPrejudice.setTitle("Pride and Prejudice");
		prideAndPrejudice.setAuthor("Jane Austen");
		prideAndPrejudice.setIsbn("12");
		
		BookEntity prideAndPrejudiceRequestEntity = new BookEntity();
		
		prideAndPrejudiceRequestEntity.setTitle("Pride and Prejudice");
		prideAndPrejudiceRequestEntity.setAuthor("Jane Austen");
		
		BookEntity prideAndPrejudiceResponseEntity = new BookEntity();
		
		prideAndPrejudiceResponseEntity.setId(1L);
		prideAndPrejudiceResponseEntity.setTitle("Pride and Prejudice");
		prideAndPrejudiceResponseEntity.setAuthor("Jane Austen");
		
		given(bookRepository.saveAndFlush(Mockito.any())).willReturn(prideAndPrejudiceResponseEntity);
		
		Book savedBook = bookService.createBook(prideAndPrejudice);
		assertEquals(1L, savedBook.getId());
	}
	
	@Test
	void addBookError() throws ForeignKeyViolationException {
		
		Book senseAndSensibility = new Book();
		
		senseAndSensibility.setTitle("Sense and Sensibility");
		senseAndSensibility.setAuthor("Jane Austen");
		senseAndSensibility.setIsbn("12");
		
		given(bookRepository.saveAndFlush(Mockito.any())).willThrow(DataIntegrityViolationException.class);
		
		Assertions.assertThrows(ForeignKeyViolationException.class, () -> bookService.createBook(senseAndSensibility));		
	}
	
	@Test
	void getAllBooks() {
		BookEntity senseAndSensibility = new BookEntity();
		
		senseAndSensibility.setTitle("Sense and Sensibility");
		senseAndSensibility.setAuthor("Jane Austen");
		senseAndSensibility.setIsbn("12");
		
		BookEntity prideAndPrejudice = new BookEntity();
		
		prideAndPrejudice.setTitle("Pride and Prejudice");
		prideAndPrejudice.setAuthor("Jane Austen");
		prideAndPrejudice.setIsbn("12");
		
		List<BookEntity> books = Arrays.asList(prideAndPrejudice, senseAndSensibility);
		
		given(bookRepository.findAll()).willReturn(books);
		List<Book> returnedBooks = bookService.getAllBooks();
		
		assertEquals(returnedBooks.size(), 2);
	}
	
	@Test
	void getBookById() {
		BookEntity senseAndSensibility = new BookEntity();
		
		senseAndSensibility.setId(1l);
		senseAndSensibility.setTitle("Sense and Sensibility");
		senseAndSensibility.setAuthor("Jane Austen");
		senseAndSensibility.setIsbn("12");
		
		Optional<BookEntity> input = Optional.of(senseAndSensibility);
		
		given(bookRepository.findById(senseAndSensibility.getId())).willReturn(input);
		
		Book book = bookService.getBookById(senseAndSensibility.getId());
		
		assertEquals(book.getId(), senseAndSensibility.getId());
	}
	
	@Test
	void updateBook() {
		Book senseAndSensibility = new Book();
		
		senseAndSensibility.setId(1l);
		senseAndSensibility.setTitle("Sense and Sensibility");
		senseAndSensibility.setAuthor("Jane Austen");
		senseAndSensibility.setIsbn("12");
		
		Book updatedSenseAndSensibility = new Book();
		
		updatedSenseAndSensibility.setIsbn("23");
		
		BookEntity senseAndSensibilityEntity = new BookEntity();
		
		senseAndSensibilityEntity.setId(1l);
		senseAndSensibilityEntity.setTitle("Sense and Sensibility");
		senseAndSensibilityEntity.setAuthor("Jane Austen");
		senseAndSensibilityEntity.setIsbn("23");
		
		given(bookRepository.save(Mockito.any())).willReturn(senseAndSensibilityEntity);
		
		Book updatedBook = bookService.updateBook(senseAndSensibility, updatedSenseAndSensibility);
		
		assertEquals(updatedSenseAndSensibility.getIsbn(), updatedBook.getIsbn());
	}
}
