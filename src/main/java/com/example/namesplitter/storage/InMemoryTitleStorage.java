package com.example.namesplitter.storage;

import com.example.namesplitter.storage.interfaces.TitleStorageService;

import java.util.*;

public class InMemoryTitleStorage implements TitleStorageService {

    public static Map<String, String> titles = new HashMap<>(Map.ofEntries(
            Map.entry("Dr\\.?\\s*rer\\.?\\s*nat\\.?\\s", "Dr.rer.nat."),
            Map.entry("Dr\\.?\\s*med\\.?\\s", "Dr.med."),
            Map.entry("Dr\\.?\\s*phil\\.?\\s", "Dr.phil."),
            Map.entry("Dr\\.?\\s*rer\\.?\\s*pol\\.?\\s", "Dr.rer.pol."),
            Map.entry("Dr\\.?\\s*oec\\.?\\s", "Dr.oec."),
            Map.entry("Dr\\.?\\s*h\\.?\\s*c\\.?\\s*mult\\.?\\s", "Dr.h.c.mult."),
            Map.entry("Dr\\.?-Ing\\.?\\s", "Dr.-Ing."),
            Map.entry("Prof\\.?\\s", "Prof."),
            Map.entry("Professor\\s", "Prof."),
            Map.entry("Dr\\.?\\s", "Dr."),
            Map.entry("Doktor\\s", "Dr."),
            Map.entry("B\\.?\\s*A\\.?\\s", "B.A."),
            Map.entry("B\\.?\\s*Sc\\.?\\s", "B.Sc."),
            Map.entry("B\\.?\\s*Eng\\.?\\s", "B.Eng."),
            Map.entry("M\\.?\\s*A\\.?\\s", "M.A."),
            Map.entry("M\\.?\\s*Sc\\.?\\s", "M.Sc."),
            Map.entry("M\\.?\\s*Eng\\.?\\s", "M.Eng."),
            Map.entry("Dipl\\.?-?\\s*Ing\\.?\\s", "Dipl.-Ing."),
            Map.entry("Dipl\\.?-?\\s*Inf\\.?\\s", "Dipl.-Inf."),
            Map.entry("Dr\\.?\\s*habil\\.?\\s", "Dr.habil.")
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
