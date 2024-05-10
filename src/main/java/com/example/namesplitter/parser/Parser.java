package com.example.namesplitter.parser;

import com.example.namesplitter.exception.ConflictingGenderException;
import com.example.namesplitter.exception.InvalidCharacterException;
import com.example.namesplitter.exception.NameSplitterException;
import com.example.namesplitter.exception.NoLastNameGivenException;
import com.example.namesplitter.helper.GlobalVariables;
import com.example.namesplitter.model.*;
import com.example.namesplitter.parser.interfaces.IParser;
import com.example.namesplitter.parser.interfaces.ISubParser;
import com.example.namesplitter.storage.*;
import com.example.namesplitter.storage.interfaces.NameGenderService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * The Parser class implements the IParser interface and provides methods to parse a name string into a structured name.
 * It uses services to retrieve titles, salutations, genders, and patronymics.
 * The parse method is the main method that orchestrates the parsing process.
 */
public class Parser implements IParser {

    private final NameGenderService nameGenderService = new SQLiteNameGenderService();
    private final ISubParser<CompleteName> nameParser = new NameParser();
    private final ISubParser<Gender> genderParser = new GenderParser();
    private final ISubParser<TitleData> titleParser = new TitleParser();

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

        String firstName;
        String lastName;

        ReturnValueAndRemainigString<Gender> genderParseResult;

        try{
            genderParseResult = genderParser.parse(input);
        }
        catch(NameSplitterException e){
            //relative position to absolute position
            errors.add(e);
            return new ImmutablePair<>(new StructuredName(null, null, null, null, null), errors);
        }


        Gender gender = genderParseResult.returnValue();
        input = genderParseResult.remainingString();

        List<TitleData> titles = new ArrayList<>();
        ReturnValueAndRemainigString<TitleData> title;
        while ((title = titleParser.parse(input)).returnValue() != null) {
            titles.add(title.returnValue());
            Gender genderFromTitle = title.returnValue().gender();
            if(genderFromTitle != null && gender != null && gender != genderFromTitle){
                errors.add(new ConflictingGenderException(new Position(0, inputBackup.length())));
                return new ImmutablePair<>(new StructuredName(null, null, null, null, null), errors);
            }
            else if(genderFromTitle != null){
                gender = genderFromTitle;
            }
            input = title.remainingString();
        }

        ReturnValueAndRemainigString<CompleteName> nameParserResult;

        try{
            nameParserResult = nameParser.parse(input);
        }
        catch(NameSplitterException e){
            //relative position to absolute position
            int startPosOfName = inputBackup.indexOf(input);
            e.setPosition(new Position(startPosOfName + e.getStartPos(), startPosOfName + e.getEndPos()));
            errors.add(e);
            return new ImmutablePair<>(new StructuredName(null, null, null, null, null), errors);
        }

        CompleteName completeName = nameParserResult.returnValue();

        firstName = completeName.firstName();
        lastName = completeName.lastName();

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

}