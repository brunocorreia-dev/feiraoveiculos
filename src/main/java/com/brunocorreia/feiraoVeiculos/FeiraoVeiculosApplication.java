package com.brunocorreia.feiraoVeiculos;

import com.brunocorreia.feiraoVeiculos.principal.Principal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FeiraoVeiculosApplication implements CommandLineRunner {

	@Value("${feirao.menu.ativo:true}")
	private boolean menuAtivo;

	public static void main(String[] args) {
		SpringApplication.run(FeiraoVeiculosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (!menuAtivo) {
			return;
		}
		Principal principal = new Principal();
		principal.exibeMenu();
	}
}
