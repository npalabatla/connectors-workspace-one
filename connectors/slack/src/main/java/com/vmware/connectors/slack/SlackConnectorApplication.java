package com.vmware.connectors.slack;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * Created by harshas on 1/15/18.
 */
@SpringBootApplication
@EnableResourceServer
@EnableWebSecurity
@EnableScheduling
@SuppressWarnings("PMD.UseUtilityClass")
public class SlackConnectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SlackConnectorApplication.class, args);
    }
}
