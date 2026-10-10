package com.brunocorreia.feiraoVeiculos;

import com.brunocorreia.feiraoVeiculos.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FeiraoVeiculosApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(FeiraoVeiculosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.exibeMenu();
	}
}
