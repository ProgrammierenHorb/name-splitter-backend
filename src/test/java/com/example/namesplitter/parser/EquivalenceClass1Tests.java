package com.example.namesplitter.parser;

import com.example.namesplitter.model.Gender;
import com.example.namesplitter.model.StructuredName;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for equivalence class 1
 * This class tests simple inputs
 */
public class EquivalenceClass1Tests {

    static Parser nameParser;

    @BeforeAll
    static void setUp() {
        nameParser = new Parser();
        assertNotNull(nameParser);
    }

    @Test
    void firstAndLastName(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(), "John", "Doe", null);
        assertEquals(expected, nameParser.parse("John Doe").getLeft());
    }

    @Test
    void lastAndFirstName(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(), "John", "Doe", null);
        assertEquals(expected, nameParser.parse("Doe, John").getLeft());
    }

    @Test
    void doctorFirstAndLastName(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(List.of("Dr.")), "John", "Doe", null);
        assertEquals(expected, nameParser.parse("Dr. John Doe").getLeft());
    }

    @Test
    void titleFirstAndLastName() {
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(List.of("Dr.")), "John", "Doe", null);
        assertEquals(expected, nameParser.parse("Dr. John Doe").getLeft());
    }
    @Test
    void shortLastName(){
        StructuredName expected = new StructuredName(Gender.FEMALE, new ArrayList<>(List.of("Dr.")),  "Eva", "Li",null);
        assertEquals(expected, nameParser.parse("Frau Dr. Eva Li").getLeft());
    }

}
