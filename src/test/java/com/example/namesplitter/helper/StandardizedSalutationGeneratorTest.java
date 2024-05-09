package com.example.namesplitter.helper;

import com.example.namesplitter.model.Gender;
import com.example.namesplitter.model.StructuredName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StandardizedSalutationGeneratorTest {

    @Test
    void genderMale(){
        StructuredName structuredName = new StructuredName(Gender.MALE, new ArrayList<>(List.of("Prof.", "Dr.")), "Max", "Mustermann", null);
        assertEquals("Sehr geehrter Herr Prof. Dr. Mustermann", StandardizedSalutationGenerator.generateStandardizedSalutation(structuredName));
    }

    @Test
    void genderFemale(){
        StructuredName structuredName = new StructuredName(Gender.FEMALE, new ArrayList<>(List.of("Prof.", "Dr.")), "Monika", "Maier", null);
        assertEquals("Sehr geehrte Frau Prof. Dr. Maier", StandardizedSalutationGenerator.generateStandardizedSalutation(structuredName));
    }

    @Test
    void genderNull(){
        StructuredName structuredName = new StructuredName(null, new ArrayList<>(List.of("Prof.", "Dr.")), "Monika", "Maier", null);
        assertEquals("Guten Tag Monika Maier", StandardizedSalutationGenerator.generateStandardizedSalutation(structuredName));
    }

    @Test
    void genderDiverse(){
        StructuredName structuredName = new StructuredName(Gender.DIVERSE, new ArrayList<>(List.of("Prof.", "Dr.")), "Monika", "Maier", null);
        assertEquals("Guten Tag Monika Maier", StandardizedSalutationGenerator.generateStandardizedSalutation(structuredName));
    }

    @Test
    void genderDiverseNoFirstName() {
        StructuredName structuredName = new StructuredName(Gender.DIVERSE, new ArrayList<>(List.of("Prof.", "Dr.")), null, "Maier", null);
        assertEquals("Sehr geehrte Damen und Herren", StandardizedSalutationGenerator.generateStandardizedSalutation(structuredName));
    }

}