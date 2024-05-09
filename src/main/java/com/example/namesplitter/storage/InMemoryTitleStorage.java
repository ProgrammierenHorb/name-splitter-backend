package com.example.namesplitter.storage;

import com.example.namesplitter.model.Gender;
import com.example.namesplitter.model.TitleDTO;
import com.example.namesplitter.storage.interfaces.TitleStorageService;
import com.example.namesplitter.model.TitleData;

import java.util.*;

public class InMemoryTitleStorage implements TitleStorageService {

    private InMemoryTitleStorage(){
        super();
    }

    private static TitleStorageService instance;

    public List<TitleData> academicTitles = new ArrayList<>(List.of(
            new TitleData("Prof\\.?\\s", "Prof.", null, 10),
            new TitleData("Professor\\s?", "Prof.", Gender.MALE, 10),
            new TitleData("Professorin\\s?", "Prof.", Gender.FEMALE, 10),
            new TitleData("Dr\\.?\\s*habil\\.?\\s", "Dr.habil.", null, 20),
            new TitleData("Dr\\.?\\s*rer\\.?\\s*nat[.\\s*|\\s+]", "Dr.rer.nat.", null, 30),
            new TitleData("Dr\\.?\\s*med\\.?\\s", "Dr.med.", null, 40),
            new TitleData("Dr\\.?\\s*phil\\.?\\s", "Dr.phil.", null, 50),
            new TitleData("Dr\\.?\\s*rer\\.?\\s*pol\\.?\\s", "Dr.rer.pol.", null, 60),
            new TitleData("Dr\\.?\\s*oec\\.?\\s", "Dr.oec.", null, 70),
            new TitleData("Dr\\.?-Ing\\.?\\s", "Dr.-Ing.", null, 80),
            new TitleData("Dr\\.?\\s", "Dr.", null, 90),
            new TitleData("Doktor\\s?", "Dr.", Gender.MALE, 90),
            new TitleData("Doktorin\\s?", "Dr.", Gender.FEMALE, 90),
            new TitleData("Dr\\.?\\s*h\\.?\\s*c\\.?\\s*mult\\.?\\s", "Dr.h.c.mult.", null, 100),
            new TitleData("Dr\\.?\\s*h\\.?\\s*c\\.?\\s", "Dr.h.c.", null, 110),
            new TitleData("M\\.?\\s*A\\.?\\s", "M.A.", null, 120),
            new TitleData("M\\.?\\s*Sc\\.?\\s", "M.Sc.", null, 130),
            new TitleData("M\\.?\\s*Eng\\.?\\s", "M.Eng.", null, 140),
            new TitleData("Dipl\\.?-?\\s*Ing\\.?\\s", "Dipl.-Ing.", null, 150),
            new TitleData("Dipl\\.?-?\\s*Inf\\.?\\s", "Dipl.-Inf.", null, 160),
            new TitleData("B\\.?\\s*A\\.?\\s", "B.A.", null, 170),
            new TitleData("B\\.?\\s*Sc\\.?\\s", "B.Sc.", null, 180),
            new TitleData("B\\.?\\s*Eng\\.?\\s", "B.Eng.", null, 190)
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
    public boolean addTitle(TitleData title) {

        //if the regex is null, the user didn't want to use regex, hence just take the title as the regex and match it exactly
        if(title.regex() == null){
            //use exact string matching
            title = new TitleData(title, "\\Q" + title.name() + "\\E");
        }

        //if the element is already in the list, return false
        TitleData finalTitle = title;
        if(academicTitles.stream().anyMatch(t -> t.regex().equals(finalTitle.regex()))){
            return false;
        }
        academicTitles.add(finalTitle);
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
