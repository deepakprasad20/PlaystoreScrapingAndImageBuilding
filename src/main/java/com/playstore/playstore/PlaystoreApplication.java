package com.playstore.playstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories(basePackages = "com.playstore.playstore.Repository")
@SpringBootApplication(scanBasePackages={"com.playstore.playstore.Service","com.playstore.playstore"})
public class PlaystoreApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(PlaystoreApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(PlaystoreApplication.class);
	}

}
