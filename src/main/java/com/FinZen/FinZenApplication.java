package com.FinZen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FinZenApplication {

	public static void main(String[] args) {

		System.out.println("======= INICIO: Verificación de variables de entorno =======");

		setEnvIfPresent("GPT");
		setEnvIfPresent("AWS_ACC_KEY");
		setEnvIfPresent("AWS_SEC_KEY");
		setEnvIfPresent("JWK_SECRET");
		setEnvIfPresent("EMAIL_KEY");

		System.out.println("=============================================================");

		SpringApplication app = new SpringApplication(FinZenApplication.class);
		app.setDefaultProperties(System.getProperties());
		app.run(args);
	}

	private static void setEnvIfPresent(String key) {
		String value = System.getenv(key);
		if (value != null && !value.isEmpty()) {
			System.setProperty(key, value);
			System.out.println("✅ " + key + " definida correctamente.");
		} else {
			System.out.println("⚠️ Advertencia: La variable de entorno '" + key + "' no está definida.");
		}
	}
}
