package com.todo.uday.todoendpoints.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.uday.todoendpoints.dto.ProductRequestDto;
import com.todo.uday.todoendpoints.dto.ProductResponseDto;
import com.todo.uday.todoendpoints.service.ProductService;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

	@MockBean
	private ProductService productService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void productInsertionSuccess() throws Exception {
		ProductRequestDto dto = new ProductRequestDto();
		dto.setDescription("Test Description");
		dto.setCompletion("Test Completion");
		dto.setStatus(1);
		Mockito.when(productService.saveProduct(dto)).thenReturn(1L);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/todos").content(toJson(dto))
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

		String headerValue = result.getResponse().getHeader("location");
		String expectedValue = "http://localhost/todos/0";

		assertTrue(headerValue.equals(expectedValue));
		assertTrue(result.getResponse().getStatus() == HttpStatus.CREATED.value());

	}

	@Test
	public void productInsertionFailure() throws Exception {

		ProductRequestDto dto = new ProductRequestDto();
		dto.setDescription("Test");
		dto.setCompletion("Test Completion");
		dto.setStatus(1);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/todos").content(toJson(dto))
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.description", Is.is("size must be between 5 and 50")))
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		assertTrue(result.getResponse().getStatus() == HttpStatus.BAD_REQUEST.value());
		assertTrue(result.getResponse().getContentAsString().contains("size must be between 5 and 50"));
	}

	@Test
	public void productPatchedSuccess() throws Exception {

		ProductResponseDto respDto = new ProductResponseDto();
		respDto.setId(1L);
		respDto.setDescription("PatchTest");
		respDto.setCompletion("Test Completion");
		respDto.setStatus(1);

		Mockito.when(productService.patchProductById(1, "PatchTest")).thenReturn(respDto);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/todos/1?description=PatchTest"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		assertTrue(result.getResponse().getStatus() == HttpStatus.OK.value());
		String contentValue = result.getResponse().getContentAsString();
		ProductResponseDto content = toResponseDto(contentValue);
		assertTrue(content.getDescription().equals(respDto.getDescription()));
	}
	
//	@Test
	public void productPatchedSuccessFailure() throws Exception {
//		CustomErrorInfo error = new CustomErrorInfo();
//		error.setMessage("Required request parameter 'description' for method parameter type String is not present");
//		error.setStatus(HttpStatus.BAD_REQUEST);
//
//		Mockito.when(productService.patchProductById(1, "PatchTest")).thenThrow(MissingServletRequestParameterException.class);
		
//		assertThrows(NoDescriptionException.class, () -> {
//			productService.patchProductById(1, "");
//		});
	}
	
	
	@Test
	public void productByIdSuccess() throws Exception {
		ProductResponseDto respDto = new ProductResponseDto();
		respDto.setId(1L);
		respDto.setDescription("PatchTest");
		respDto.setCompletion("Test Completion");
		respDto.setStatus(1);
		
		Mockito.when(productService.findProductById(1)).thenReturn(respDto);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/todos/1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		assertTrue(result.getResponse().getStatus() == HttpStatus.OK.value());
		
		ProductResponseDto respResult = toResponseDto(result.getResponse().getContentAsString());
		assertTrue(respResult.getCompletion().equals(respDto.getCompletion()));
		assertTrue(respResult.getDescription().equals(respDto.getDescription()));
		assertTrue(respResult.getStatus() == respDto.getStatus());
		assertTrue(respResult.getId() == respDto.getId());
	}
	
	@Test
	public void fetchAllProductSuccess() throws Exception {
		ProductResponseDto respDto = new ProductResponseDto();
		respDto.setId(1L);
		respDto.setDescription("PatchTest");
		respDto.setCompletion("Test Completion");
		respDto.setStatus(1);
		
		List<ProductResponseDto> lst = new ArrayList<>();
		lst.add(respDto);
		
		Mockito.when(productService.fetchAllProduct()).thenReturn(lst);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/todos"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		assertTrue(result.getResponse().getStatus() == HttpStatus.OK.value());
		
		List<ProductResponseDto> respResult = toResponseDtoList(result.getResponse().getContentAsString());
		assertTrue(respResult.get(0).getCompletion().equals(respDto.getCompletion()));
		assertTrue(respResult.get(0).getDescription().equals(respDto.getDescription()));
		assertTrue(respResult.get(0).getStatus() == respDto.getStatus());
		assertTrue(respResult.get(0).getId() == respDto.getId());
	}
	
	@Test
	public void productDelete() throws Exception {

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/todos/1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		assertTrue(result.getResponse().getStatus() == HttpStatus.OK.value());
	}

	private String toJson(ProductRequestDto productRequestDto) throws JsonProcessingException {
		return objectMapper.writeValueAsString(productRequestDto);
	}
	
	private ProductResponseDto toResponseDto(String str) throws JsonMappingException, JsonProcessingException {
		return objectMapper.readValue(str, ProductResponseDto.class);
	}
	
	
	private List<ProductResponseDto> toResponseDtoList(String str) throws JsonMappingException, JsonProcessingException {
		List<ProductResponseDto> dto =  objectMapper.readValue(str, new TypeReference<List<ProductResponseDto>>(){});
		return dto;
	}
}
