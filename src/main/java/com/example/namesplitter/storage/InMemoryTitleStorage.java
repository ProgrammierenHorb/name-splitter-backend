package com.example.namesplitter.storage;

import com.example.namesplitter.model.Gender;
import com.example.namesplitter.storage.interfaces.TitleStorageService;
import com.example.namesplitter.model.TitleData;

import java.util.*;

/**
 * The InMemoryTitleStorage class implements the TitleStorageService interface and provides an in-memory storage for titles.
 * It uses ArrayLists to store academic titles and titles of nobility.
 * It follows the Singleton design pattern to ensure that only one instance of this class is created.
 */
public class InMemoryTitleStorage implements TitleStorageService {

    /**
     * Private constructor to prevent instantiation of this class directly.
     * It calls the super constructor implicitly.
     */
    private InMemoryTitleStorage(){
        super();
    }

    /**
     * A static instance of this class. It is used to implement the Singleton design pattern.
     */
    private static TitleStorageService instance;

    /**
     * An ArrayList to store the academic titles. It is initialized with a list of predefined academic titles.
     */
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

    /**
     * An ArrayList to store the titles of nobility. It is initialized with a list of predefined titles of nobility.
     * The titles are taken from <a href="https://de.wikipedia.org/wiki/Adelstitel">this wikipedia entry</a>
     */
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

    /**
     * This method returns a list of all academic titles stored in the ArrayList.
     * It overrides the getAllAcademicTitles method of the TitleStorageService interface.
     *
     * @return A list of all academic titles.
     */
    @Override
    public List<TitleData> getAllAcademicTitles() {
        return academicTitles;
    }

    /**
     * This method returns a list of all titles of nobility stored in the ArrayList.
     * It overrides the getAllNobilityTitles method of the TitleStorageService interface.
     *
     * @return A list of all titles of nobility.
     */
    @Override
    public List<TitleData> getAllNobilityTitles() {
        return titleOfNobility;
    }

    /**
     * This method adds a new title to the list of academic titles.
     * It checks if the title is already in the list before adding it.
     * It overrides the addTitle method of the TitleStorageService interface.
     *
     * @param title The title to be added.
     * @return A boolean indicating whether the title was added successfully.
     */
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

    /**
     * This method removes a title from the list of academic titles.
     * It overrides the removeTitle method of the TitleStorageService interface.
     *
     * @param title The title to be removed.
     * @return A boolean indicating whether the title was removed successfully.
     */
    @Override
    public boolean removeTitle(TitleData title) {
        return academicTitles.remove(title);
    }

    /**
     * This method returns the single instance of this class.
     * If the instance is null, it creates a new instance of this class and returns it.
     * Otherwise, it returns the existing instance.
     *
     * @return The single instance of this class.
     */
    public static TitleStorageService getInstance() {
        return instance == null ? instance = new InMemoryTitleStorage() : instance;
    }
}
