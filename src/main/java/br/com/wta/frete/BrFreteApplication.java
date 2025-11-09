package br.com.wta.frete;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BrFreteApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrFreteApplication.class, args);
	}

}
