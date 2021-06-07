package com.web.demo.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.web.demo.domain.BookEntity;
import com.web.demo.exception.ForeignKeyViolationException;
import com.web.demo.model.Book;
import com.web.demo.repository.BookRepository;

@Service
@Transactional
public class BookService {

	private final EntityManager em;
	
	private final BookRepository bookRepository;
	
	public BookService(EntityManager em, BookRepository bookRepository) {
		this.em = em;
		this.bookRepository = bookRepository;
	}
	
	public Book createBook(Book book) throws ForeignKeyViolationException {
		try {
			BookEntity bookEntity = bookModelToEntityMapper(book);
			bookEntity = bookRepository.saveAndFlush(bookEntity);
			return bookEntityToModelMapper(bookEntity);
		} catch (DataIntegrityViolationException e) {
			throw new ForeignKeyViolationException("The ISBN value is not unique");
		}
	}
	
	public Book getBookById(Long id) {
		Optional<BookEntity> bookEntity = bookRepository.findById(id);
		if (bookEntity.isPresent()) {
			return bookEntityToModelMapper(bookEntity.get());
		}
		return null;
	}
	
	public List<Book> getAllBooks() {
		List<BookEntity> bookEntityList = bookRepository.findAll();
		List<Book> bookList = bookEntityList.stream().map(bookEntity -> 
			bookEntityToModelMapper(bookEntity)
		).collect(Collectors.toList());
		return bookList;
	}
	
	public List<Book> searchBooksByField(String title, String author, Instant dateOfPublication, String genre, String isbn) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<BookEntity> cq = cb.createQuery(BookEntity.class);
		
		Root<BookEntity> book = cq.from(BookEntity.class);
		List<Predicate> predicates = new ArrayList<>();
		if (title != null) {
			String titlePattern = "%"+title+"%";
			predicates.add(cb.like(book.get("title"), titlePattern));
		}
		if (author != null) {
			String authorPattern = "%"+author+"%"; 
			predicates.add(cb.like(book.get("author"), authorPattern));
		}
		if (dateOfPublication != null) {
			predicates.add(cb.equal(book.get("dateOfPublication"), dateOfPublication));
		}
		if (genre != null) {
			predicates.add(cb.equal(book.get("genre"), genre));
		}
		if (isbn != null) {
			predicates.add(cb.equal(book.get("isbn"), isbn));
		}
		cq.where(predicates.toArray(new Predicate[0]));
		
		List<BookEntity> bookEntityList = em.createQuery(cq).getResultList();
		List<Book> bookList = bookEntityList.stream().map(bookEntity -> 
			bookEntityToModelMapper(bookEntity)
		).collect(Collectors.toList());
		return bookList;
	}
	
	public Book updateBook(Book currentBook, Book updatedBookDetails) {
		currentBook.setTitle(updatedBookDetails.getTitle() != null ? updatedBookDetails.getTitle() : currentBook.getTitle());
		currentBook.setAuthor(updatedBookDetails.getAuthor() != null ? updatedBookDetails.getAuthor() : currentBook.getAuthor());
		currentBook.setDateOfPublishing(updatedBookDetails.getDateOfPublishing() != null? updatedBookDetails.getDateOfPublishing() : currentBook.getDateOfPublishing());
		currentBook.setGenre(updatedBookDetails.getGenre() != null ? updatedBookDetails.getGenre() : currentBook.getGenre());
		currentBook.setIsbn(updatedBookDetails.getIsbn() != null? updatedBookDetails.getIsbn() : currentBook.getIsbn());
		
		BookEntity updatedBook = bookModelToEntityMapper(currentBook);
		updatedBook = bookRepository.save(updatedBook);
		
		return bookEntityToModelMapper(updatedBook);
	}
	
	public void deleteBookById(Long id) throws IllegalArgumentException {
		bookRepository.deleteById(id);
	}
	
	private Book bookEntityToModelMapper(BookEntity bookEntity) {
		Book book = new Book();
		
		book.setId(bookEntity.getId());
		book.setTitle(bookEntity.getTitle());
		book.setAuthor(bookEntity.getAuthor());
		book.setDateOfPublishing(bookEntity.getDateOfPublishing());
		book.setGenre(bookEntity.getGenre());
		book.setIsbn(bookEntity.getIsbn());
		
		return book;
	}
	
	private BookEntity bookModelToEntityMapper(Book book) {
		BookEntity bookEntity = new BookEntity();
		bookEntity.setId(book.getId());
		bookEntity.setTitle(book.getTitle());
		bookEntity.setAuthor(book.getAuthor());
		bookEntity.setDateOfPublishing(book.getDateOfPublishing());
		bookEntity.setGenre(book.getGenre());
		bookEntity.setIsbn(book.getIsbn());
		
		return bookEntity;
	}
}
