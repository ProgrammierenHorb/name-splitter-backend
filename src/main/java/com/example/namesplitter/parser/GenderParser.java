package com.example.namesplitter.parser;

import com.example.namesplitter.model.Gender;
import com.example.namesplitter.model.ReturnValueAndRemainigString;
import com.example.namesplitter.storage.InMemorySalutationService;
import com.example.namesplitter.storage.interfaces.SalutationStorageService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenderParser implements ISubParser<Gender>{

    private final SalutationStorageService salutationStorage = InMemorySalutationService.getInstance();

    //TODO: Detect multiple genders and throw an exception
    @Override
    public ReturnValueAndRemainigString<Gender> parse(String input) {
        for (var s : salutationStorage.getAllSalutations().entrySet()) {
            Pattern pattern = Pattern.compile("^" + s.getKey(), Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);//Gender needs to be at the beginning of the string
            if (matcher.find()) {
                input = input.replaceFirst(s.getKey(), "");
                return new ReturnValueAndRemainigString<>(s.getValue(), input);
            }
        }
        return new ReturnValueAndRemainigString<>(null, input);
    }
}
