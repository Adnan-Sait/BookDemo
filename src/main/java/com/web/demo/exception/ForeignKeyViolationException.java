package com.web.demo.exception;

public class ForeignKeyViolationException extends Exception {
	
	public ForeignKeyViolationException(String message) {
		super(message);
	}
}
