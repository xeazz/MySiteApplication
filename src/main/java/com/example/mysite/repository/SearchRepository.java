package com.example.mysite.repository;

import com.example.mysite.model.Person;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public interface SearchRepository {
    void saveAge(String name, int age);

    Optional<Integer> getAge(String name);

    Optional<Person> getOldPerson();

    Optional<Map.Entry<String, AtomicInteger>> getPopularPerson();
}
