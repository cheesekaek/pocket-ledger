package com.example.pl;

import com.example.pl.repository.ExpensesRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PockLedApplication {
    public static void main(String[] args) {
        SpringApplication.run(PockLedApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(ExpensesRepository expensesRepository) {
        return args -> {

        };
    }
}