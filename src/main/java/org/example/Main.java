package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ////////////////////////////
        Gson gson = new Gson();

        File input = new File("books.json");

        List<Person> people;
        try{
            people = gson.fromJson(new FileReader(input), new TypeToken<>(){});
        } catch (FileNotFoundException e ){
            throw new RuntimeException(e);
        }
        ////////////////////////////

        //1
        Set<String> persons = people.stream()
                .map(e -> (e.getName() + " " + e.getSurname()))
                .collect(Collectors.toSet());
        System.out.println(persons.size());
        persons.forEach(System.out::println);

        //2
        Set<Book> uniqueBooks = people.stream()
                .map(Person::getFavoriteBooks)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        System.out.println(uniqueBooks.size());
        uniqueBooks.forEach(System.out::println);

        //3
        uniqueBooks.stream()
                .sorted(Comparator.comparing(Book::getPublishingYear))
                .forEach(System.out::println);

        //4
        boolean hasJaneAustenBooks = uniqueBooks.stream()
                .anyMatch(e -> e.getAuthor().equals("Jane Austen"));
        if (hasJaneAustenBooks)
            System.out.println("Has");
        else System.out.println("Has not");
        //5
        int maxCount;
        int temp = 0;
        for (Person person : people) {
            if (person.getFavoriteBooks().size() > temp) {
                temp = person.getFavoriteBooks().size();
            }
        }
        maxCount = temp;

        System.out.println(maxCount);
        //6
        List<SMS> smsMessages = new ArrayList<>();
        double averageFavorites = people.stream()
                .mapToInt(e -> e.getFavoriteBooks().size())
                .average()
                .orElse(0);

        people.stream()
                .filter(Person::isSubscribed)
                .forEach(e -> {
                    int favoriteCount = e.getFavoriteBooks().size();
                    String message;
                    if (favoriteCount > averageFavorites) {
                        message = "you are a bookworm";
                    } else if (favoriteCount < averageFavorites) {
                        message = "read more";
                    } else {
                        message = "fine";
                    }
                    smsMessages.add(new SMS(e.getPhone(), message));
                });
        smsMessages.forEach(sms -> System.out.println("SMS to " + sms.getPhone() + ": " + sms.getMessage()));
    }
}