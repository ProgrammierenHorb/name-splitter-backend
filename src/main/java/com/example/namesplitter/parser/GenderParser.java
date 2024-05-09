package com.example.namesplitter.parser;

import com.example.namesplitter.exception.InvalidSalutationPositionException;
import com.example.namesplitter.exception.MultipleSalutationException;
import com.example.namesplitter.model.Gender;
import com.example.namesplitter.model.Position;
import com.example.namesplitter.model.ReturnValueAndRemainigString;
import com.example.namesplitter.parser.interfaces.ISubParser;
import com.example.namesplitter.storage.InMemorySalutationService;
import com.example.namesplitter.storage.interfaces.SalutationStorageService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is responsible for parsing the gender from a given input string.
 * It implements the ISubParser interface with Gender as the type parameter.
 */
public class GenderParser implements ISubParser<Gender> {

    /**
     * An instance of SalutationStorageService to access the salutations.
     */
    private final SalutationStorageService salutationStorage = InMemorySalutationService.getInstance();

    /**
     * This method parses the input string to extract the gender.
     * It first builds a regex pattern for all salutations.
     * If a salutation is found at the beginning of the string, it is considered valid and removed from the string.
     * If another salutation is found anywhere else in the string, a MultipleSalutationException is thrown.
     * If a salutation is found but not at the beginning of the string, an InvalidSalutationPositionException is thrown.
     * If no salutation is found, the method returns null for the gender and the original input string.
     *
     * @param input The input string to parse.
     * @return A ReturnValueAndRemainigString object containing the parsed gender and the remaining string.
     * @throws MultipleSalutationException If multiple salutations are found in the string.
     * @throws InvalidSalutationPositionException If a salutation is found but not at the beginning of the string.
     */
    @Override
    public ReturnValueAndRemainigString<Gender> parse(String input) {

        // Build regex pattern for all salutations
        //Gender needs to be at the beginning of the string
        //if string contains a gender but not at the beginning throw exception
        String regexPattern = salutationStorage.getAllSalutations().keySet().stream()
                .map(g -> "\\b" + Pattern.quote(g) + "(\\W|$)")
                .reduce("", (a, b) -> a + "|" + b);

        String regexPatternValidSalutation = "^(" + regexPattern.replaceFirst("\\|", "") + ")";
        String regexPatternInvalidSalutation = "(" + regexPattern.replaceFirst("\\|", "") + ")";

        Pattern invalidPattern = Pattern.compile(regexPatternInvalidSalutation, Pattern.CASE_INSENSITIVE);
        Pattern validPattern = Pattern.compile(regexPatternValidSalutation, Pattern.CASE_INSENSITIVE);

        Matcher validMatcher = validPattern.matcher(input);
        Matcher invalidMatcher = invalidPattern.matcher(input);
        if (validMatcher.find()) {
            input = input.replaceFirst(validMatcher.group(), "");
            //if anywhere in the string there is another salutation, throw exception
            invalidMatcher = invalidPattern.matcher(input);
            if(invalidMatcher.find()){
                throw new MultipleSalutationException(new Position(invalidMatcher.start(), invalidMatcher.end()));
            }
            String matchedSalutation = validMatcher.group().toLowerCase().trim();
            return new ReturnValueAndRemainigString<>(salutationStorage.getAllSalutations().get(matchedSalutation), input);
        }

        //salutation is not at the beginning of the string
        if(invalidMatcher.find()) {
            throw new InvalidSalutationPositionException(new Position(invalidMatcher.start(), invalidMatcher.end()));
        }

        //no salutation found
        return new ReturnValueAndRemainigString<>(null, input);
    }
}