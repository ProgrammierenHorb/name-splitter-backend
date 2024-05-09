package com.example.namesplitter.parser;

import com.example.namesplitter.exception.InvalidCharacterException;
import com.example.namesplitter.exception.NameSplitterException;
import com.example.namesplitter.exception.NoLastNameGivenException;
import com.example.namesplitter.model.CompleteName;
import com.example.namesplitter.model.Position;
import com.example.namesplitter.model.ReturnValueAndRemainigString;
import com.example.namesplitter.parser.interfaces.ISubParser;
import com.example.namesplitter.storage.InMemoryPatronymicsStorage;
import com.example.namesplitter.storage.InMemoryTitleStorage;
import com.example.namesplitter.storage.interfaces.PatronymicsService;
import com.example.namesplitter.storage.interfaces.TitleStorageService;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameParser implements ISubParser<CompleteName> {

    PatronymicsService patronymicsService = InMemoryPatronymicsStorage.getInstance();
    TitleStorageService titleStorageService = InMemoryTitleStorage.getInstance();

    @Override
    public ReturnValueAndRemainigString<CompleteName> parse(String name) throws NameSplitterException {

        name = name.replaceAll(" +", " ").trim();

        Position patronymsPos;
        String lastName = "";
        String firstName = "";
        String patronymic = "";

        //if input is empty, return null
        if (name.isEmpty()) {
            throw new NoLastNameGivenException(new Position(0, 0));
        }

        //dot not allowed . in name
        if(name.contains(".")){
            throw new InvalidCharacterException(new Position(name.indexOf("."), name.indexOf(".")));
        }

        if (name.contains(",")) {
            String[] parts = name.split(",");
            //more than 1 comma
            if(parts.length > 2) throw new InvalidCharacterException(new Position(parts[1].indexOf(","), parts[1].indexOf(",")));


            String tempLastName = parts[0];
            firstName = parts[1];

            patronymsPos = getPositionOfPatronyms(parts[0]);

            //no patronymic found
            if(patronymsPos.start() == -1){
                lastName = tempLastName;
            }
            else{
                patronymic = parts[0].substring(patronymsPos.start(), patronymsPos.end());
                lastName = parts[0].substring(patronymsPos.end() + 1);
            }

        }
        else{
            patronymsPos = getPositionOfPatronyms(name);
            //no patronymic found
            if (patronymsPos.start() == -1) {
                var parts = name.split(" ");
                //only last name given
                if (parts.length == 1){
                    lastName = parts[0];
                }
                else {
                    firstName = String.join(" ", Arrays.stream(parts).toList().subList(0, parts.length - 1));
                    lastName = parts[parts.length - 1];
                }
            }
            else{
                patronymic = name.substring(patronymsPos.start(), patronymsPos.end());
                lastName = name.substring(patronymsPos.end() + 1);
                firstName = name.substring(0, patronymsPos.start() - 1);
            }

        }

        lastName = lastName.toLowerCase().trim();
        firstName = firstName.toLowerCase().trim();

        firstName = capitalizeNames(firstName.trim());

        lastName = capitalizeNames(lastName.trim());
        //format double last names with a hyphen
        lastName = lastName.replace(' ', '-');

        if (lastName.isEmpty()) throw new NoLastNameGivenException(new Position(patronymsPos.end() + 1, patronymsPos.end() + 1));
        if (firstName.isEmpty()) return new ReturnValueAndRemainigString<>(new CompleteName(null, lastName), "");
        if(patronymic.isEmpty()) return new ReturnValueAndRemainigString<>(new CompleteName(firstName, lastName), "");
        return new ReturnValueAndRemainigString<>(new CompleteName(firstName, patronymic + " " + lastName), "");
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

        String nobilityTitlesRegex = titleStorageService.getAllNobilityTitles().stream().map(title -> title.regex()).reduce("", (a, b) -> a + "|" + b);
        String patronymicsRegex = patronymicsService.getAllPatronymics().stream().map(patronym -> "\\b" + patronym + "\\b").reduce("", (a, b) -> a + "|" + b);
        nobilityTitlesRegex = "(" + nobilityTitlesRegex.replaceFirst("\\|", "") + ")\\s?";
        patronymicsRegex = "(" + patronymicsRegex.replaceFirst("\\|", "") + ")";

        String regexPattern = "((" + nobilityTitlesRegex + ")?" + patronymicsRegex + ")";
        Pattern pattern = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);
        Matcher m = pattern.matcher(input);

        if (m.find()) {
            int i = m.start();
            int j = m.end();
            return new Position(m.start(), m.end());
        }

        return new Position(-1, -1);

//        List<String> patronymics = patronymicsService.getAllPatronymics();
//        int earliestLastNameStartIndex = Integer.MAX_VALUE;
//        int earliestLastNameEndIndex = Integer.MAX_VALUE;
//        int lastNameStartIndex;
//
//        //search for patronymics (i.e. "van", "von", "de" etc.) in the input string
//        for (String patronymic : patronymics) {
//            //if the string starts with patronymic, i.e. "van Hoof, Markus" one cannot assume there is a whitespace in front
//            if(input.startsWith(patronymic + " ")){
//                return new Position(0, patronymic.length());
//            }
//            lastNameStartIndex = input.toLowerCase().indexOf(" " + patronymic + " ");
//            //the first patronymic found is taken as the beginning of the last name
//            if (lastNameStartIndex == -1) continue;
//            else{
//                lastNameStartIndex++;
//            }
//            if (lastNameStartIndex < earliestLastNameStartIndex) {
//                earliestLastNameStartIndex = lastNameStartIndex;
//                earliestLastNameEndIndex = lastNameStartIndex + patronymic.length();
//            }
//
//            //if two patronymics are found that start at the same position, the longer one is taken
//            //i.e. "van den" ist taken over "van"
//            else if (lastNameStartIndex <= earliestLastNameStartIndex && lastNameStartIndex + patronymic.length() > earliestLastNameEndIndex) {
//                earliestLastNameEndIndex = lastNameStartIndex + patronymic.length();
//            }
//        }
//        if (earliestLastNameStartIndex == Integer.MAX_VALUE) {
//            return new Position(-1, -1);
//        }
//        return new Position(earliestLastNameStartIndex, earliestLastNameEndIndex);
    }

    private String capitalizeNames(String input) {
        String[] words = input.split("(\\s|-)+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)));
                result.append(word.substring(1).toLowerCase());
                result.append(' ');
            }
        }

        // Trim the trailing space and replace the original hyphens
        String capitalized = result.toString().trim();
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '-') {
                capitalized = capitalized.substring(0, i) + "-" + capitalized.substring(i + 1);
            }
        }

        return capitalized;
    }
}
