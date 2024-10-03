package com.myversion.myversion;

// import com.myversion.myversion.FlaskProperties;
import com.myversion.myversion.service.Service;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(FlaskProperties.class)
public class MyversionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyversionApplication.class, args);
	}
}
