package com.example.namesplitter.helper;

import com.example.namesplitter.model.Gender;
import com.example.namesplitter.model.StructuredName;
import com.example.namesplitter.storage.InMemoryTitleStorage;
import com.example.namesplitter.storage.interfaces.TitleStorageService;
import org.apache.logging.log4j.util.Strings;

import com.example.namesplitter.model.TitleData;

import java.util.List;

/**
 * The StandardizedSalutationGenerator class provides a static method to generate a standardized salutation for a given structured name.
 * The salutation is generated based on the gender and the presence of a first name in the structured name.
 * For male and female genders, the salutation includes the gender-specific greeting, the titles, and the last name.
 * For diverse gender, the salutation includes a gender-neutral greeting and, if a first name is given, the first name and the last name.
 */
public class StandardizedSalutationGenerator {

    /**
     * The generateStandardizedSalutation method generates a standardized salutation for a given structured name.
     * It first checks the gender of the name.
     * If the gender is male or female, it appends a gender-specific greeting, the titles, and the last name to the salutation.
     * If the gender is diverse, it checks if a first name is given.
     * If a first name is given, it appends a gender-neutral greeting, the first name, and the last name to the salutation.
     * If no first name is given, it appends a gender-neutral greeting to the salutation.
     *
     * @param name The structured name for which to generate a standardized salutation.
     * @return A string containing the standardized salutation.
     */
    public static String generateStandardizedSalutation(StructuredName name){

        TitleStorageService titleStorageService = InMemoryTitleStorage.getInstance();

        StringBuilder salutation = new StringBuilder();
        //MALE or FEMALE Gender
        if(name.gender() == Gender.MALE || name.gender() == Gender.FEMALE){
            salutation.append(name.gender() == Gender.MALE ? "Sehr geehrter Herr " : "Sehr geehrte Frau ");

            List<String> sortedTitles = List.copyOf(name.titles());
            List<TitleData> savedTitles = titleStorageService.getAllAcademicTitles();

            //sort the given titles by the priority saved in the TitleStorageService
            name.titles().sort((s1, s2) -> {
                int p1 = savedTitles.stream()
                        .filter(titleData -> titleData.name().equals(s1))
                        .findFirst()
                        .map(TitleData::priority)
                        .orElse(Integer.MAX_VALUE); // or any default value

                int p2 = savedTitles.stream()
                        .filter(titleData -> titleData.name().equals(s2))
                        .findFirst()
                        .map(TitleData::priority)
                        .orElse(Integer.MAX_VALUE); // or any default value

                return Integer.compare(p1, p2);
            });

            salutation.append(Strings.join(name.titles(), ' '));
            salutation.append(" ");
            salutation.append(name.lastName());
            return salutation.toString();
        }

        //DIVERSE Gender
        //no firstname given
        if(name.firstName() == null || name.firstName().isBlank()){
            salutation.append("Sehr geehrte Damen und Herren");
            return salutation.toString();
        }

        //firstname given
        salutation.append("Guten Tag ");
        salutation.append(name.firstName());
        salutation.append(" ");
        salutation.append(name.lastName());
        return salutation.toString();
    }
}
