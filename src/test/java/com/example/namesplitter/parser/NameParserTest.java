package com.example.namesplitter.parser;

import com.example.namesplitter.model.Gender;
import com.example.namesplitter.model.StructuredName;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NameParserTest {

    static Parser nameParser;

    @BeforeAll
    static void setUp() {
        nameParser = new Parser();
        assertNotNull(nameParser);
    }

    //region Equivalence class 1	"Gängige Anreden"

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
    //endregion

    //region Equivalence class 2	"Falsche Anreden"

    @Test
    void numbers(){
        StructuredName expected = new StructuredName(null,null, null, null, null);
        assertEquals(expected, nameParser.parse("987234 8596748 4345").getLeft());
    }
    @Test
    void specialCharacters(){
        StructuredName expected = new StructuredName(null,null, null, null, null);
        assertEquals(expected, nameParser.parse("M%x Müller").getLeft());
    }
    @Test
    void specialCharactersAndNumbers(){
        StructuredName expected = new StructuredName(null,null, null, null, null);
        assertEquals(expected, nameParser.parse("$123dfsg4 4645567").getLeft());
    }
    //endregion

    //region Equivalence class 3	"Keine Eingabe"


    @Test
    void emptyInput() {
        StructuredName expected = new StructuredName(null, null, null, null, null);
        assertEquals(expected, nameParser.parse("").getLeft());
    }

    @Test
    void nullInput() {
        assertThrows(NullPointerException.class, () -> nameParser.parse(null).getLeft());
    }

    //endregion

    //region Equivalence class 4	"Ungewöhnliche Namen"

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

    @Test
    void fails(){
        StructuredName expected = new StructuredName(null, new ArrayList<>(),  "Estobar", "y Gonzales-Müller",null);
        assertEquals(expected, nameParser.parse("y Gonzales Müller, Herr Estobar").getLeft());
    }

    //endregion

    //region Equivalence class 5	"Kein Vorname"
    @Test
    void onlyLastName(){
        StructuredName expected = new StructuredName(null, new ArrayList<>(), null, "Doe", null);
        assertEquals(expected, nameParser.parse("Doe").getLeft());
    }

    @Test
    void doctorAndLastName(){
        StructuredName expected = new StructuredName(null, new ArrayList<>(List.of("Dr.")), null, "Doe", null);
        assertEquals(expected, nameParser.parse("Dr. Doe").getLeft());
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
    void titleWithSpecialCharactersAndLastName() {
        StructuredName expected = new StructuredName(null, new ArrayList<>(List.of("Dr.-Ing.")), null, "Doe", null);
        assertEquals(expected, nameParser.parse("Dr.-Ing. Doe").getLeft());
    }

    @Test
    void titleAndDoubleLastName() {
        StructuredName expected = new StructuredName(null, new ArrayList<>(List.of("Dr.")), null, "Voss-Tecklenburg", null);
        assertEquals(expected, nameParser.parse("Dr. Voss-Tecklenburg").getLeft());
    }

    //endregion

    //region Equivalence class 6	"Kein Vorname und Nachname"

    @Test
    void noLastName(){
        StructuredName expected = new StructuredName(null,null, null, null, null);
        assertEquals(expected, nameParser.parse("Frau B.Eng. ").getLeft());
    }
    @Test
    void noLastNameDoubleTitle(){
        StructuredName expected = new StructuredName(null,null, null, null, null);
        assertEquals(expected, nameParser.parse("Herr Dr. Dr. ").getLeft());
    }

    //endregion






    //Note: Test is probably not decidable by a simple algorithm
    @Test
    void vonUndZuGutenberg(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(List.of("Dr.")), "Karl-Theodor Maria Nikolaus Johann Jacob Philipp Franz Joseph Sylvester", "von und zu Guttenberg", null);
        assertEquals(expected, nameParser.parse("Herr Doktor Karl-Theodor Maria Nikolaus Johann Jacob Philipp Franz Joseph Sylvester von und zu Guttenberg").getLeft());
    }




}