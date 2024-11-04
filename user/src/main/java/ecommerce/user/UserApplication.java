package ecommerce.user;

import ecommerce.common.service.Aop.LogServiceAdvice;
import ecommerce.common.service.Config.LoggingConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@SpringBootApplication
@Import({LogServiceAdvice.class})
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

	@Bean
	public CommandLineRunner createLogsDirectory() {
		return args -> {
			String[] logDirs = {"./auth-server/logs/node1", "./auth-server/logs/node2"};

			for (String logsDir : logDirs) {
				File directory = new File(logsDir);

				if (!directory.exists()) {
					try {
						Files.createDirectories(Paths.get(logsDir));
						log.info("Logs directory {} created successfully.", logsDir);
					} catch (IOException e) {
						log.error("Failed to create logs directory {}: {}", logsDir, e.getMessage());
					}
				} else {
					log.info("Logs directory {} already exists.", logsDir);
				}
			}

			// Setup file logging
			LoggingConfig.setupFileLogging();
		};
	}

}
