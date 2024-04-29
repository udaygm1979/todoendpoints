package com.todo.uday.todoendpoints.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.todo.uday.todoendpoints.dto.ProductRequestDto;
import com.todo.uday.todoendpoints.dto.ProductResponseDto;
import com.todo.uday.todoendpoints.exceptions.CustomErrorInfo;
import com.todo.uday.todoendpoints.exceptions.NoDescriptionException;
import com.todo.uday.todoendpoints.exceptions.NoRecordFoundException;
import com.todo.uday.todoendpoints.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/todos")
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping
	public List<ProductResponseDto> products() {
		return productService.fetchAllProduct();
	}

	/**
	 * Method to handle Get requests with provided id as path variable.
	 * Url e.g: /todos/1
	 * @param id
	 * @return ProductResponseDto
	 */
	@GetMapping(path = "/{id}")
	public ProductResponseDto product(@PathVariable int id) {
		return productService.findProductById(id);
	}

	/**
	 * Method to perform delete operation for a specified id.
	 * Url e.g: /todos/1
	 * @param id
	 */
	@DeleteMapping(path = "/{id}")
	public void productDeletion(@PathVariable int id) {
		productService.deleteProductById(id);
	}

	/**
	 * Method to create a new Product.
	 * Details are read from ProductRequestDto and mapped to Product entity.
	 * @param productRequestDto
	 * @param uriComponentsBuilder
	 * @return Status code, message and new resource URL in the Location header. 
	 */
	@PostMapping
	public ResponseEntity<String> productInsertion(@Valid @RequestBody ProductRequestDto productRequestDto,
			UriComponentsBuilder uriComponentsBuilder) {
		long id = productService.saveProduct(productRequestDto);
		URI productLocation = uriComponentsBuilder.path("/todos/")
				.path(String.valueOf(id)).build().toUri();

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(productLocation);
		return new ResponseEntity<String>("New Entry Created", headers, HttpStatus.CREATED);
	}

	/**
	 * Method to update description field for a specified id.
	 * URL e.g: /todo/1?description=test
	 * @param id
	 * @param description
	 * @return ProductResponseDto
	 */
	@PatchMapping(path = "/{id}")
	public ProductResponseDto productPatched(@PathVariable int id, @RequestParam String description) {
		return productService.patchProductById(id, description);
	}


	/**
	 * Controller level exception handler.
	 * It handles custom exception "NoRecordFoundException".
	 * @param ex
	 * @return CustomErrorInfo
	 */
	@ExceptionHandler(value = NoRecordFoundException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public CustomErrorInfo handleNoRecordFound(NoRecordFoundException ex) {
		CustomErrorInfo error = new CustomErrorInfo();
		error.setMessage(ex.getMessage());
		error.setStatus(HttpStatus.NOT_FOUND);
		return error;
	}

	/**
	 * Controller level exception handler.
	 * It handles custom exception "NoDescriptionException".
	 * @param ex
	 * @return CustomErrorInfo
	 */
	@ExceptionHandler(value = NoDescriptionException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public CustomErrorInfo handleNoRecordFound(NoDescriptionException ex) {
		CustomErrorInfo error = new CustomErrorInfo();
		error.setMessage(ex.getMessage());
		error.setStatus(HttpStatus.BAD_REQUEST);
		return error;
	}

	/**
	 * Controller level exception handler.
	 * It handles exception "MethodArgumentNotValidException".
	 * @param ex
	 * @return Map of field name with error description.
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}

	/**
	 * Controller level exception handler.
	 * It handles exception "MissingServletRequestParameterException".
	 * @param ex
	 * @return CustomErrorInfo
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CustomErrorInfo handleValidationExceptions(MissingServletRequestParameterException ex) {
		CustomErrorInfo error = new CustomErrorInfo();
		error.setMessage(ex.getMessage());
		error.setStatus(HttpStatus.BAD_REQUEST);
		return error;
	}
}
