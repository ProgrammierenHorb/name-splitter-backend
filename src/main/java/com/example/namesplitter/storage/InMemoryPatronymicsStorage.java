package com.example.namesplitter.storage;

import com.example.namesplitter.storage.interfaces.PatronymicsService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InMemoryPatronymicsStorage implements PatronymicsService {

    private InMemoryPatronymicsStorage() {
        super();
    }

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

    static InMemoryPatronymicsStorage instance;

    @Override
    public List<String> getAllPatronymics() {
        return patronymics.stream().toList();
    }

    public static InMemoryPatronymicsStorage getInstance() {
        return instance == null ? instance = new InMemoryPatronymicsStorage() : instance;
    }
}