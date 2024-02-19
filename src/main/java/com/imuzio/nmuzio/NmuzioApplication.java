package com.imuzio.nmuzio;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NmuzioApplication {

	public static void main(String[] args) {
		SpringApplication.run(NmuzioApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI(){
		return new OpenAPI()
				.info(new Info().title("Ignacio Muzio 2nd Test")
						.version("1.0.0")
						.description("This is the second test from Ignacio Muzio for MobyDigital"));
	}
}
