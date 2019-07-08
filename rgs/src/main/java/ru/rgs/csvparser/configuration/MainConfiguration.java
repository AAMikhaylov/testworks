package ru.rgs.csvparser.configuration;

import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rgs.csvparser.feign.UserClient;
import ru.rgs.csvparser.service.CsvParserService;
import ru.rgs.csvparser.service.CsvParserServiceImpl;


@Configuration
public class MainConfiguration {
    @Value("${feign.userclient.url}")
    String userClientURL;
    @Bean
    public CsvParserService csvParserService() {
        return new CsvParserServiceImpl();
    }
    @Bean
    public UserClient userClient() {
        return Feign.builder()
                .contract(new SpringMvcContract())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(new Logger.ErrorLogger())
                .logLevel(Logger.Level.FULL)
                .target(UserClient.class,userClientURL);


    }

}
