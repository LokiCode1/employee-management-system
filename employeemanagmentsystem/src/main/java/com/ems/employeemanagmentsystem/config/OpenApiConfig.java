package com.ems.employeemanagmentsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI employeeManagementOpenApi() {
		return new OpenAPI()
				.info(new Info()
						.title("Employee Management System API")
						.description("JSON API documentation for Employee endpoints under /employees/api")
						.version("v1")
						.contact(new Contact()
								.name("EMS Development Team")
								.email("dev-team@ems.local"))
						.license(new License()
								.name("Internal Use")));
	}
}
