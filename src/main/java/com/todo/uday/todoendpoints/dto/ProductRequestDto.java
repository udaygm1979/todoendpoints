package com.todo.uday.todoendpoints.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductRequestDto {
	@NotNull
	@Size(min = 5, max = 50)
	private String description;
	
	@NotNull
	@Size(min = 5, max = 50)
	private String completion;
	
	@NotNull
	@Max(value = 1, message = "Status is incorrect it can be 0 or 1")
	@Min(value = 0, message = "Status is incorrect it can be 0 or 1")
	private int status;
	
	
}
