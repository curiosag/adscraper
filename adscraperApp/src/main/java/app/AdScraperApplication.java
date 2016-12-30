package app;

import org.apache.commons.logging.LogFactory;
import org.cg.common.io.logging.DelegatingOutputStream;
import org.cg.common.io.logging.OnLineWritten;
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

@ImportResource("classpath:app-config.xml")
@ComponentScan
@EnableAutoConfiguration
public class AdScraperApplication {

	@Autowired
	EmbeddedServletContainerFactory serv;

	@Value("${logging.forkSystemOutput}")
	String forkSystemOutput;

	public static void main(String[] args) {
		SpringApplication.run(AdScraperApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(final ApplicationContext ctx) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				{
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
					LogFactory.getLog("r").info(value);				}
			};

			System.setOut(DelegatingOutputStream.createPrintStream(System.out, redirect));
			System.setErr(DelegatingOutputStream.createPrintStream(System.err, redirect));
		}
	}

}