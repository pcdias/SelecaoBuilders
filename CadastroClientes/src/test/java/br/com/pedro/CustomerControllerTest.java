package br.com.pedro;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pedro.data.model.Customer;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

	private static final String API_CUSTOMERS_V1 = "/api/customers/v1/";
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void customerTestFindAll() throws Exception {

		mockMvc.perform(get(API_CUSTOMERS_V1))
			.andExpect(status()
			.isOk());
	}
	
	@Test
	public void customerTestCreate() throws JsonProcessingException, Exception {
		
		Customer customer = new Customer("João da Silva", "Rua Espírito Santo, 100", LocalDate.parse("1978-10-01"));
		
		mockMvc.perform(post(API_CUSTOMERS_V1)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(customer)))
				.andExpect(status().isOk());
	}
	
}
