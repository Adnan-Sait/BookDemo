package com.web.demo.web.rest;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.demo.model.Book;
import com.web.demo.service.BookService;
import com.web.demo.web.rest.model.ResponseJson;

@Controller
@Validated
@RequestMapping("/api/book")
public class BookController {

	@Autowired
	private BookService bookService;

	@PostMapping
	public ResponseEntity<ResponseJson<Book>> addBook(@RequestBody @NonNull Book book) {
		if (book.getId() != null) {
			return new ResponseEntity<>(new ResponseJson<>(null, "The ID should not be sent."), HttpStatus.BAD_REQUEST);
		}
		if (book.getTitle() == null) {
			return new ResponseEntity<>(new ResponseJson<>(null, "The book title should be provided"), HttpStatus.BAD_REQUEST);
		}
		try {
			Book createdBook = bookService.createBook(book);
			return new ResponseEntity<>(new ResponseJson<>(createdBook, "success"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseJson<>(null, "The ISBN value is not unique"), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping
	public ResponseEntity<ResponseJson<List<Book>>> getAllBooks() {
		List<Book> books = bookService.getAllBooks();
		return new ResponseEntity<>(new ResponseJson<>(books, "success"), HttpStatus.OK);
	}

	@GetMapping("/{bookId}")
	public ResponseEntity<ResponseJson<Book>> getBookById(@PathVariable("bookId") Long bookId) {
		Book book = bookService.getBookById(bookId);
		if (book == null) {
			return new ResponseEntity<>(new ResponseJson<>(null, "No book with ID: " + bookId), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(new ResponseJson<>(book, "success"), HttpStatus.OK);
	}

	@GetMapping("/search")
	public ResponseEntity<ResponseJson<List<Book>>> searchBooks(@RequestParam(name = "title", required = false) String title,
			@RequestParam(name = "author", required = false) String author,
			@RequestParam(name = "dateOfPublication", required = false) Instant dateOfPublication,
			@RequestParam(name = "genre", required = false) String genre,
			@RequestParam(name = "isbn", required = false) String isbn) {
		List<Book> books = bookService.searchBooksByField(title, author, dateOfPublication, genre, isbn);
		return new ResponseEntity<>(new ResponseJson<>(books, "success"), HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<ResponseJson<Book>> updateBook(@RequestBody Book book) {
		if (book.getId() == null) {
			return new ResponseEntity<>(new ResponseJson<>(null, "The ID should be provided while updating a book"),
					HttpStatus.BAD_REQUEST);
		}

		Book currentBook = bookService.getBookById(book.getId());
		if (currentBook == null) {
			return new ResponseEntity<>(new ResponseJson<>(null, "No book with ID: " + book.getId()), HttpStatus.BAD_REQUEST);
		}
		Book updatedBook = bookService.updateBook(currentBook, book);
		return new ResponseEntity<>(new ResponseJson<>(updatedBook, "success"), HttpStatus.OK);
	}

	@DeleteMapping("/{bookId}")
	public ResponseEntity<ResponseJson<Book>> deleteBookById(@PathVariable("bookId") Long bookId) {
		try {
			bookService.deleteBookById(bookId);
			return new ResponseEntity<>(new ResponseJson<>(null, "Deleted book with ID: " + bookId), HttpStatus.OK);
		} catch (EmptyResultDataAccessException e) {
			return new ResponseEntity<>(new ResponseJson<>(null, "No book with ID: " + bookId), HttpStatus.BAD_REQUEST);
		}
	}
}
