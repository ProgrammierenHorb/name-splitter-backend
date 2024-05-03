package com.example.namesplitter.parser;

import com.example.namesplitter.model.Gender;
import com.example.namesplitter.model.StructuredName;
import com.example.namesplitter.storage.*;
import com.example.namesplitter.storage.interfaces.NameGenderService;
import com.example.namesplitter.storage.interfaces.PatronymicsService;
import com.example.namesplitter.storage.interfaces.SalutationStorageService;
import com.example.namesplitter.storage.interfaces.TitleStorageService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class NameParser implements Parsable {

    private final TitleStorageService titleStorage = new InMemoryTitleStorage();
    private final SalutationStorageService salutationStorage = new InMemorySalutationService();
    private final NameGenderService nameGenderService = new SQLiteNameGenderService();
    private final PatronymicsService patronymicsService = new InMemoryPatronymicsStorage();

    @Override
    public StructuredName parse(String input) {
        String firstName = "";
        String lastName = "";

        var genderParseResult = parseGender(input);

        Gender gender = genderParseResult.getLeft();
        input = genderParseResult.getRight().trim();

        List<String> titles = new ArrayList<>();
        Pair<String, String> title;
        while((title = parseTitle(input)).getLeft() != null)
        {
            titles.add(title.getLeft());
            input = title.getRight().trim();
        }

        Pair<String, String> name = parseName(input);

        firstName = name.getLeft();
        lastName = name.getRight();

        //if no gender is found yet, try guessing with first name from name database
        if(gender == null && firstName != null){
            firstName = firstName.trim();
            Gender potentialGender = nameGenderService.getGender(firstName.split(" ")[0]);
            if(potentialGender != null){
                gender = potentialGender;
            }
        }

        return new StructuredName(gender, titles, firstName, lastName, null);
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
            input = input.replace(longestMatch, "").trim();
            return new ImmutablePair<>(longestTitle, input);
        }

        return new ImmutablePair<>(null, input);
    }

    private Pair<Gender, String> parseGender(String input) {
        for (var s : salutationStorage.getAllSalutations().entrySet()) {
            if (input.startsWith(s.getKey())) {
                input = input.replaceFirst(s.getKey(), "");
                return new ImmutablePair<>(s.getValue(), input);
            }
        }
        return new ImmutablePair<>(null, input);
    }

    private Pair<String, String> parseName(String input){

        input = input.trim();

        //if input is empty, return null
        if(input.isEmpty()){
            return new ImmutablePair<>(null, null);
        }

        if(input.contains(",")){
            String[] parts = input.split(",");
            return new ImmutablePair<>(parts[1].trim(), parts[0].trim());
        }

        List<String> patronymics = patronymicsService.getAllPatronymics();

        int lastNameStartIndex = -1;

        String[] parts = input.split(" ");
        if(parts.length == 1) return new ImmutablePair<>(null, input);

        for(int i = 0; i < parts.length; i++){
            parts[i] = parts[i].trim();
            if(patronymics.contains(parts[i])){
                lastNameStartIndex = i;
                break;
            }
        }

        if(lastNameStartIndex == -1){
            return new ImmutablePair<>(String.join(" ", Arrays.stream(parts).toList().subList(0, parts.length -1)), parts[parts.length - 1]);
        }
        else{
            return new ImmutablePair<>(String.join(" ", Arrays.stream(parts).toList().subList(0, lastNameStartIndex)), String.join(" ", Arrays.stream(parts).toList().subList(lastNameStartIndex, parts.length)));
        }

    }
}