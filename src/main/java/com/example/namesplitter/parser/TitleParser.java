package com.example.namesplitter.parser;

import com.example.namesplitter.model.ReturnValueAndRemainigString;
import com.example.namesplitter.model.TitleData;
import com.example.namesplitter.parser.interfaces.ISubParser;
import com.example.namesplitter.storage.InMemoryTitleStorage;
import com.example.namesplitter.storage.interfaces.TitleStorageService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The TitleParser class implements the ISubParser interface and provides methods to parse a title from an input string.
 * It uses a TitleStorageService to retrieve a list of academic titles and their corresponding regex patterns.
 */
public class TitleParser implements ISubParser<TitleData> {

    private final TitleStorageService titleStorage = InMemoryTitleStorage.getInstance();

    /**
     * The parse method takes an input string and tries to parse a title from it.
     * It finds the longest matching title in the input string and removes it from the string.
     * It returns a pair containing the title and the remaining input string.
     *
     * @param input The input string from which to parse a title.
     * @return A Pair object containing the title and the remaining input string.
     */
    public ReturnValueAndRemainigString<TitleData> parse(String input) {
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
            return new ReturnValueAndRemainigString<>(longestTitle, input);
        }

        return new ReturnValueAndRemainigString<>(null, input);
    }
}
