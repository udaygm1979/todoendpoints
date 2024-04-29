package com.todo.uday.todoendpoints.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.uday.todoendpoints.dao.ProductRepo;
import com.todo.uday.todoendpoints.dto.ProductRequestDto;
import com.todo.uday.todoendpoints.dto.ProductResponseDto;
import com.todo.uday.todoendpoints.entities.Product;
import com.todo.uday.todoendpoints.exceptions.NoDescriptionException;
import com.todo.uday.todoendpoints.exceptions.NoRecordFoundException;
import com.todo.uday.todoendpoints.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * Method that fetch all the records from database.
	 * If returned list is empty it throws "NoRecordFoundException".
	 * Else it returns list of ProductResponseDto.
	 */
	@Override
	public List<ProductResponseDto> fetchAllProduct() {
		List<Product> prodList = Optional.of(productRepo.fetchAllProduct()).orElse(new ArrayList<>());
		if(prodList.isEmpty()) {
			log.error("Exception faced while fetch all records");
			throw new NoRecordFoundException("No records found");
		}
		List<ProductResponseDto> respLst = prodList.stream().map(this::productResponseDtoMapper).collect(Collectors.toList());
		log.info("All records fetched successfully");
		return respLst;
	}

	
	/**
	 * Method that creates a new record in the database.
	 */
	@Override
	public long saveProduct(ProductRequestDto productRequestDto) {
		productRepo.saveProduct(productMapper(productRequestDto));
		long id = productRepo.getMaxProductId();
		log.info("New rocord saved successfully with id: {}", id);
		return id;
	}

	/**
	 * Method that updates description field of a Product.
	 * If description is empty it throws "NoDescriptionException".
	 * Else it update the field and return ProductResponseDto.
	 */
	@Override
	public ProductResponseDto patchProductById(long id, String description) {
		if(!StringUtils.hasText(description)) {
			log.error("Exception faced while patching record with id: {}", id);
			throw new NoDescriptionException("Please provide valid description. Description provided is :"+description);
		}
		return patchProduct(id, description);
	}

	/**
	 * Method to fetch a record from database based on the id provided.
	 * It searches in the database and map Product entity to ProductResponseDto.
	 * ProductResponseDto is used in the response.
	 */
	@Override
	public ProductResponseDto findProductById(long id) {
		return productResponseDtoMapper(getProductById(id));
	}

	/**
	 * Method used to delete a Product based on the id provided.
	 */
	@Override
	public void deleteProductById(long id) {
		productRepo.deleteProductyId(id);
	}

	private ProductResponseDto patchProduct(long id, String description) {
		Product product = getProductById(id);
		product.setDescription(description);

		productRepo.patchProduct(product);
		log.info("Product with id: {} patched successfully", id);
		return productResponseDtoMapper(product);
	}
	
	private Product getProductById(long id) {
		Product product = null;
		try {
			product = productRepo.findProductById(id);
			log.info("Product with id: {} found successfully", id);
		} catch(EmptyResultDataAccessException ex) {
			log.error("Exception faced while get product by id: {}", id);
			throw new NoRecordFoundException("No record found for id:"+id);
		}
		return product;
	}

	private ProductResponseDto productResponseDtoMapper(Product product) {
		return objectMapper.convertValue(product, ProductResponseDto.class);
	}

	private Product productMapper(ProductRequestDto productRequestDto) {
		return objectMapper.convertValue(productRequestDto, Product.class);
	}
}