package com.example.mysite.model;

import java.util.Objects;

public record Person(String name, int age) implements Comparable<Person> {
    @Override
    public int compareTo(Person person) {
        return person.age - this.age;
    }
}
