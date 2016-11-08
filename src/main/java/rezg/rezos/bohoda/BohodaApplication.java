package rezg.rezos.bohoda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class BohodaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BohodaApplication.class, args);
	}
}
