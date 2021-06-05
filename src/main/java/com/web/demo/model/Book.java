package com.web.demo.model;

import java.time.Instant;

public class Book {

	private Long id;
	private String title;
	private String author;
	private Instant dateOfPublishing;
	private String genre;
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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Instant getDateOfPublishing() {
		return dateOfPublishing;
	}

	public void setDateOfPublishing(Instant dateOfPublishing) {
		this.dateOfPublishing = dateOfPublishing;
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

	@Override
	public String toString() {
		return "Book [id=" + id + ", name=" + title + ", author=" + author + ", dateOfPublishing=" + dateOfPublishing
				+ ", genre=" + genre + ", isbn=" + isbn + "]";
	}
}
