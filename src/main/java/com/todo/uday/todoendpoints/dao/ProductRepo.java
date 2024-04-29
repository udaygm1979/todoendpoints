package com.todo.uday.todoendpoints.dao;

import java.util.List;
import java.util.Optional;

import com.todo.uday.todoendpoints.entities.Product;

public interface ProductRepo {
	List<Product> fetchAllProduct();

//	Optional<Product> findProductById(long id);
	
	Product findProductById(long id);

	void saveProduct(Product product);

	void deleteProductyId(long id);
	
	void patchProduct(Product product);
	
	Long getMaxProductId();

}
