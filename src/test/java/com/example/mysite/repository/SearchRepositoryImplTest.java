package com.example.mysite.repository;

import com.example.mysite.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
public class SearchRepositoryImplTest {
    SearchRepositoryImpl repository;
    @Mock
    FileParser fileParser;

    public SearchRepositoryImplTest() {
        MockitoAnnotations.openMocks(this);
        this.repository = new SearchRepositoryImpl(fileParser);
    }

    @BeforeAll
    public static void initTransferRepository() {
        log.info("Running test");
    }

    @AfterAll
    public static void completeTransferRepository() {
        log.info("Test completed");
    }

    @Test
    @DisplayName("Saving new users to the Map. Unfavorable outcome")
    public void savePersonToMapFirstTest() {
        Person person = new Person("Сергей", 33);
        var map = new ConcurrentHashMap<>();
        map.putIfAbsent(person, new AtomicInteger(1));
        repository.saveAge("Иван", 2);
        assertNotEquals(map, repository.getPersonMap());
    }

    @Test
    @DisplayName("Saving new users to the Map. Favorable outcome")
    public void savePersonToMapSecondTest() {
        Person person = new Person("Павел", 33);
        var map = new ConcurrentHashMap<>();
        map.putIfAbsent(person, new AtomicInteger(1));
        repository.saveAge("Павел", 33);
        assertNotEquals(map, repository.getPersonMap());
    }

    @Test
    @DisplayName("Getting age by name when the name is on the Map.")
    public void getAgeFirstTest() {
        repository.getPersonMap().putIfAbsent(new Person("Иван", 12), new AtomicInteger(12));
        assertEquals(Optional.of(12), repository.getAge("Иван"));
    }

    @Test
    @DisplayName("Getting age from name when name is not in Map")
    public void getAgeSecondTest() {
        repository.getPersonMap().putIfAbsent(new Person("Иван", 12), new AtomicInteger(12));
        assertEquals(Optional.empty(), repository.getAge("Паша"));
    }

    @Test
    @DisplayName("Getting the oldest user. Favorable outcome")
    public void getOldPersonFirstTest() {
        AtomicInteger atomicInteger = new AtomicInteger(12);
        Person personFirst = new Person("Иван", 99);
        Person personSecond = new Person("Слава", 33);
        Person personThird = new Person("Anton", 65);
        repository.getPersonMap().putIfAbsent(personFirst, atomicInteger);
        repository.getPersonMap().putIfAbsent(personSecond, atomicInteger);
        repository.getPersonMap().putIfAbsent(personThird, atomicInteger);
        assertEquals(Optional.of(personFirst), repository.getOldPerson());
    }

    @Test
    @DisplayName("Getting the oldest user. Unfavorable outcome")
    public void getOldPersonSecondTest() {
        AtomicInteger atomicInteger = new AtomicInteger(12);
        Person personFirst = new Person("Иван", 99);
        Person personSecond = new Person("Слава", 33);
        Person personThird = new Person("Anton", 65);
        repository.getPersonMap().putIfAbsent(personFirst, atomicInteger);
        repository.getPersonMap().putIfAbsent(personSecond, atomicInteger);
        repository.getPersonMap().putIfAbsent(personThird, atomicInteger);
        assertNotEquals(Optional.of(personSecond), repository.getOldPerson());
    }

    @Test
    @DisplayName("Getting the popular user. Favorable outcome")
    public void getPopularPersonFirstTest() {
        Person personFirst = new Person("Иван", 99);
        Person personSecond = new Person("Слава", 33);
        Person personThird = new Person("Anton", 65);
        AtomicInteger atomicIntegerFirst = new AtomicInteger(1);
        AtomicInteger atomicIntegerSecond = new AtomicInteger(12);
        AtomicInteger atomicIntegerThird = new AtomicInteger(33);
        repository.getPersonMap().putIfAbsent(personFirst, atomicIntegerFirst);
        repository.getPersonMap().putIfAbsent(personSecond, atomicIntegerSecond);
        repository.getPersonMap().putIfAbsent(personThird, atomicIntegerThird);

        Map.Entry<String, AtomicInteger> entry = new AbstractMap.SimpleEntry<>(personThird.name(), atomicIntegerThird);

        assertEquals(Optional.of(entry), repository.getPopularPerson());
    }

    @Test
    @DisplayName("Getting the popular user. Unfavorable outcome")
    public void getPopularPersonSecondTest() {
        Person personFirst = new Person("Иван", 99);
        Person personSecond = new Person("Слава", 33);
        Person personThird = new Person("Anton", 65);
        AtomicInteger atomicIntegerFirst = new AtomicInteger(1);
        AtomicInteger atomicIntegerSecond = new AtomicInteger(12);
        AtomicInteger atomicIntegerThird = new AtomicInteger(33);
        repository.getPersonMap().putIfAbsent(personFirst, atomicIntegerFirst);
        repository.getPersonMap().putIfAbsent(personSecond, atomicIntegerSecond);
        repository.getPersonMap().putIfAbsent(personThird, atomicIntegerThird);

        Map.Entry<String, AtomicInteger> entry = new AbstractMap.SimpleEntry<>(personFirst.name(), atomicIntegerFirst);

        assertNotEquals(Optional.of(entry), repository.getPopularPerson());
    }

    @Test
    @DisplayName("Method for checking parsing a file into a Map. Favorable outcome")
    public void queryFileFirstTest() {
        Person personFirst = new Person("Иван", 23);
        AtomicInteger atomicInteger = new AtomicInteger(3);
        when(fileParser.read(any())).thenReturn(Map.of(personFirst, atomicInteger));
        assertEquals(Map.of(personFirst, atomicInteger), repository.queryFile());
    }

    @Test
    @DisplayName("Method for checking parsing a file into a Map. Unfavorable outcome")
    public void queryFileSecondTest() {
        Person personFirst = new Person("Иван", 23);
        Person personSecond = new Person("Павел", 67);
        AtomicInteger atomicInteger = new AtomicInteger(3);
        when(fileParser.read(any())).thenReturn(Map.of(personFirst, atomicInteger));
        assertNotEquals(Map.of(personSecond, atomicInteger), repository.queryFile());
    }
}
