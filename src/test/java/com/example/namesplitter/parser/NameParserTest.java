package com.example.namesplitter.parser;

import com.example.namesplitter.model.Gender;
import com.example.namesplitter.model.StructuredName;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NameParserTest {

    static NameParser nameParser;

    @BeforeAll
    static void setUp() {
        nameParser = new NameParser();
        assertNotNull(nameParser);
    }

    @Test
    void onlyLastName(){
        StructuredName expected = new StructuredName(null, new ArrayList<>(), null, "Doe", null);
        assertEquals(expected, nameParser.parse("Doe").getLeft());
    }

    @Test
    void firstAndLastName(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(), "John", "Doe", null);
        assertEquals(expected, nameParser.parse("John Doe").getLeft());
    }

    @Test
    void LastAndFirstName(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(), "John", "Doe", null);
        assertEquals(expected, nameParser.parse("Doe, John").getLeft());
    }

    @Test
    void doctorAndLastName(){
        StructuredName expected = new StructuredName(null, new ArrayList<>(List.of("Dr.")), null, "Doe", null);
        assertEquals(expected, nameParser.parse("Dr. Doe").getLeft());
    }

    @Test
    void doctorFirstAndLastName(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(List.of("Dr.")), "John", "Doe", null);
        assertEquals(expected, nameParser.parse("Dr. John Doe").getLeft());
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
    void titleAndLastName() {
        StructuredName expected = new StructuredName(null, new ArrayList<>(List.of("Dr.rer.nat.")), null, "Doe", null);
        assertEquals(expected, nameParser.parse("Dr.rer.nat. Doe").getLeft());
    }

    @Test
    void multipleTitlesAndLastName() {
        StructuredName expected = new StructuredName(null, new ArrayList<>(List.of("Prof.", "Dr.")), null, "Doe", null);
        assertEquals(expected, nameParser.parse("Prof. Dr. Doe").getLeft());
    }

    @Test
    void titleFirstAndLastName() {
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(List.of("Dr.")), "John", "Doe", null);
        assertEquals(expected, nameParser.parse("Dr. John Doe").getLeft());
    }

    @Test
    void titleWithSpecialCharactersAndLastName() {
        StructuredName expected = new StructuredName(null, new ArrayList<>(List.of("Dr.-Ing.")), null, "Doe", null);
        assertEquals(expected, nameParser.parse("Dr.-Ing. Doe").getLeft());
    }

    @Test
    void titleAndDoubleLastName() {
        StructuredName expected = new StructuredName(null, new ArrayList<>(List.of("Dr.")), null, "Voss-Tecklenburg", null);
        assertEquals(expected, nameParser.parse("Dr. Voss-Tecklenburg").getLeft());
    }

    //TODO: Fix this test
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
    void emptyInput() {
        StructuredName expected = new StructuredName(null, null, null, null, null);
        assertEquals(expected, nameParser.parse("").getLeft());
    }

    @Test
    void nullInput() {
        assertThrows(NullPointerException.class, () -> nameParser.parse(null).getLeft());
    }

    @Test
    void doubleFirstAndLastName(){
        StructuredName expected = new StructuredName(null, new ArrayList<>(List.of("Dr.phil.")), "Marie-Agnes", "Strack-Zimmermann", null);
        assertEquals(expected, nameParser.parse("Dr.phil. Marie-Agnes Strack-Zimmermann").getLeft());
    }

    //Note: Test is probably not decidable by a simple algorithm
/*    @Test
    void vonUndZuGutenberg(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(List.of("Dr.")), "Karl-Theodor Maria Nikolaus Johann Jacob Philipp Franz Joseph Sylvester", "Buhl-Freiherr von und zu Guttenberg", null);
        assertEquals(expected, nameParser.parse("Herr Doktor Karl-Theodor Maria Nikolaus Johann Jacob Philipp Franz Joseph Sylvester Buhl-Freiherr von und zu Guttenberg"));
    }*/

    @Test
    void middleName(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(), "Markus Paul Wolfgang", "Blattau", null);
        assertEquals(expected, nameParser.parse("Markus Paul Wolfgang Blattau").getLeft());
    }

}