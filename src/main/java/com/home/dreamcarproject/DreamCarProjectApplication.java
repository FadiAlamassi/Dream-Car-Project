package com.home.dreamcarproject;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


import java.util.Properties;


@SpringBootApplication
@EnableJpaRepositories("com.home.dreamcarproject.repository")
@EnableScheduling
public class DreamCarProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(DreamCarProjectApplication.class, args);
    }

//    @Bean
//    ApplicationRunner init(UserRepository repository) {
//        // Save our starter set of books
//        return args -> {
//            Stream.of(new User("Admin", "fadi", "fadi","fadi@gmail.com", "123456789")).forEach(user -> {
//                repository.save(user);
//            });
//            //retrieve them all, and print so that we see everything is wired up correctly
//            repository.findAll().forEach(System.out::println);
//        };
//    }

}
