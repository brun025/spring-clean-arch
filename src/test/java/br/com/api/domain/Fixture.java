package br.com.api.domain;

import com.github.javafaker.Faker;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static String email() {
    	return FAKER.options().option(
                "test@gmail.com",
                "test1@gmail.com",
                "test2@gmail.com"
        );
    }

    public static String password() {
        return FAKER.options().option(
                "03fe62de",
                "12345",
                "54321"
        );
    }
}

