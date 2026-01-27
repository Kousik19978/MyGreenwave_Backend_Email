package in.co.gw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EmailApiApplication extends SpringBootServletInitializer  {

	public static void main(String[] args) {
		SpringApplication.run(EmailApiApplication.class, args);
	}

}
