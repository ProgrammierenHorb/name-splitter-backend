package com.example.namesplitter.storage;

import com.example.namesplitter.storage.interfaces.TitleStorageService;

import java.util.*;

public class InMemoryTitleStorage implements TitleStorageService {

    public static Map<String, String> titles = new HashMap<String, String>(Map.ofEntries(
            Map.entry("Dr\\.?\\s*rer\\.?\\s*nat\\.?", "Dr. rer. nat."),
            Map.entry("Dr\\.?\\s*med\\.?", "Dr. med."),
            Map.entry("Dr\\.?\\s*phil\\.?", "Dr. phil."),
            Map.entry("Dr\\.?\\s*rer\\.?\\s*pol\\.?", "Dr. rer. pol."),
            Map.entry("Dr\\.?\\s*oec\\.?", "Dr. oec."),
            Map.entry("Dr\\.?\\s*h\\.?\\s*c\\.?\\s*mult\\.?", "Dr. h. c. mult."),
            Map.entry("Dr\\.?-Ing\\.?", "Dr.-Ing."),
            Map.entry("Prof\\.?", "Prof."),
            Map.entry("Professor", "Prof."),
            Map.entry("Dr\\.?", "Dr."),
            Map.entry("Doktor", "Dr."),
            Map.entry("B\\.?\\s*A\\.?", "B. A."),
            Map.entry("B\\.?\\s*Sc\\.?", "B. Sc."),
            Map.entry("B\\.?\\s*Eng\\.?", "B. Eng."),
            Map.entry("M\\.?\\s*A\\.?", "M. A."),
            Map.entry("M\\.?\\s*Sc\\.?", "M. Sc."),
            Map.entry("M\\.?\\s*Eng\\.?", "M. Eng."),
            Map.entry("Dipl\\.?\\s*Ing\\.?", "Dipl.-Ing."),
            Map.entry("Dipl\\.?\\s*Inf\\.?", "Dipl.-Inf."),
            Map.entry("Dr\\.?\\s*habil\\.?", "Dr. habil.")
    ));

    @Override
    public Map<String, String> getAllTitles() {
        return Map.copyOf(titles);
    }

    @Override
    public void addTitle(String regex, String title) {
        titles.put(regex, title);
    }

    @Override
    public void removeTitle(String regex) {
        titles.remove(regex);
    }
}
