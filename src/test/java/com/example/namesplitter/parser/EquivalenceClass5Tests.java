package com.example.namesplitter.parser;

import com.example.namesplitter.model.StructuredName;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for equivalence class 5
 * This class tests input where no firstname is given
 */
public class EquivalenceClass5Tests {

    static Parser nameParser;

    @BeforeAll
    static void setUp() {
        nameParser = new Parser();
        assertNotNull(nameParser);
    }

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
}
