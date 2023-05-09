package com.surinder.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {
}
