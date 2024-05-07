package com.example.namesplitter.parser;

import com.example.namesplitter.exception.InvalidCharacterException;
import com.example.namesplitter.exception.NameSplitterException;
import com.example.namesplitter.exception.NoLastNameGivenException;
import com.example.namesplitter.model.*;
import com.example.namesplitter.storage.*;
import com.example.namesplitter.storage.interfaces.NameGenderService;
import com.example.namesplitter.storage.interfaces.PatronymicsService;
import com.example.namesplitter.storage.interfaces.SalutationStorageService;
import com.example.namesplitter.storage.interfaces.TitleStorageService;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**🐒🐒🐒🐒
 * The Parser class implements the IParser interface and provides methods to parse a name string into a structured name.
 * It uses services to retrieve titles, salutations, genders, and patronymics.
 * The parse method is the main method that orchestrates the parsing process.
 */
public class Parser implements IParser {

    // Services used for retrieving titles, salutations, genders, and patronymics
    private final TitleStorageService titleStorage = InMemoryTitleStorage.getInstance();
    private final SalutationStorageService salutationStorage = new InMemorySalutationService();
    private final NameGenderService nameGenderService = new SQLiteNameGenderService();
    private final NameParser nameParser = new NameParser();

    /**
     * The parse method takes an input string and parses it into a structured name.
     * It first parses the gender, then the titles, and finally the name.
     * If no last name is found, it adds an error to the list of errors.
     * If no gender is found, it tries to guess the gender using the first name.
     * It returns a pair containing the structured name and a list of errors.
     *
     * @param input The input string to be parsed.
     * @return A Pair object containing the structured name and a list of errors.
     */
    @Override
    public Pair<StructuredName, List<? extends NameSplitterException>> parse(String input) {

        String inputBackup = input;

        List<NameSplitterException> errors = new ArrayList<>();

        //forbid special characters except . , - and whitespace
        String allowedSymbols = "^[\\p{L}\\p{M}\\p{Z}.,\\s-]+$";
        Pattern pattern = Pattern.compile(allowedSymbols);
        Matcher matcher = pattern.matcher(input);
        if(!matcher.matches()) {
            Matcher invalidCharMatcher = Pattern.compile("[^\\p{L}\\p{M}\\p{Z}.,\\s-]").matcher(input);
            while (invalidCharMatcher.find()) {
                errors.add(new InvalidCharacterException(new Position(invalidCharMatcher.start(), invalidCharMatcher.end() - 1)));
            }
            return new ImmutablePair<>(new StructuredName(null, null, null, null, null), errors);
        }

        String firstName;
        String lastName;

        Pair<Gender, String> genderParseResult;

        genderParseResult = parseGender(input);

        Gender gender = genderParseResult.getLeft();
        input = genderParseResult.getRight();

        List<TitleData> titles = new ArrayList<>();
        Pair<TitleData, String> title;
        while ((title = parseTitle(input)).getLeft() != null) {
            titles.add(title.getLeft());
            input = title.getRight();
        }

        CompleteName completeName;

        try{
            completeName = nameParser.parseName(input);
        }
        catch(NameSplitterException e){
            //relative position to absolute position
            int startPosOfName = inputBackup.indexOf(input);
            e.setPosition(new Position(startPosOfName + e.getStartPos(), startPosOfName + e.getEndPos()));
            errors.add(e);
            return new ImmutablePair<>(new StructuredName(null, null, null, null, null), errors);
        }

        firstName = completeName.firstName();
        lastName = completeName.lastName();

        if (lastName == null || lastName.isEmpty()) {
            errors.add(new NoLastNameGivenException(new Position(inputBackup.length() - 1, inputBackup.length() - 1)));
            return new ImmutablePair<>(new StructuredName(null, null, null, null, null), errors);
        }

        //if no gender is found yet, try guessing with first name from name database
        if (gender == null && firstName != null) {
            firstName = firstName.trim();
            Gender potentialGender = nameGenderService.getGender(firstName.split(" ")[0]);
            if (potentialGender != null) {
                gender = potentialGender;
            }
        }
        return new ImmutablePair<>(new StructuredName(gender, titles.stream().sorted().map(TitleData::name).toList(), firstName, lastName, null), errors);
    }

    /**
     * The parseTitle method takes an input string and tries to parse a title from it.
     * It finds the longest matching title in the input string and removes it from the string.
     * It returns a pair containing the title and the remaining input string.
     *
     * @param input The input string from which to parse a title.
     * @return A Pair object containing the title and the remaining input string.
     */
    private Pair<TitleData, String> parseTitle(String input) {
        String longestMatch = null;
        TitleData longestTitle = null;

        for (var s : titleStorage.getAllAcademicTitles()) {
            Pattern pattern = Pattern.compile(s.regex(), Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                String match = matcher.group();
                if (longestMatch == null || match.length() > longestMatch.length()) {
                    longestMatch = match;
                    longestTitle = s;
                }
            }
        }
        if (longestMatch != null) {
            input = input.replaceFirst(longestMatch, "");
            return new ImmutablePair<>(longestTitle, input);
        }

        return new ImmutablePair<>(null, input);
    }

    /**
     * The parseGender method takes an input string and tries to parse a gender from it.
     * It finds the first matching salutation in the input string and removes it from the string.
     * It returns a pair containing the gender and the remaining input string.
     *
     * @param input The input string from which to parse a gender.
     * @return A Pair object containing the gender and the remaining input string.
     */
    private Pair<Gender, String> parseGender(String input) {
        for (var s : salutationStorage.getAllSalutations().entrySet()) {
            Pattern pattern = Pattern.compile("^" + s.getKey(), Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);//Gender needs to be at the beginning of the string
            if (matcher.find()) {
                input = input.replaceFirst(s.getKey(), "");
                return new ImmutablePair<>(s.getValue(), input);
            }
        }
        return new ImmutablePair<>(null, input);
    }

}