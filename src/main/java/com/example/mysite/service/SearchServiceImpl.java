package com.example.mysite.service;

import com.example.mysite.exception.IncorrectInputDataException;
import com.example.mysite.model.Person;
import com.example.mysite.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchServiceImpl implements SearchService {
    private final SearchRepository repository;

    public int getAge(String name) {
        if (isEmpty(name)) {
            log.info("Ошибка! Имя не может быть пустым");
            throw new IncorrectInputDataException("Name must not be empty");
        }
        var ageByName = repository.getAge(name);
        if (ageByName.isPresent()) {
            log.info(String.format("Возраст %s составляет %s лет", name, ageByName.get()));
            return ageByName.get();
        }
        var newAge = ThreadLocalRandom.current().nextInt(1, 110);
        repository.saveAge(name, newAge);
        log.info(String.format("Пользователя %s отсуствует. Ему сгенерирован возраст %s лет", name, newAge));
        return newAge;
    }

    public Optional<Person> getOldUser() {
        var oldUser = repository.getOldPerson();
        if (oldUser.isEmpty()) {
            log.info("Пользователь с самым большим возрастом не найден!");
            throw new IncorrectInputDataException("Someone must be old :(");
        }
        log.info(String.format("Самый старый %s с возрастом %s лет",
                oldUser.get().name(), oldUser.get().age()));
        return oldUser;


    }

    public Optional<Map.Entry<String, AtomicInteger>> getPopularPerson() {
        var popularUser = repository.getPopularPerson();
        if (popularUser.isEmpty()) {
            log.info("Самый популярный пользователь не найден!");
            throw new IncorrectInputDataException("Someone has to be the most popular :)");
        }
        log.info(String.format("Самый популярный %s с количеством запросов %s",
                popularUser.get().getKey(), popularUser.get().getValue()));
        return popularUser;
    }

    private boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
