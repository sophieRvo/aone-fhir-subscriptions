package com.athenahealth.eventing.partner;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

import javax.annotation.PreDestroy;

@SpringBootApplication
@ComponentScan(basePackageClasses = PartnerWebhookApplication.class)
@EnableConfigurationProperties
@Slf4j
@Profile("!test")
public class PartnerWebhookApplication {

  public static void main(final String[] args) {
    // Load .env file before Spring Boot starts
    try {
      Dotenv dotenv = Dotenv.configure()
              .ignoreIfMissing()
              .load();
      dotenv.entries().forEach(entry -> {
        if (System.getProperty(entry.getKey()) == null) {
          System.setProperty(entry.getKey(), entry.getValue());
        }
      });
      log.info("Loaded .env file successfully");
    } catch (Exception e) {
      log.warn("Could not load .env file: {}. Using system environment variables instead.", e.getMessage());
    }
    
    SpringApplication.run(PartnerWebhookApplication.class, args);
  }

  @PreDestroy
  public void onExit() {
    log.info( "Service Terminated");
  }

}
