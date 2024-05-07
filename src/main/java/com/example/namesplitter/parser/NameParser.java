package com.example.namesplitter.parser;

import com.example.namesplitter.exception.InvalidCharacterException;
import com.example.namesplitter.exception.NameSplitterException;
import com.example.namesplitter.exception.NoLastNameGivenException;
import com.example.namesplitter.model.Gender;
import com.example.namesplitter.model.Position;
import com.example.namesplitter.model.StructuredName;
import com.example.namesplitter.model.TitleData;
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
 * The NameParser class implements the Parser interface and provides methods to parse a name string into a structured name.
 * It uses services to retrieve titles, salutations, genders, and patronymics.
 * The parse method is the main method that orchestrates the parsing process.
 */
public class NameParser implements Parser {

    // Services used for retrieving titles, salutations, genders, and patronymics
    private final TitleStorageService titleStorage = InMemoryTitleStorage.getInstance();
    private final SalutationStorageService salutationStorage = new InMemorySalutationService();
    private final NameGenderService nameGenderService = new SQLiteNameGenderService();
    private final PatronymicsService patronymicsService = new InMemoryPatronymicsStorage();

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

        //support latin, chinese, portuguese,
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

        if(!input.contains(" ")){
            input = input + " ";
        }

        String firstName = "";
        String lastName = "";

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

        Pair<String, String> name;

        try{
           name = parseName(input);
        }
        catch(NameSplitterException e){
            //relative position to absolute position
            int startPosOfName = inputBackup.indexOf(input);
            e.setPosition(new Position(startPosOfName + e.getStartPos(), startPosOfName + e.getEndPos()));
            errors.add(e);
            return new ImmutablePair<>(new StructuredName(null, null, null, null, null), errors);
        }


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

        for (var s : titleStorage.getAllTitles()) {
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

        //dot not allowed . in name
        if(input.contains(".")){
            throw new InvalidCharacterException(new Position(input.indexOf("."), input.indexOf(".")));
        }

        //if input is empty, return null
        if (input.isEmpty()) {
            throw new NoLastNameGivenException(new Position(0, 0));
        }

        if (input.contains(",")) {
            String[] parts = input.split(",");
            if(parts.length > 1) throw new InvalidCharacterException(new Position(parts[1].indexOf(","), parts[1].indexOf(",")));

            Position patronymsPos = getPositionOfPatronyms(parts[0]);

            if(patronymsPos.start() == -1) return new ImmutablePair<>(parts[1].
                    replaceAll(" +", " "), parts[0].replaceAll(" +", " ").replace(" ", "-"));

            String patronym = parts[0].substring(patronymsPos.start(), patronymsPos.end()).toLowerCase();
            var trimmedLastName = parts[0].substring(patronymsPos.end() + 1).trim();
            //prettify name such that i.e. mÜllEr-MaIer is formatted to Müller-Maier
            String lastName = WordUtils.capitalize(trimmedLastName.replace("-", " ").toLowerCase().replaceAll(" +", " ")).replace(" ", "-");

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
     * The getPositionOfPatronyms method takes an input string and finds the position of patronymics in it.
     * It iterates over each patronymic and checks if it is present in the input string.
     * It returns a Position object containing the start and end position of the earliest patronymic found.
     *
     * @param input The input string in which to find the position of patronymics.
     * @return A Position object containing the start and end position of the earliest patronymic found.
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