package com.FinZen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FinZenApplication {

	public static void main(String[] args) {

		setEnvIfPresent("GPT");
		setEnvIfPresent("AWS_ACC_KEY");
		setEnvIfPresent("AWS_SEC_KEY");
		setEnvIfPresent("JWK_SECRET");
		setEnvIfPresent("EMAIL_KEY");

		SpringApplication app = new SpringApplication(FinZenApplication.class);
		app.setDefaultProperties(System.getProperties());
		app.run(args);
	}

	private static void setEnvIfPresent(String key) {
		String value = System.getenv(key);
		if (value != null) {
			System.setProperty(key, value);
		} else {
			System.out.println("⚠️ Advertencia: La variable de entorno '" + key + "' no está definida.");
		}
	}
}
