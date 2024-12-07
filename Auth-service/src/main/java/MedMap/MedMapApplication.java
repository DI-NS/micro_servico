package MedMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação Spring Boot MedMap.
 * Inicializa o contexto do Spring e inicia o servidor embutido.
 */
@SpringBootApplication
public class MedMapApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedMapApplication.class, args);
	}
}
