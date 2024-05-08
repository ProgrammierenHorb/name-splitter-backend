package com.example.namesplitter.parser;

import com.example.namesplitter.model.Gender;
import com.example.namesplitter.model.StructuredName;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class FormattingTests {

    static Parser nameParser;

    @BeforeAll
    static void setUp() {
        nameParser = new Parser();
        assertNotNull(nameParser);
    }

    @Test
    void simpleInput(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(), "John", "Doe", null);
        assertEquals(expected, nameParser.parse("JOhN DoE").getKey());
    }

    @Test
    void inputWithComma(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(), "John", "Doe", null);
        assertEquals(expected, nameParser.parse("DoE, JOhN").getKey());
    }

    @Test
    void additionalSpaces() {
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(), "John", "Doe", null);
        assertEquals(expected, nameParser.parse("  DoE    ,  JOhN   ").getKey());
    }

    @Test
    void title(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(List.of("Dr.")), "Max", "Mustermann", null);
        assertEquals(expected, nameParser.parse("  Dr.   MUSTERMANN    ,  MaX   ").getKey());
    }

    @Test
    void titleBoforeFirstName(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(List.of("Dr.")), "Max", "Mustermann", null);
        assertEquals(expected, nameParser.parse("     MUSTERMANN    , Dr. MaX   ").getKey());
    }

    @Test
    void doubleName(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(List.of("M.Sc.")), "Marius Müller", "Westernhagen", null);
        assertEquals(expected, nameParser.parse("M.SC. Marius MÜller    WesteRNhagen").getKey());
    }

    @Test
    void nobilityTitle(){
        StructuredName expected = new StructuredName(Gender.FEMALE, new ArrayList<>(List.of("Prof.", "Dr.")), "Claudia Maria Tristan", "Baronin von und zu Maier-Müller", null);
        assertEquals(expected, nameParser.parse("ProfeSSorin  Dr  ClAuDia   Maria   TriStAn   Baronin   von und zu  MaIeR   MülleR").getKey());
    }

    @Test
    void doubleFirstName() {
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(List.of("Prof.", "Dr.")), "Markus-Mirakulus Tristan", "Baron von und zu Guttenberg", null);
        assertEquals(expected, nameParser.parse("Professor Dr.  maRKus-miRAkuluS   Tristan   Baron von und zu Guttenberg").getKey());
    }
}
