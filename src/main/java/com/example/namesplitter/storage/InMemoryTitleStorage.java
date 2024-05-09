package com.example.namesplitter.storage;

import com.example.namesplitter.model.Gender;
import com.example.namesplitter.storage.interfaces.TitleStorageService;
import com.example.namesplitter.model.TitleData;

import java.util.*;

public class InMemoryTitleStorage implements TitleStorageService {

    private InMemoryTitleStorage(){
        super();
    }

    private static TitleStorageService instance;

    public List<TitleData> academicTitles = new ArrayList<>(List.of(
            new TitleData("Dr\\.?\\s*rer\\.?\\s*nat[.\\s*|\\s+]", "Dr.rer.nat.", null, 3),
            new TitleData("Dr\\.?\\s*med\\.?\\s", "Dr.med.", null, 3),
            new TitleData("Dr\\.?\\s*phil\\.?\\s", "Dr.phil.", null, 3),
            new TitleData("Dr\\.?\\s*rer\\.?\\s*pol\\.?\\s", "Dr.rer.pol.", null, 3),
            new TitleData("Dr\\.?\\s*oec\\.?\\s", "Dr.oec.", null, 3),
            new TitleData("Dr\\.?\\s*h\\.?\\s*c\\.?\\s*mult\\.?\\s", "Dr.h.c.mult.", null, 4),
            new TitleData("Dr\\.?\\s*h\\.?\\s*c\\.?\\s", "Dr.h.c.", null, 3),
            new TitleData("Dr\\.?-Ing\\.?\\s", "Dr.-Ing.", null, 3),
            new TitleData("Prof\\.?\\s", "Prof.", null, 1),
            new TitleData("Professor\\s?", "Prof.", Gender.MALE, 2),
            new TitleData("Professorin\\s?", "Prof.", Gender.FEMALE, 2),
            new TitleData("Dr\\.?\\s", "Dr.", null, 3),
            new TitleData("Doktor\\s?", "Dr.", Gender.MALE, 3),
            new TitleData("Doktorin\\s?", "Dr.", Gender.FEMALE, 3),
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

    //https://de.wikipedia.org/wiki/Adelstitel
    public List<TitleData> titleOfNobility = new ArrayList<>(List.of(
            new TitleData("Kaiser|Zar", "Kaiser", Gender.MALE, Integer.MAX_VALUE),
            new TitleData("Kaiserin|Zariza", "Kaiserin", Gender.FEMALE, Integer.MAX_VALUE),
            new TitleData("König", "König", Gender.MALE, Integer.MAX_VALUE),
            new TitleData("Königin", "Königin", Gender.FEMALE, Integer.MAX_VALUE),
            new TitleData("Erzherzog", "Erzherzog", Gender.MALE, Integer.MAX_VALUE),
            new TitleData("Erzherzogin", "Erzherzogin", Gender.FEMALE, Integer.MAX_VALUE),
            new TitleData("Großherzog", "Großherzog", Gender.MALE, Integer.MAX_VALUE),
            new TitleData("Großherzogin", "Großherzogin", Gender.FEMALE, Integer.MAX_VALUE),
            new TitleData("Kurfürst", "Kurfürst", Gender.MALE, Integer.MAX_VALUE),
            new TitleData("Kurfürstin", "Kurfürstin", Gender.FEMALE, Integer.MAX_VALUE),
            new TitleData("Herzog", "Herzog", Gender.MALE, Integer.MAX_VALUE),
            new TitleData("Herzogin", "Herzogin", Gender.FEMALE, Integer.MAX_VALUE),
            new TitleData("Landgraf", "Landgraf", Gender.MALE, Integer.MAX_VALUE),
            new TitleData("Landgräfin", "Landgräfin", Gender.FEMALE, Integer.MAX_VALUE),
            new TitleData("Pfalzgraf", "Pfalzgraf", Gender.MALE, Integer.MAX_VALUE),
            new TitleData("Pfalzgräfin", "Pfalzgräfin", Gender.FEMALE, Integer.MAX_VALUE),
            new TitleData("Markgraf", "Markgraf", Gender.MALE, Integer.MAX_VALUE),
            new TitleData("Markgräfin", "Markgräfin", Gender.FEMALE, Integer.MAX_VALUE),
            new TitleData("Fürst", "Fürst", Gender.MALE, Integer.MAX_VALUE),
            new TitleData("Fürstin", "Fürstin", Gender.FEMALE, Integer.MAX_VALUE),
            new TitleData("Graf", "Graf", Gender.MALE, Integer.MAX_VALUE),
            new TitleData("Gräfin", "Gräfin", Gender.FEMALE, Integer.MAX_VALUE),
            new TitleData("Freiherr", "Freiherr", Gender.MALE, Integer.MAX_VALUE),
            new TitleData("Baron", "Baron", Gender.MALE, Integer.MAX_VALUE),
            new TitleData("Freifrau", "Freifrau", Gender.FEMALE, Integer.MAX_VALUE),
            new TitleData("Baronin", "Baronin", Gender.FEMALE, Integer.MAX_VALUE)
    ));

    @Override
    public List<TitleData> getAllAcademicTitles() {
        return academicTitles;
    }

    @Override
    public List<TitleData> getAllNobilityTitles() {
        return titleOfNobility;
    }

    @Override
    public boolean addTitle(String title, String regex) {
        //if the element is already in the list, return false
        if(academicTitles.stream().anyMatch(t -> t.regex().equals(regex) && t.name().equals(title))){
            return false;
        }
        academicTitles.add(new TitleData(regex, title, null, 100));
        return true;
    }

    @Override
    public boolean removeTitle(TitleData title) {
        return academicTitles.remove(title);
    }

    public static TitleStorageService getInstance() {
        return instance == null ? instance = new InMemoryTitleStorage() : instance;
    }
}
