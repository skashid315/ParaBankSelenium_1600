package com.parabank.utils;

import java.time.Instant;
import java.util.Random;

public class DataGenerator {
    private static final Random random = new Random();
    
    public static String generateUniqueUsername() {
        long timestamp = Instant.now().getEpochSecond();
        int randomNum = random.nextInt(1000);
        return "user" + timestamp + randomNum;
    }
    
    public static String generateUniquePassword() {
        long timestamp = Instant.now().getEpochSecond();
        int randomNum = random.nextInt(1000);
        return "Pass@" + timestamp + randomNum;
    }
    
    public static String generateFirstName() {
        String[] firstNames = {"John", "Jane", "Michael", "Sarah", "David", "Emily", "Robert", "Lisa"};
        return firstNames[random.nextInt(firstNames.length)];
    }
    
    public static String generateLastName() {
        String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis"};
        return lastNames[random.nextInt(lastNames.length)];
    }
    
    public static String generateStreetAddress() {
        int streetNum = random.nextInt(9999) + 1;
        String[] streets = {"Main St", "Oak Ave", "Park Rd", "Elm St", "Maple Dr", "Washington Blvd"};
        return streetNum + " " + streets[random.nextInt(streets.length)];
    }
    
    public static String generateCity() {
        String[] cities = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia", "San Antonio"};
        return cities[random.nextInt(cities.length)];
    }
    
    public static String generateState() {
        String[] states = {"NY", "CA", "IL", "TX", "AZ", "PA", "FL", "OH"};
        return states[random.nextInt(states.length)];
    }
    
    public static String generateZipCode() {
        return String.format("%05d", random.nextInt(100000));
    }
    
    public static String generatePhoneNumber() {
        return String.format("%03d-%03d-%04d", 
            random.nextInt(1000), 
            random.nextInt(1000), 
            random.nextInt(10000));
    }
    
    public static String generateSSN() {
        return String.format("%03d-%02d-%04d", 
            random.nextInt(1000), 
            random.nextInt(100), 
            random.nextInt(10000));
    }
}
