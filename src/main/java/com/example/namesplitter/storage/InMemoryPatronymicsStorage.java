package com.example.namesplitter.storage;

import com.example.namesplitter.storage.interfaces.PatronymicsService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InMemoryPatronymicsStorage implements PatronymicsService {

    static Set<String> patronymics = new HashSet<String>(Set.of(
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
            "von",
            "zu",
            "vom"
    ));
    @Override
    public List<String> getAllPatronymics() {
        return patronymics.stream().toList();
    }
}
