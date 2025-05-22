package com.FinZen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class FinZenApplication {

	public static void main(String[] args) {
		 Dotenv dotenv = Dotenv.load(); // Carga el archivo .env
        System.setProperty("GPT", dotenv.get("GPT"));
        System.setProperty("AWS_ACC_KEY", dotenv.get("AWS_ACC_KEY"));
        System.setProperty("AWS_SEC_KEY", dotenv.get("AWS_SEC_KEY"));
		
		SpringApplication.run(FinZenApplication.class, args);
	}

}
