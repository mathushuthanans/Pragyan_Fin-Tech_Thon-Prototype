package com.fraud.detector.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
public class TransactionGenerator {

    private static final String[] countries = {"USA", "Canada", "UK", "Germany", "France", "India"};
    private static final String[] genders = {"Male", "Female", "Other"};
    private static final String[] occupations = {"Engineer", "Teacher", "Doctor", "Artist", "Lawyer"};
    private static final String[] transactionTypes = {"Payment", "Cash Out", "Debt", "Transfer", "Bill Payment"};

    public String generateTransaction() {
        LocalDateTime timestamp = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTimestamp = timestamp.format(formatter);

        Random random = new Random();
        String country = countries[random.nextInt(countries.length)];
        String gender = genders[random.nextInt(genders.length)];
        String occupation = occupations[random.nextInt(occupations.length)];
        double amountSpent = Math.round((10 + (10000 - 10) * random.nextDouble()) * 100.0) / 100.0;
        double balance = Math.round((1000 + (50000 - 1000) * random.nextDouble()) * 100.0) / 100.0;
        String transactionType = transactionTypes[random.nextInt(transactionTypes.length)];
        int isFraud = random.nextInt(2); // 0 or 1

        return country + ", " + gender + ", " + occupation + ", " + amountSpent + ", " + balance + ", " + transactionType + ", " + isFraud;
    }
}