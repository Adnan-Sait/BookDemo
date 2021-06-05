package com.web.demo.domain;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "book")
public class BookEntity {
	
	@Id
	@GenericGenerator(strategy = "increment", name = "book_id_generator")
	@GeneratedValue(generator = "book_id_generator")
	private Long id;
	
	@Column(name = "title", nullable = false)
	private String title;
	
	@Column(name = "date_of_publishing", nullable = false)
	private Instant dateOfPublishing;
	
	@Column(name = "author", nullable = false)
	private String author;
	
	@Column(name = "genre", nullable = false)
	private String genre;
	
	@Column(name = "isbn", nullable = false, unique = true)
	private String isbn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Instant getDateOfPublishing() {
		return dateOfPublishing;
	}

	public void setDateOfPublishing(Instant dateOfPublishing) {
		this.dateOfPublishing = dateOfPublishing;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
}
