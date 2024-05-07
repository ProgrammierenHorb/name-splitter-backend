package com.example.namesplitter.parser;

import com.example.namesplitter.exception.NameSplitterException;
import com.example.namesplitter.exception.NoLastNameGivenException;
import com.example.namesplitter.model.Gender;
import com.example.namesplitter.model.Position;
import com.example.namesplitter.model.StructuredName;
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

public class NameParser implements Parser {

    private final TitleStorageService titleStorage = new InMemoryTitleStorage();
    private final SalutationStorageService salutationStorage = new InMemorySalutationService();
    private final NameGenderService nameGenderService = new SQLiteNameGenderService();
    private final PatronymicsService patronymicsService = new InMemoryPatronymicsStorage();

    @Override
    public Pair<StructuredName, List<? extends NameSplitterException>> parse(String input) {
        String inputBackup = input;

        List<NameSplitterException> errors = new ArrayList<>();

        String firstName = "";
        String lastName = "";

        Pair<Gender, String> genderParseResult;

        genderParseResult = parseGender(input);

        Gender gender = genderParseResult.getLeft();
        input = genderParseResult.getRight();

        List<String> titles = new ArrayList<>();
        Pair<String, String> title;
        while ((title = parseTitle(input)).getLeft() != null) {
            titles.add(title.getLeft());
            input = title.getRight();
        }

        Pair<String, String> name = parseName(input);

        firstName = name.getLeft();
        lastName = name.getRight();

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
        return new ImmutablePair<>(new StructuredName(gender, titles, firstName, lastName, null), errors);
    }

    private Pair<String, String> parseTitle(String input) {
        String longestMatch = null;
        String longestTitle = null;

        for (var s : titleStorage.getAllTitles().entrySet()) {
            Pattern pattern = Pattern.compile(s.getKey(), Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                String match = matcher.group();
                if (longestMatch == null || match.length() > longestMatch.length()) {
                    longestMatch = match;
                    longestTitle = s.getValue();
                }
            }
        }

        if (longestMatch != null) {
            input = input.replaceFirst(longestMatch, "");
            return new ImmutablePair<>(longestTitle, input);
        }

        return new ImmutablePair<>(null, input);
    }

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

    /**
     * This method is used to parse a name from a given input string.
     * The input string is first trimmed of any leading or trailing white spaces.
     * If the input string is empty after trimming, the method returns a pair of null values.
     * If the input string contains a comma, it is assumed that the string is in the format "Lastname, Firstname".
     * The string is split at the comma and the parts are returned as a pair in the order "Firstname, Lastname".
     * If the input string does not contain a comma, it is split into parts at the white spaces.
     * The method then checks each part against a list of patronymics.
     * If a part is found in the list of patronymics, it is assumed that this part and all following parts make up the last name.
     * If no part is found in the list of patronymics, it is assumed that the last part is the last name and all preceding parts make up the first name.
     * The method returns a pair of strings in the order "Firstname, Lastname".
     *
     * @param input The name string to be parsed.
     * @return A Pair object containing a first name string and a last name string.
     */
    private Pair<String, String> parseName(String input) throws NameSplitterException {

        input = input.trim();

        //if input is empty, return null
        if (input.isEmpty()) {
            throw new NoLastNameGivenException(new Position(0, 0));
        }

        if (input.contains(",")) {
            String[] parts = input.split(",");

            Position patronymsPos = getPositionOfPatronyms(parts[0]);

            if(patronymsPos.start() == -1) return new ImmutablePair<>(parts[1].trim(), parts[0].trim().replace(" ", "-"));

            String patronym = parts[0].substring(patronymsPos.start(), patronymsPos.end()).toLowerCase();
            var trimmedLastName = parts[0].substring(patronymsPos.end() + 1).trim();
            //prettify name such that i.e. mÜllEr-MaIer is formatted to Müller-Maier
            String lastName = WordUtils.capitalize(trimmedLastName.replace("-", " ").toLowerCase()).replaceAll(" +", "-");

            return new ImmutablePair<>(parts[1].trim(), patronym + " " + lastName);
        }

        Position patronymsPos = getPositionOfPatronyms(input);
        //no patronymic found
        if (patronymsPos.start() == -1) {
            var parts = input.split(" ");
            if (parts.length == 1) return new ImmutablePair<>(null, input.trim());
            else {
                return new ImmutablePair<>(String.join(" ", Arrays.stream(parts).toList().subList(0, parts.length - 1)), parts[parts.length - 1]);
            }
        }

        String patronymic = input.substring(patronymsPos.start(), patronymsPos.end()).toLowerCase();
        String lastName = WordUtils.capitalize(input.substring(patronymsPos.end() + 1).replace("-", " ").toLowerCase());
        String firstName = WordUtils.capitalize(input.substring(0, patronymsPos.start() - 1).toLowerCase());

        //format double last names with a hyphen
        lastName = lastName.replace(' ', '-');

        if (lastName.isEmpty())
            throw new NoLastNameGivenException(new Position(patronymsPos.end() + 1, patronymsPos.end() + 1));
        if (firstName.isEmpty()) return new ImmutablePair<>(null, lastName);

        return new ImmutablePair<>(firstName, patronymic + " " + lastName);
    }

    /**
     * This method is used to find the position of patronymics in a given input string.
     * Patronymics are parts of a name that indicate lineage and are often used in many cultures.
     * Examples of patronymics include "van", "von", "de" etc.
     * The method first retrieves a list of all patronymics from the patronymicsService.
     * It then iterates over each patronymic and checks if it is present in the input string.
     * If a patronymic is found in the input string, the method checks if its position is earlier than the current earliest position.
     * If it is, the method updates the earliest position and the end position to the current patronymic's position and length respectively.
     * If two patronymics start at the same position, the method chooses the longer one.
     * After checking all patronymics, the method returns a pair of integers representing the start and end position of the earliest patronymic found.
     * If no patronymic is found, the method returns a pair of -1.
     *
     * @param input The string in which to find the position of patronymics.
     * @return A Pair object containing the start and end position of the earliest patronymic found.
     */
    private Position getPositionOfPatronyms(String input) {
        List<String> patronymics = patronymicsService.getAllPatronymics();
        int earliestLastNameStartIndex = Integer.MAX_VALUE;
        int earliestLastNameEndIndex = Integer.MAX_VALUE;
        int lastNameStartIndex;

        //search for patronymics (i.e. "van", "von", "de" etc.) in the input string
        for (String patronymic : patronymics) {
            lastNameStartIndex = input.toLowerCase().indexOf(patronymic + " ");
            //the first patronymic found is taken as the beginning of the last name
            if (lastNameStartIndex == -1) continue;
            if (lastNameStartIndex < earliestLastNameStartIndex) {
                earliestLastNameStartIndex = lastNameStartIndex;
                earliestLastNameEndIndex = lastNameStartIndex + patronymic.length();
            }
            //if two patronymics are found that start at the same position, the longer one is taken
            //i.e. "van den" ist taken over "van"
            else if (lastNameStartIndex <= earliestLastNameStartIndex && lastNameStartIndex + patronymic.length() > earliestLastNameEndIndex) {
                earliestLastNameEndIndex = lastNameStartIndex + patronymic.length();
            }
        }
        if (earliestLastNameStartIndex == Integer.MAX_VALUE) {
            return new Position(-1, -1);
        }
        return new Position(earliestLastNameStartIndex, earliestLastNameEndIndex);
    }
}