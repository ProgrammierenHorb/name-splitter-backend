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

public class GenderParser implements ISubParser<Gender> {

    private final SalutationStorageService salutationStorage = InMemorySalutationService.getInstance();

    //TODO: Detect multiple genders and throw an exception
    @Override
    public ReturnValueAndRemainigString<Gender> parse(String input) {

        // Build regex pattern for all salutations
        String regexPattern = salutationStorage.getAllSalutations().keySet().stream()
                .map(g -> "\\b" + Pattern.quote(g) + "(\\W|$)")
                .reduce("", (a, b) -> a + "|" + b);

        //Example:
        //match Mme. but not Mme.abc
        //match herr but nor freiherr

        //Gender needs to be at the beginning of the string
        //if string contains a gender but not at the beginning throw exception
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
