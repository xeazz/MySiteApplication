package com.example.mysite.repository;

import com.example.mysite.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
public class FileParserTest {
    FileParser fileParser;

    public FileParserTest() {
        this.fileParser = new FileParser();
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
    @DisplayName("Checking the correctness of reading from a file in Map (the case when the contents of the file are known)")
    public void checkingReadFileFirstTest() {
        Map<Person, AtomicInteger> person = new ConcurrentHashMap<>();
        String mapEntity = "{Person[name=Дима, age=6]=0}";
        assertEquals(mapEntity, fileParser.read(person).toString());
    }

    @Test
    @DisplayName("Checking the correctness of reading from a file in Map")
    public void checkingReadFileSecondTest() {
        Map<Person, AtomicInteger> person = new ConcurrentHashMap<>();
        String mapEntity = "{Person[name=Саша, age=32]=0, Person[name=Иван, age=42]=0}";
        assertNotEquals(mapEntity, fileParser.read(person).toString());
    }
}
