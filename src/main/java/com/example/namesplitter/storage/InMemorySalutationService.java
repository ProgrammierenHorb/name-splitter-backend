package com.example.namesplitter.storage;

import com.example.namesplitter.model.Gender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemorySalutationService implements SalutationStorageService {

    @Override
    public Map<String, Gender> getAllSalutations() {
        return new HashMap<>(Map.of(
                "Herr", Gender.MALE,
                "Frau", Gender.FEMALE
        ));
    }
}
