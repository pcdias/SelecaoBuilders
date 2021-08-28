package br.com.pedro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().info(
			new Info()
				.title("RESTFul API - Seleção Builders")
				.version("v1")
				.description("MVP de cadastro de clientes")
				.termsOfService("http://springdoc.org")
				.license(new License().name("Free").url("http://springdoc.org")));
	}
}
