package com.example.mysite.repository;

import com.example.mysite.model.Person;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Getter
public class FileParser {
    private static final String FILE_ADDRESS = "person.txt";

    public Map<Person, AtomicInteger> read(Map<Person, AtomicInteger> personMap) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_ADDRESS))) {
            String text;
            while ((text = br.readLine()) != null) {
                String[] data = text.split("_");
                personMap.putIfAbsent(new Person(data[0], Integer.parseInt(data[1])), new AtomicInteger(0));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return personMap;
    }

    public void save(String name, int age) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_ADDRESS, true))) {
            bw.write(name + "_" + age);
            bw.append("\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
