package com.example.mysite.service;

import com.example.mysite.model.Person;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public interface SearchService {
    int getAge(String name);

    Optional<Person> getOldUser();

    Optional<Map.Entry<String, AtomicInteger>> getPopularPerson();
}
