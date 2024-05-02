package com.example.namesplitter.parser;

import com.example.namesplitter.model.Gender;
import com.example.namesplitter.model.StructuredName;
import com.example.namesplitter.storage.InMemorySalutationService;
import com.example.namesplitter.storage.InMemoryTitleStorage;
import com.example.namesplitter.storage.SalutationStorageService;
import com.example.namesplitter.storage.TitleStorageService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class NameParser implements Parsable {

    private final TitleStorageService titleStorage = new InMemoryTitleStorage();
    private final SalutationStorageService salutationStorage = new InMemorySalutationService();

    @Override
    public StructuredName parse(String input) {
        String firstName = null;
        String lastName = null;

        if(input.contains(","))
        {
            //handle comma separated names
            return new StructuredName(null, null, null, null, null);
        }

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

        if(input.contains(" ")) {
            String[] names = input.split(" ");
            firstName = names[0];
            lastName = String.join(" ", Arrays.copyOfRange(names, 1, names.length));
        }
        else{
            if(!input.isBlank() && !input.isEmpty()){
                lastName = input;
            }
        }

        return new StructuredName(gender, titles, firstName, lastName, null);
    }

    private Pair<String, String> parseTitle(String input) {
        String longestMatch = null;
        String longestTitle = null;

        for (var s : titleStorage.getAllTitles().entrySet()) {
            Pattern pattern = Pattern.compile(s.getKey());
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
}