package org.cg.ads;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@ImportResource("classpath:app-config.xml")
@ComponentScan
@EnableAutoConfiguration
@EnableScheduling
public class Adscanner extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Adscanner.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Adscanner.class);
	}

}
