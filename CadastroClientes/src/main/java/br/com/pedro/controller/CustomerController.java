package br.com.pedro.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.pedro.data.vo.v1.CustomerVO;
import br.com.pedro.services.CustomerServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Endpoint de Clientes")
@RestController
@RequestMapping("/api/customers/v1")
public class CustomerController {

	@Autowired
	private CustomerServices services;
	
	@Operation(summary = "Lista paginada de clientes cadastrados")
	@GetMapping(produces = {"application/json","application/xml"})
	public ResponseEntity<CollectionModel<CustomerVO>> findAll(
			@RequestParam(value="page", defaultValue = "0") int page,
			@RequestParam(value="limit", defaultValue = "10") int limit,
			@RequestParam(value="dir", defaultValue = "asc") String dir) {
		
		var sortDirection = "desc".equalsIgnoreCase(dir) ? Direction.DESC : Direction.ASC;
		
		Pageable paginacao = PageRequest.of(page, limit, Sort.by(sortDirection,"nome"));
		
		Page<CustomerVO> customers = services.findAll(paginacao);
		
		customers
			.stream()
			.forEach(c -> c.add(
					linkTo(methodOn(this.getClass()).findById(c.getKey())).withSelfRel()
				)
			);
		Link findAllLink = linkTo(methodOn(this.getClass()).findAll(page, limit, dir)).withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(customers, findAllLink));
	}
	
	@Operation(summary = "Lista paginada de clientes cadastrados")
	@GetMapping(value="/findCustomerByName/{nome}", produces = {"application/json","application/xml"})
	public ResponseEntity<CollectionModel<CustomerVO>> findCustomerByName(
			@PathVariable("nome") String nome,
			@RequestParam(value="page", defaultValue = "0") int page,
			@RequestParam(value="limit", defaultValue = "10") int limit,
			@RequestParam(value="dir", defaultValue = "asc") String dir) {
		
		var sortDirection = "desc".equalsIgnoreCase(dir) ? Direction.DESC : Direction.ASC;
		
		Pageable paginacao = PageRequest.of(page, limit, Sort.by(sortDirection,"nome"));
		
		Page<CustomerVO> customers = services.findCustomerByName(nome, paginacao);
		
		customers
			.stream()
			.forEach(c -> c.add(
					linkTo(methodOn(this.getClass()).findById(c.getKey())).withSelfRel()
				)
			);
		Link findAllLink = linkTo(methodOn(this.getClass()).findAll(page, limit, dir)).withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(customers, findAllLink));
	}
	
	@Operation(summary = "Obter um cliente pelo seu código")
	@GetMapping(value="/{id}", produces = {"application/json","application/xml"})
	public CustomerVO findById(@PathVariable("id") Long id) {
		CustomerVO customerVO = services.findById(id);
		customerVO.add(linkTo(methodOn(this.getClass()).findAll(0, 10, "asc")).withRel("Listagem"));
		return customerVO;
	}
	
	@Operation(summary = "Inserir um novo cliente na base")
	@PostMapping(produces = {"application/json","application/xml"},
			consumes = {"application/json","application/xml"})
	public CustomerVO create(@RequestBody CustomerVO customer) {
		CustomerVO customerVO = services.create(customer);
		customerVO.add(linkTo(methodOn(this.getClass()).findById(customerVO.getKey())).withSelfRel());
		return customerVO;		
	}
	
	@Operation(summary = "Atualizar os dados de um cliente previamente cadastrado")
	@PutMapping(produces = {"application/json","application/xml"},
			consumes = {"application/json","application/xml"})
	public CustomerVO update(@RequestBody CustomerVO customer) {
		CustomerVO customerVO = services.update(customer);
		customerVO.add(linkTo(methodOn(this.getClass()).findById(customer.getKey())).withSelfRel());
		return customerVO;		
	}
	
	@PatchMapping(
			value="/{id}",
			produces = {"application/json","application/xml"},
			consumes = {"application/json","application/xml"})
	public CustomerVO patch(
			@PathVariable Long id,
			@RequestBody Map<Object, Object> fields) {
		CustomerVO customerVO = services.patch(id, fields);
		customerVO.add(linkTo(methodOn(this.getClass()).findById(id)).withSelfRel());
		return customerVO;
	}
	
	@Operation(summary = "Excluir um cliente pelo seu código")
	@DeleteMapping(value="/{id}")
	public void delete(@PathVariable("id") Long id) {
		services.delete(id);
	}	
}
