package com.todo.uday.todoendpoints.exceptions;

public class NoRecordFoundException extends RuntimeException{
	
//	private String message;
	
	public NoRecordFoundException(String message) {
		super(message);
	}

}
