package com.example.mysite.service;

import com.example.mysite.exception.IncorrectInputDataException;
import com.example.mysite.model.Person;
import com.example.mysite.repository.SearchRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
public class SearchServiceImplTest {
    SearchServiceImpl service;
    @Mock
    SearchRepositoryImpl repository;

    public SearchServiceImplTest() {
        MockitoAnnotations.openMocks(this);
        this.service = new SearchServiceImpl(repository);
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
    @DisplayName("Getting a person's age from a name that is observed in databases")
    public void gettingAgeFromDatabase() {
        String nameInDB = "Pavel";
        int ageInDB = 23;
        String nameOnRequest = "Pavel";
        int expectedAge = 23;
        when(repository.getAge(nameInDB)).thenReturn(Optional.of(ageInDB));
        assertEquals(expectedAge, service.getAge(nameOnRequest));
    }

    @Test
    @DisplayName("Getting a person's age by auto-generation when it's not in the database")
    public void ageAutoGenerationFirstTest() {
        String nameInDB = "Pavel";
        int ageInDB = 23;
        String nameOnRequest = "Slava";
        when(repository.getAge(nameInDB)).thenReturn(Optional.of(ageInDB));
        assertNotEquals(32, service.getAge(nameOnRequest));
    }

    @Test
    @DisplayName("Checking for an throw IncorrectInputDataException when calling the getAge method when name is null")
    public void gettingAgeWhenNameIsEmpty() {
        String nameInDB = "Pavel";
        String nameOnRequest = null;
        when(repository.getAge(nameInDB)).thenThrow(IncorrectInputDataException.class);
        assertThatThrownBy(() -> service.getAge(nameOnRequest)).isInstanceOf(IncorrectInputDataException.class);
    }

    @Test
    @DisplayName("Getting the name and age of the oldest person")
    public void checkingTheOldestUser() {
        String nameInDB = "Pavel";
        int ageInDB = 23;
        when(repository.getOldPerson()).thenReturn(Optional.of(new Person(nameInDB, ageInDB)));
        assertNotNull(service.getOldUser());
    }

    @Test
    @DisplayName("Checking for an throw IncorrectInputDataException when calling the getOldestUser method when Person class is empty")
    public void checkingTheOldestUserWithException() {
        when(repository.getOldPerson()).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getOldUser()).isInstanceOf(IncorrectInputDataException.class);
    }

    @Test
    @DisplayName("Getting the name and the number of requests for it")
    public void checkingPopularPerson() {
        String nameInDB = "Pavel";
        AtomicInteger numberOfPopular = new AtomicInteger(888);
        Map.Entry<String, AtomicInteger> entry = new AbstractMap.SimpleEntry<>(nameInDB, numberOfPopular);
        when(repository.getPopularPerson()).thenReturn(Optional.of(entry));
        assertNotNull(service.getPopularPerson());
    }

    @Test
    @DisplayName("Checking for an throw IncorrectInputDataException when calling the getPopularPerson method")
    public void checkingPopularPersonWithException() {
        Map.Entry<String, AtomicInteger> entry = null;
        when(repository.getPopularPerson()).thenReturn(Optional.ofNullable(entry));
        assertThatThrownBy(() -> service.getPopularPerson()).isInstanceOf(IncorrectInputDataException.class);
    }
}
