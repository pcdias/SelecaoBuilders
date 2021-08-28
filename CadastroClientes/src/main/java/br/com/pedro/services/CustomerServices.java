package br.com.pedro.services;

import java.lang.reflect.Field;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import br.com.pedro.converter.DozerConverter;
import br.com.pedro.data.model.Customer;
import br.com.pedro.data.vo.v1.CustomerVO;
import br.com.pedro.exception.ResourceNotFoundException;
import br.com.pedro.repository.CustomerRepository;

@Service
public class CustomerServices {

	@Autowired
	CustomerRepository repository;
	
	public CustomerVO create(CustomerVO customer) {
		var entity = DozerConverter.parseObject(customer, Customer.class);
		return DozerConverter.parseObject(repository.save(entity), CustomerVO.class);
	}
	
	public Page<CustomerVO> findAll(Pageable pageable) {
		var page = repository.findAll(pageable);
		return page.map(this::convertToCustomerVO);
	}
	
	public Page<CustomerVO> findCustomerByName(String nome, Pageable pageable) {
		var page = repository.findCustomerByName(nome, pageable);
		return page.map(this::convertToCustomerVO);
	}
	
	private CustomerVO convertToCustomerVO(Customer entity) {
		return DozerConverter.parseObject(entity, CustomerVO.class);
	}
	
	public CustomerVO findById(Long id) {
		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Nenhum cliente encontrado com o id especificado."));
		return DozerConverter.parseObject(entity, CustomerVO.class);
	}
	
	public CustomerVO update(CustomerVO customer) {
		var entity = repository.findById(customer.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("Nenhum cliente encontrado com o id especificado."));
		
		entity.setNome(customer.getNome());
		entity.setDataNascimento(customer.getDataNascimento());
		entity.setEndereco(customer.getEndereco());
		
		return DozerConverter.parseObject(repository.save(entity), CustomerVO.class);
	}
	
	public CustomerVO patch(Long id, Map<Object, Object> fields) {
		Customer entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Nenhum cliente encontrado com o id especificado."));
		fields.forEach((key, value) -> {
			Field field = ReflectionUtils.findField(Customer.class, (String )key);
			field.setAccessible(true);
			ReflectionUtils.setField(field, entity, value);
		});
		return DozerConverter.parseObject(repository.save(entity), CustomerVO.class);
	}
	
	public void delete(Long id) {
		Customer entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Nenhum cliente encontrado com o id especificado."));
		
		repository.delete(entity);
	}
	
}
