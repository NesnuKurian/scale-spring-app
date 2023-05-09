package com.surinder;

import com.surinder.customer.Customer;
import com.surinder.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ComponentScan(basePackages = "com.surinder")
@EnableAutoConfiguration
@Configuration
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

        //ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class, args);

        // you can find beans with the following code, predefined or which are already there in the springboot
        /*String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }*/
    }
    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository){

        return args -> {

            Customer surinder = new Customer(1,
                    "Surinder",
                    "surinder@gmail.com",
                    23);
            Customer sachin = new Customer(2,
                    "sachin",
                    "sachin@gmail.com",
                    21);
            List<Customer> customers = List.of(surinder, sachin);
            customerRepository.saveAll(customers);
        };
    }
}
