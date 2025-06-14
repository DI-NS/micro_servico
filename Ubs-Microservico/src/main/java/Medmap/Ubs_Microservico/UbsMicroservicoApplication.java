package Medmap.Ubs_Microservico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "Medmap.Ubs_Microservico.client")
public class UbsMicroservicoApplication {
	public static void main(String[] args) {
		SpringApplication.run(UbsMicroservicoApplication.class, args);
	}
}
