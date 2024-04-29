package com.todo.uday.todoendpoints.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Product {
	private Long id;
	private String description;
	private String completion;
	private int status;

}
