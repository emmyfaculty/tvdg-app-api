package com.tvdgapp;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.Servers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Deluxe Link Enterprise API",
				version = "1.0",
				description = "DLE Back-end Rest API Documentation v1.0",
				contact = @Contact(
						name = "Emmanuel Ahola",
						email = "aholemmy@gmail.com",
						url = "https://github.com/emmyfaculty/emmy-bank"
				),
				license = @License(
						name = "DLE",
						url = "https://github.com/emmyfaculty/emmy-bank"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Deluxe Link Enterprise Back-end Rest API Documentation v1.0",
				url = "https://github.com/emmyfaculty/emmy-bank"
		),
		servers = {
				@Server(
						description = "Local ENV",
						url = "http://localhost:8080/tvdgapp"
				),
				@Server(
						description = "Dev ENV",
						url = "http://167.86.94.2:8080/tvdgapp"
				)
		}
)
@EnableTransactionManagement
public class TvdgAppApiApplication {


	public static void main(String[] args) {
		SpringApplication.run(TvdgAppApiApplication.class, args);
	}

}
