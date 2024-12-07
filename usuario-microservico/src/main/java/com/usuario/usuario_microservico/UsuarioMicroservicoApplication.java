package com.usuario.usuario_microservico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UsuarioMicroservicoApplication {

	public static void main(String[] args) {

		SpringApplication.run(UsuarioMicroservicoApplication.class, args);
	}

}
