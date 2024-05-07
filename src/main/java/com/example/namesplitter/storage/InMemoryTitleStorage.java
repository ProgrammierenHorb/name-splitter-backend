package com.example.namesplitter.storage;

import com.example.namesplitter.model.Gender;
import com.example.namesplitter.storage.interfaces.TitleStorageService;
import com.example.namesplitter.model.TitleData;

import java.util.*;

public class InMemoryTitleStorage implements TitleStorageService {

    public static List<TitleData> titles = new ArrayList<>(List.of(
            new TitleData("Dr[\\.|\\s*]rer[\\.|\\s*]nat[\\.|\\s]?", "Dr.rer.nat.", null, 3),
            new TitleData("Dr\\.?\\s*med\\.?\\s", "Dr.med.", null, 3),
            new TitleData("Dr\\.?\\s*phil\\.?\\s", "Dr.phil.", null, 3),
            new TitleData("Dr\\.?\\s*rer\\.?\\s*pol\\.?\\s", "Dr.rer.pol.", null, 3),
            new TitleData("Dr\\.?\\s*oec\\.?\\s", "Dr.oec.", null, 3),
            new TitleData("Dr\\.?\\s*h\\.?\\s*c\\.?\\s*mult\\.?\\s", "Dr.h.c.mult.", null, 4),
            new TitleData("Dr\\.?\\s*h\\.?\\s*c\\.?\\s", "Dr.h.c.", null, 3),
            new TitleData("Dr\\.?-Ing\\.?\\s", "Dr.-Ing.", null, 3),
            new TitleData("Prof\\.?\\s", "Prof.", null, 1),
            new TitleData("Professor\\s?", "Prof.", Gender.MALE, 2),
            new TitleData("Dr\\.?\\s", "Dr.", null, 3),
            new TitleData("Doktor\\s?", "Dr.", Gender.MALE, 3),
            new TitleData("B\\.?\\s*A\\.?\\s", "B.A.", null, 6),
            new TitleData("B\\.?\\s*Sc\\.?\\s", "B.Sc.", null, 6),
            new TitleData("B\\.?\\s*Eng\\.?\\s", "B.Eng.", null, 6),
            new TitleData("M\\.?\\s*A\\.?\\s", "M.A.", null, 5),
            new TitleData("M\\.?\\s*Sc\\.?\\s", "M.Sc.", null, 5),
            new TitleData("M\\.?\\s*Eng\\.?\\s", "M.Eng.", null, 5),
            new TitleData("Dipl\\.?-?\\s*Ing\\.?\\s", "Dipl.-Ing.", null, 5),
            new TitleData("Dipl\\.?-?\\s*Inf\\.?\\s", "Dipl.-Inf.", null, 5),
            new TitleData("Dr\\.?\\s*habil\\.?\\s", "Dr.habil.", null, 2)
    ));

    @Override
    public List<TitleData> getAllTitles() {
        return List.copyOf(titles);
    }

    @Override
    public boolean addTitle(String title, String regex) {
        if(titles.contains(title)) {
            return false;
        }
        titles.add(new TitleData(regex, title, null, 100));
        return true;
    }

    @Override
    public boolean removeTitle(String regex) {
        return titles.remove(regex);
    }
}
