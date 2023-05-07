package com.surinder;

import com.surinder.customer.Customer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@ComponentScan(basePackages = "com.surinder")
@EnableAutoConfiguration
@Configuration
public class Main {

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);
    }
}
