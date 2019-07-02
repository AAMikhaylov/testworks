package ru.rgs.csvparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Import;
import ru.rgs.csvparser.configuration.MainConfiguration;

@EnableFeignClients
@SpringBootApplication
@Import(MainConfiguration.class)
public class Starter {
    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }
}