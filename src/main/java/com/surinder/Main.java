package com.surinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@ComponentScan(basePackages = "com.surinder")
@EnableAutoConfiguration
@Configuration
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @GetMapping("/greet")
    public GreetResponse greet(@RequestParam(value = "name", required = false) String name){
        String greetMessage = name.isBlank()?"Hello ": "Hello "+name;

        return new GreetResponse(greetMessage,
                List.of("Java","Python","GoLang","Javascript"),
                new Person("Surinder","surinder@gmail.com","password"));
    }
}

record Person(String name, String email, String password){}
record GreetResponse(String greet,
                     List<String> favLanguage,
                     Person person
                     ){}
