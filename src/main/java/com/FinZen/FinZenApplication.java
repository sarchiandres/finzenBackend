package com.FinZen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class FinZenApplication {

	public static void main(String[] args) {
		// Intentamos obtener desde el entorno del sistema (Railway u OS)
		String gpt = System.getenv("GPT");
		String accKey = System.getenv("AWS_ACC_KEY");
		String secKey = System.getenv("AWS_SEC_KEY");
		String jwt = System.getenv("JWK_SECRET");
		String emailKey = System.getenv("EMAIL_KEY");

		// Si alguna variable está vacía, la obtenemos del archivo .env
		if (gpt == null || accKey == null || secKey == null || jwt == null || emailKey == null) {
			Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
			if (gpt == null) gpt = dotenv.get("GPT");
			if (accKey == null) accKey = dotenv.get("AWS_ACC_KEY");
			if (secKey == null) secKey = dotenv.get("AWS_SEC_KEY");
			if (jwt == null) jwt = dotenv.get("JWK_SECRET");
			if (emailKey == null) emailKey = dotenv.get("EMAIL_KEY");
		}

		System.setProperty("GPT", gpt);
		System.setProperty("AWS_ACC_KEY", accKey);
		System.setProperty("AWS_SEC_KEY", secKey);
		System.setProperty("JWK_SECRET", jwt);
		System.setProperty("EMAIL_KEY", emailKey);

		SpringApplication app = new SpringApplication(FinZenApplication.class);
		app.setDefaultProperties(System.getProperties());
		app.run(args);
	}
}
