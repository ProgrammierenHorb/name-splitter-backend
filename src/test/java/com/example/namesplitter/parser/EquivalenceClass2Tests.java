package com.example.namesplitter.parser;

import com.example.namesplitter.model.StructuredName;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for equivalence class 2
 * This class tests invalid inputs
 */
public class EquivalenceClass2Tests {

    static Parser nameParser;

    @BeforeAll
    static void setUp() {
        nameParser = new Parser();
        assertNotNull(nameParser);
    }

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
}
