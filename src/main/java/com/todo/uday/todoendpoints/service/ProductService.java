package com.todo.uday.todoendpoints.service;

import java.util.List;

import com.todo.uday.todoendpoints.dto.ProductRequestDto;
import com.todo.uday.todoendpoints.dto.ProductResponseDto;

public interface ProductService {
	
	List<ProductResponseDto> fetchAllProduct();
	
	ProductResponseDto findProductById(long id);
	
	long saveProduct(ProductRequestDto productDto);
	
	void deleteProductById(long id);
	
	ProductResponseDto patchProductById(long id, String description);

}
