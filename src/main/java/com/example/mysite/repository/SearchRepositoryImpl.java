package com.example.mysite.repository;

import com.example.mysite.model.Person;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@RequiredArgsConstructor
@Getter
@Slf4j
public class SearchRepositoryImpl implements SearchRepository {
    private final Map<Person, AtomicInteger> personMap = new ConcurrentHashMap<>();
    private final FileParser fileParser;

    @EventListener(ApplicationReadyEvent.class)
    public Map<Person, AtomicInteger> queryFile() {
        log.info("Файл инициализирован!");
        return fileParser.read(personMap);
    }

    @Override
    public Optional<Integer> getAge(String name) {
        return personMap.entrySet().stream()
                .filter(e -> e.getKey().name().equals(name))
                .findFirst()
                .map(x -> {
                    x.getValue().getAndIncrement();
                    return x.getKey().age();
                });
    }

    @Override
    public void saveAge(String name, int age) {
        fileParser.save(name, age);
        personMap.putIfAbsent(new Person(name, age), new AtomicInteger(1));
        log.info(String.format("Пользователь %s с возрастом %s успешно сохранен!", name, age));
    }

    @Override
    public Optional<Person> getOldPerson() {
        return personMap.keySet().stream()
                .max(Comparator.comparingInt(Person::age));
    }

    @Override
    public Optional<Map.Entry<String, AtomicInteger>> getPopularPerson() {
        return personMap.entrySet().stream()
                .max(Map.Entry.comparingByValue(Comparator.comparing(AtomicInteger::get)))
                .map(e -> Map.entry(e.getKey().name(), e.getValue()));
    }
}
