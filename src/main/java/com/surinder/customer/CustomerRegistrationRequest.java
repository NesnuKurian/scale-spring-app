package com.surinder.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age
) {
}
