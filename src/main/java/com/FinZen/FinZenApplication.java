package com.FinZen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class FinZenApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();

		// Poner variables en System properties ANTES
		// de SpringApplication.run()
		System.setProperty("GPT", dotenv.get("GPT"));
		System.setProperty("AWS_ACC_KEY", dotenv.get("AWS_ACC_KEY"));
		System.setProperty("AWS_SEC_KEY", dotenv.get("AWS_SEC_KEY"));
		System.setProperty("JWK_SECRET", dotenv.get("JWK_SECRET"));
		System.setProperty("EMAIL_KEY", dotenv.get("EMAIL_KEY"));
	
		SpringApplication app = new SpringApplication(FinZenApplication.class);
	
		// Esto asegura que Spring tenga acceso a las variables del sistema
		app.setDefaultProperties(System.getProperties());
	
		app.run(args);
	}
}
