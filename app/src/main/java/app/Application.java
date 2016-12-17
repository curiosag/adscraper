package app;

import org.cg.adscraper.factory.StorageFactory;
import org.cg.dispatch.Dispatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import app.storage.SpringStorageFactory;

@ImportResource("classpath:app-config.xml")
@ComponentScan
@EnableAutoConfiguration
@EnableScheduling
public class Application {

	@Autowired
	EmbeddedServletContainerFactory serv;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(final ApplicationContext ctx) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				{
					StorageFactory.setUp(ctx.getBean(SpringStorageFactory.class));
					Dispatch.setUp(ctx.getBean(org.cg.base.MailSessionProperties.class));
					
				}
			}
		};
	}

}