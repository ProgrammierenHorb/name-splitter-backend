package com.example.namesplitter.storage;

import com.example.namesplitter.storage.interfaces.PatronymicsService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The InMemoryPatronymicsStorage class implements the PatronymicsService interface and provides an in-memory storage for patronymics.
 * It uses a HashSet to store the patronymics and provides a method to get all patronymics.
 * It follows the Singleton design pattern to ensure that only one instance of this class is created.
 */
public class InMemoryPatronymicsStorage implements PatronymicsService {

    /**
     * Private constructor to prevent instantiation of this class directly.
     * It calls the super constructor implicitly.
     */
    private InMemoryPatronymicsStorage() {
        super();
    }

    /**
     * A HashSet to store the patronymics. It is initialized with a set of predefined patronymics.
     */
    private final Set<String> patronymics = new HashSet<String>(Set.of(
            "van",
            "de",
            "den",
            "der",
            "het",
            "ten",
            "ter",
            "uit",
            "van den",
            "van der",
            "von und zu",
            "von",
            "zu",
            "vom",
            "y"
    ));

    /**
     * A static instance of this class. It is used to implement the Singleton design pattern.
     */
    static InMemoryPatronymicsStorage instance;

    /**
     * This method returns a list of all patronymics stored in the HashSet.
     * It overrides the getAllPatronymics method of the PatronymicsService interface.
     *
     * @return A list of all patronymics.
     */
    @Override
    public List<String> getAllPatronymics() {
        return patronymics.stream().toList();
    }

    /**
     * This method returns the single instance of this class.
     * If the instance is null, it creates a new instance of this class and returns it.
     * Otherwise, it returns the existing instance.
     *
     * @return The single instance of this class.
     */
    public static InMemoryPatronymicsStorage getInstance() {
        return instance == null ? instance = new InMemoryPatronymicsStorage() : instance;
    }
}