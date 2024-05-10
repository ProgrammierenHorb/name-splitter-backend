package com.example.namesplitter.parser;

import com.example.namesplitter.exception.InvalidCharacterException;
import com.example.namesplitter.exception.NameSplitterException;
import com.example.namesplitter.exception.NoLastNameGivenException;
import com.example.namesplitter.helper.GlobalVariables;
import com.example.namesplitter.model.CompleteName;
import com.example.namesplitter.model.Position;
import com.example.namesplitter.model.ReturnValueAndRemainigString;
import com.example.namesplitter.parser.interfaces.ISubParser;
import com.example.namesplitter.storage.InMemoryPatronymicsStorage;
import com.example.namesplitter.storage.InMemoryTitleStorage;
import com.example.namesplitter.storage.interfaces.PatronymicsService;
import com.example.namesplitter.storage.interfaces.TitleStorageService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The NameParser class implements the ISubParser interface and provides methods to parse a name string into a CompleteName object.
 * It uses services provided by InMemoryPatronymicsStorage and InMemoryTitleStorage to handle patronymics and titles.
 */
public class NameParser implements ISubParser<CompleteName> {

    PatronymicsService patronymicsService = InMemoryPatronymicsStorage.getInstance();
    TitleStorageService titleStorageService = InMemoryTitleStorage.getInstance();

    /**
     * The parse method takes a name string as input and parses it into a CompleteName object.
     * It throws a NameSplitterException if the input string is invalid or cannot be parsed.
     *
     * @param name The name string to be parsed.
     * @return A ReturnValueAndRemainigString object containing the parsed CompleteName object and the remaining string after parsing.
     * @throws NameSplitterException If the input string is invalid or cannot be parsed.
     */
    @Override
    public ReturnValueAndRemainigString<CompleteName> parse(String name) throws NameSplitterException {

        checkInput(name);

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
    }

    /**
     * The capitalizeNames method takes an input string and capitalizes the first letter of each word in it.
     * It handles words separated by spaces, hyphens, and apostrophes.
     *
     * @param input The input string to be capitalized.
     * @return The capitalized string.
     */
    private String capitalizeNames(String input) {
        String[] words = input.split("(\\s|-|')+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)));
                result.append(word.substring(1));
            }
        }

        // Replace the original hyphens, apostrophes and whitespaces
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '-' || input.charAt(i) == '\'' || input.charAt(i) == ' ') {
                result.insert(i, input.charAt(i));
            }
        }

        return result.toString();
    }

    /**
     * This method checks the input string for any disallowed characters.
     *
     * @param input The input string to be checked.
     * @throws InvalidCharacterException If the input string contains any disallowed characters.
     */
    private void checkInput(String input) throws InvalidCharacterException{

        //forbid most special characters
        String allowedSymbols = GlobalVariables.allowedCharacters;
        Pattern pattern = Pattern.compile(allowedSymbols);
        Matcher matcher = pattern.matcher(input);
        if(!matcher.matches()) {
            Matcher invalidCharMatcher = Pattern.compile(GlobalVariables.disallowedCharacters).matcher(input);
            if(invalidCharMatcher.find()){
                throw new InvalidCharacterException(new Position(invalidCharMatcher.start(), invalidCharMatcher.end() - 1));
            }
        }
    }
}
