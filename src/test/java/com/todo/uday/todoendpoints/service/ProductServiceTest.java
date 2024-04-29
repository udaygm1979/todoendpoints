package com.todo.uday.todoendpoints.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.uday.todoendpoints.dao.ProductRepo;
import com.todo.uday.todoendpoints.dto.ProductRequestDto;
import com.todo.uday.todoendpoints.dto.ProductResponseDto;
import com.todo.uday.todoendpoints.entities.Product;
import com.todo.uday.todoendpoints.exceptions.NoRecordFoundException;
import com.todo.uday.todoendpoints.service.Impl.ProductServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

	@Mock
	private ProductRepo productRepo;

	@Mock
	private ObjectMapper objectMapper;

	@InjectMocks
	private ProductServiceImpl prodService;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void fetchAllProductTest() {
		Product product = new Product();
		product.setCompletion("Test Completion");
		product.setDescription("Test Description");
		product.setId(1L);
		product.setStatus(1);

		List<Product> lst = new ArrayList<>();
		lst.add(product);

		ProductResponseDto dto = new ProductResponseDto();
		dto.setCompletion("Test Completion");
		dto.setDescription("Test Description");
		dto.setId(1L);
		dto.setStatus(1);

		List<ProductResponseDto> dtoLst = new ArrayList<>();
		dtoLst.add(dto);

		when(productRepo.fetchAllProduct()).thenReturn(lst);
		when(objectMapper.convertValue(product, ProductResponseDto.class)).thenReturn(dto);

		List<ProductResponseDto> respLst = prodService.fetchAllProduct();

		assertThat(respLst.get(0).getId()).isSameAs(respLst.get(0).getId());
		assertThat(respLst.get(0).getDescription()).isSameAs(respLst.get(0).getDescription());
		assertThat(respLst.get(0).getCompletion()).isSameAs(respLst.get(0).getCompletion());
		assertThat(respLst.get(0).getStatus()).isSameAs(respLst.get(0).getStatus());
	}

	@Test
	public void fetchAllProductTestWithException() {
		assertThrows(NoRecordFoundException.class, () -> {
			prodService.fetchAllProduct();
		});
	}
	
	@Test
	public void saveProductTest() {
		ProductRequestDto reqdto = new ProductRequestDto();
		reqdto.setDescription("Test");
		reqdto.setCompletion("Test Completion");
		reqdto.setStatus(1);
		
		ProductResponseDto respdto = new ProductResponseDto();
		respdto.setCompletion("Test Completion");
		respdto.setDescription("Test Description");
		respdto.setId(1L);
		respdto.setStatus(1);
		
		Product product = new Product();
		product.setCompletion("Test Completion");
		product.setDescription("Test Description");
		product.setId(1L);
		product.setStatus(1);
		
		when(objectMapper.convertValue(reqdto, Product.class)).thenReturn(product);
		when(productRepo.getMaxProductId()).thenReturn(1L);
		
		prodService.saveProduct(reqdto);
		verify(productRepo, times(1)).saveProduct(product);
		
		assertTrue(respdto.getId() == 1L);
	}
	
	@Test
	public void patchProductByIdTest() {
		ProductResponseDto respdto = new ProductResponseDto();
		respdto.setCompletion("Test Completion");
		respdto.setDescription("Hello");
		respdto.setId(1L);
		respdto.setStatus(1);
		
		Product product = new Product();
		product.setCompletion("Test Completion");
		product.setDescription("Test Description");
		product.setId(1L);
		product.setStatus(1);
		
		when(productRepo.findProductById(1)).thenReturn(product);
		when(objectMapper.convertValue(product, ProductResponseDto.class)).thenReturn(respdto);
		
		prodService.patchProductById(1, "Hello");
		
		verify(productRepo, times(1)).patchProduct(product);
		assertEquals(respdto.getDescription(), product.getDescription());
		
	}
	
	@Test
	public void findProductById() {
		ProductRequestDto reqdto = new ProductRequestDto();
		reqdto.setDescription("Test");
		reqdto.setCompletion("Test Completion");
		reqdto.setStatus(1);
		
		ProductResponseDto respdto = new ProductResponseDto();
		respdto.setCompletion("Test Completion");
		respdto.setDescription("Test Description");
		respdto.setId(1L);
		respdto.setStatus(1);
		
		Product product = new Product();
		product.setCompletion("Test Completion");
		product.setDescription("Test Description");
		product.setId(1L);
		product.setStatus(1);
		
		when(productRepo.findProductById(1)).thenReturn(product);
		when(objectMapper.convertValue(product, ProductResponseDto.class)).thenReturn(respdto);
		
		ProductResponseDto resultdto = prodService.findProductById(1);
		
		assertTrue(resultdto.getCompletion().equals(respdto.getCompletion()));
		assertTrue(resultdto.getDescription().equals(respdto.getDescription()));
		assertTrue(resultdto.getStatus()==(respdto.getStatus()));
		assertTrue(resultdto.getId()==(respdto.getId()));
	}

}
