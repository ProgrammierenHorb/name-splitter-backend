package com.example.namesplitter.parser;

import com.example.namesplitter.exception.*;
import com.example.namesplitter.model.StructuredName;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ErrorTests {

    static Parser nameParser;

    @BeforeAll
    static void setUp() {
        nameParser = new Parser();
        assertNotNull(nameParser);
    }

    @Test
    void invalidSalutationPosition(){
        assertInstanceOf(InvalidSalutationPositionException.class, nameParser.parse("y Gonzales-Müller, Herr Estobar").getRight().getFirst());
    }

    @Test
    void invalidSalutationPosition2(){
        assertInstanceOf(InvalidSalutationPositionException.class, nameParser.parse("John Frau").getRight().getFirst());
    }

    @Test
    void multipleSalutations(){
        assertInstanceOf(MultipleSalutationException.class, nameParser.parse("Herr Frau John").getRight().getFirst());
    }

    @Test
    void multipleSalutations2(){
        assertInstanceOf(MultipleSalutationException.class, nameParser.parse("Herr Herr John").getRight().getFirst());
    }

    @Test
    void conflictingGender(){
        assertInstanceOf(ConflictingGenderException.class, nameParser.parse("Herr Professorin John").getRight().getFirst());
    }

    @Test
    void conflictingGender2(){
        assertInstanceOf(ConflictingGenderException.class, nameParser.parse("Frau Doktor Müller").getRight().getFirst());
    }

    @Test
    void invalidChar(){
        assertInstanceOf(InvalidCharacterException.class, nameParser.parse("Herr! John").getRight().getFirst());
    }

    @Test
    void noLastName(){
        assertInstanceOf(NoLastNameGivenException.class, nameParser.parse("Professor").getRight().getFirst());
    }

    @Test
    void noLastName2(){
        assertInstanceOf(NoLastNameGivenException.class, nameParser.parse("Herr").getRight().getFirst());
    }


}
