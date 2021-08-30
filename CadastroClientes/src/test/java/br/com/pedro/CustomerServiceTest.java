package br.com.pedro;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.pedro.data.model.Customer;
import br.com.pedro.data.vo.v1.CustomerVO;
import br.com.pedro.repository.CustomerRepository;
import br.com.pedro.services.CustomerServices;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CustomerServiceTest {

	@TestConfiguration
	static class CustomerServiceTestConfig {
		
		@Bean(name="service")
		public CustomerServices service() {
			return new CustomerServices();
		}
	}
	
	@Autowired
	@Qualifier("service")
	CustomerServices service;
	
	@MockBean
	CustomerRepository customerRepository;
	
	@Test
	public void customerTestFindById() {
		
		Long id = 1L;
		CustomerVO customer = service.findById(id);
		
		Assertions.assertEquals(customer.getNome(), "João da Silva");
	}
	
	@Before
	public void setup() {
		
		Customer customer = new Customer("João da Silva", "Rua Espírito Santo, 100", LocalDate.parse("1978-10-01"));
		
		Mockito.when(customerRepository.findById(1L)).thenReturn(java.util.Optional.of(customer));
	}
}
