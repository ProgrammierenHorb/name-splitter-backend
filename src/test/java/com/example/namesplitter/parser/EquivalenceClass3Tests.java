package com.example.namesplitter.parser;

import com.example.namesplitter.model.StructuredName;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for equivalence class 3
 * This class tests no input/empty input
 */
public class EquivalenceClass3Tests {

    static Parser nameParser;

    @BeforeAll
    static void setUp() {
        nameParser = new Parser();
        assertNotNull(nameParser);
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

}
