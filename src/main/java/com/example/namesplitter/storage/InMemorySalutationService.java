package com.example.namesplitter.storage;

import com.example.namesplitter.model.Gender;
import com.example.namesplitter.storage.interfaces.SalutationStorageService;

import java.util.HashMap;
import java.util.Map;

public class InMemorySalutationService implements SalutationStorageService {

    static InMemorySalutationService instance;

    Map<String, Gender> salutations = new HashMap<>(Map.of(
            "herr", Gender.MALE,
            "frau", Gender.FEMALE,
            "mme.", Gender. FEMALE,
            "mrs.", Gender.FEMALE
    ));

    private InMemorySalutationService(){
        super();
    }

    public static InMemorySalutationService getInstance() {
        return instance == null ? instance = new InMemorySalutationService() : instance;
    }

    @Override
    public Map<String, Gender> getAllSalutations() {
        return Map.copyOf(salutations);
    }
}
