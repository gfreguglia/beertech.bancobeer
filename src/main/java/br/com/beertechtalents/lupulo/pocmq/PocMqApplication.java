package br.com.beertechtalents.lupulo.pocmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PocMqApplication {

	public static void main(String[] args) {
		SpringApplication.run(PocMqApplication.class, args);
	}

}
