package com.example.namesplitter.storage;

import com.example.namesplitter.model.Gender;
import com.example.namesplitter.storage.interfaces.SalutationStorageService;

import java.util.HashMap;
import java.util.Map;

public class InMemorySalutationService implements SalutationStorageService {

    @Override
    public Map<String, Gender> getAllSalutations() {
        return new HashMap<>(Map.of(
                "Herr\\s", Gender.MALE,
                "Frau\\s", Gender.FEMALE,
                "Mme\\.?\\s", Gender. FEMALE,
                "Mrs\\.?\\s", Gender.FEMALE
        ));
    }
}
