package com.todo.uday.todoendpoints.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomErrorInfo {
	private String message;
	private HttpStatus status;
	
}
