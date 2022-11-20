package red.eminence.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication(exclude = {SessionAutoConfiguration.class})
@EnableWebFlux
@EnableScheduling
@EnableAsync
public class SvcUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(SvcUserApplication.class, args);
	}

}
