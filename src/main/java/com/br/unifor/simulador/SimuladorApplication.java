package com.br.unifor.simulador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Ponto de entrada. Sobe o servidor em http://localhost:8080
// e serve a página que está em src/main/resources/static/index.html
@SpringBootApplication
public class SimuladorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimuladorApplication.class, args);
	}

}
