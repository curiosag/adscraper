package app;

import org.cg.adscraper.factory.StorageFactory;
import org.cg.common.io.logging.DelegatingOutputStream;
import org.cg.common.io.logging.OnLineWritten;
import org.cg.dispatch.Dispatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${logging.forkSystemOutput}")
	String forkSystemOutput;

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
					forkSystemOutput();
				}
			}
		};
	}

	private void forkSystemOutput() {
		if ("true".equals(forkSystemOutput)) {
			OnLineWritten redirect = new OnLineWritten() {
				@Override
				public void notify(String value) {
					java.util.logging.Logger.getGlobal().info(value);
				}
			};

			System.setOut(DelegatingOutputStream.createPrintStream(System.out, redirect));
			System.setErr(DelegatingOutputStream.createPrintStream(System.err, redirect));
		}
	}

}