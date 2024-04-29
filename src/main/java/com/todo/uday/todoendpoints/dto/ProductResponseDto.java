package com.todo.uday.todoendpoints.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductResponseDto {
	
	private Long id;
	private String description;
	private String completion;
	private int status;

}
