package com.example.namesplitter.parser;

import com.example.namesplitter.model.Gender;
import com.example.namesplitter.model.StructuredName;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Test class for equivalence class 4
 * This class tests for extrordinary/uncommon names
 */
public class EquivalenceClass4Tests {

    static Parser nameParser;

    @BeforeAll
    static void setUp() {
        nameParser = new Parser();
        assertNotNull(nameParser);
    }


    @Test
    void doubleLastName(){
        StructuredName expected = new StructuredName(Gender.FEMALE, new ArrayList<>(), "Martina", "Voss-Tecklenburg", null);
        assertEquals(expected, nameParser.parse("Martina Voss-Tecklenburg").getLeft());
    }
    @Test
    void doubleLastNameWithComma(){
        StructuredName expected = new StructuredName(Gender.FEMALE, new ArrayList<>(), "Martina", "Voss-Tecklenburg", null);
        assertEquals(expected, nameParser.parse("Voss-Tecklenburg, Martina").getLeft());
    }

    @Test
    void titleAndLastNameWithComma() {
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(List.of("Dr.")), "John", "Doe", null);
        assertEquals(expected, nameParser.parse("Doe, Dr. John").getLeft());
    }

    @Test
    void titleAndDoubleLastNameWithComma() {
        StructuredName expected = new StructuredName(Gender.FEMALE, new ArrayList<>(List.of("Dr.")), "Martina", "Voss-Tecklenburg", null);
        assertEquals(expected, nameParser.parse("Dr. Voss-Tecklenburg, Martina").getLeft());
    }

    @Test
    void middleName(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(), "Markus Paul Wolfgang", "Blattau", null);
        assertEquals(expected, nameParser.parse("Markus Paul Wolfgang Blattau").getLeft());
    }
    @Test
    void doubleTitle(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(List.of("Dr.", "Dr.")), "Max", "Mustermann", null);
        assertEquals(expected, nameParser.parse("Herr Dr. Dr. Max Mustermann").getLeft());
    }
    @Test
    void doubleFirstAndLastName(){
        StructuredName expected = new StructuredName(null, new ArrayList<>(List.of("Dr.phil.")), "Marie-Agnes", "Strack-Zimmermann", null);
        assertEquals(expected, nameParser.parse("Dr.phil. Marie-Agnes Strack-Zimmermann").getLeft());
    }
    @Test
    void specialCharactersInTitle(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(List.of("Dr.")),  "Max", "Müller",null);
        assertEquals(expected, nameParser.parse("Herr Dr. Max Müller").getLeft());
    }
    @Test
    void multipleMiddleNames(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(List.of("Dr.")),  "Max Peter", "Mustermann",null);
        assertEquals(expected, nameParser.parse("Herr Dr. Max Peter Mustermann").getLeft());
    }
    @Test
    void vanHoof(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(List.of("Prof.", "Dr.phil.")),  "Antonius", "van Hoof",null);
        assertEquals(expected, nameParser.parse("Herr Dr.phil. Prof. Antonius van Hoof").getLeft());
    }

    @Test
    void vanHoof2(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(List.of("Prof.", "Dr.phil.")),  "Antonius", "van Hoof",null);
        assertEquals(expected, nameParser.parse("van    Hoof,    Dr.    phil.    Prof.    Antonius").getLeft());
    }

    @Test
    void y(){
        StructuredName expected = new StructuredName(null, new ArrayList<>(),  "Estobar", "y Gonzales",null);
        assertEquals(expected, nameParser.parse("Estobar y Gonzales").getLeft());
    }

    @Test
    void y2(){
        StructuredName expected = new StructuredName(null, new ArrayList<>(),  "Estobar", "y Gonzales",null);
        assertEquals(expected, nameParser.parse("y Gonzales, Estobar").getLeft());
    }

}
