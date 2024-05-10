package com.example.namesplitter.storage;

import com.example.namesplitter.model.Gender;
import com.example.namesplitter.storage.interfaces.SalutationStorageService;

import java.util.HashMap;
import java.util.Map;

/**
 * The InMemorySalutationService class implements the SalutationStorageService interface and provides an in-memory storage for salutations.
 * It uses a HashMap to store the salutations and provides a method to get all salutations.
 * It follows the Singleton design pattern to ensure that only one instance of this class is created.
 */
public class InMemorySalutationService implements SalutationStorageService {

    /**
     * A static instance of this class. It is used to implement the Singleton design pattern.
     */
    static InMemorySalutationService instance;

    /**
     * A HashMap to store the salutations. It is initialized with a map of predefined salutations.
     */
    Map<String, Gender> salutations = new HashMap<>(Map.of(
            "herr", Gender.MALE,
            "frau", Gender.FEMALE,
            "mme.", Gender. FEMALE,
            "mrs.", Gender.FEMALE
    ));

    /**
     * Private constructor to prevent instantiation of this class directly.
     * It calls the super constructor implicitly.
     */
    private InMemorySalutationService(){
        super();
    }

    /**
     * This method returns the single instance of this class.
     * If the instance is null, it creates a new instance of this class and returns it.
     * Otherwise, it returns the existing instance.
     *
     * @return The single instance of this class.
     */
    public static InMemorySalutationService getInstance() {
        return instance == null ? instance = new InMemorySalutationService() : instance;
    }

    /**
     * This method returns a copy of all salutations stored in the HashMap.
     * It overrides the getAllSalutations method of the SalutationStorageService interface.
     *
     * @return A copy of the map of all salutations.
     */
    @Override
    public Map<String, Gender> getAllSalutations() {
        return Map.copyOf(salutations);
    }
}