package com.example.namesplitter.parser;

import com.example.namesplitter.exception.NoLastNameGivenException;
import com.example.namesplitter.model.StructuredName;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for equivalence class 5
 * This class tests input where no lastname is given
 */
public class EquivalenceClass6Tests {

    static Parser nameParser;

    @BeforeAll
    static void setUp() {
        nameParser = new Parser();
        assertNotNull(nameParser);
    }

    @Test
    void noLastName(){
        assertInstanceOf(NoLastNameGivenException.class, nameParser.parse("Frau B.Eng. ").getRight().getFirst());
    }

    @Test
    void noLastNameDoubleTitle(){
        assertInstanceOf(NoLastNameGivenException.class, nameParser.parse("Herr Dr. Dr. ").getRight().getFirst());
    }

    @Test
    void noLastNameCommaSyntax(){
        assertInstanceOf(NoLastNameGivenException.class, nameParser.parse(", Dr. Tom").getRight().getFirst());
    }
}
