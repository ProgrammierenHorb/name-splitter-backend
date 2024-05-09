package com.example.namesplitter.parser;

import com.example.namesplitter.model.Gender;
import com.example.namesplitter.model.StructuredName;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AdditionalTests {

    static Parser nameParser;

    @BeforeAll
    static void setUp() {
        nameParser = new Parser();
        assertNotNull(nameParser);
    }

    @Test
    void vonUndZuGutenberg(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(List.of("Dr.")), "Karl-Theodor Maria Nikolaus Johann Jacob Philipp Franz Joseph Sylvester", "Freiherr von und zu Guttenberg", null);
        assertEquals(expected, nameParser.parse("Herr Doktor Karl-Theodor Maria Nikolaus Johann Jacob Philipp Franz Joseph Sylvester Freiherr von und zu Guttenberg").getLeft());
    }

    @Test
    void testFrauSandraBerger(){
        StructuredName expected = new StructuredName(Gender.FEMALE, new ArrayList<>(), "Sandra", "Berger", null);
        assertEquals(expected, nameParser.parse("Frau Sandra Berger").getLeft());
    }

    @Test
    void testHerrDrSandroGutmensch(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(List.of("Dr.")), "Sandro", "Gutmensch", null);
        assertEquals(expected, nameParser.parse("Herr Dr. Sandro Gutmensch").getLeft());
    }

    @Test
    void testProfessorHeinreichFreiherrVomWald(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(List.of("Prof.")), "Heinreich", "Freiherr vom Wald", null);
        assertEquals(expected, nameParser.parse("Professor Heinreich Freiherr vom Wald").getLeft());
    }

    @Test
    void testMrsDoreenFaber(){
        StructuredName expected = new StructuredName(Gender.FEMALE, new ArrayList<>(), "Doreen", "Faber", null);
        assertEquals(expected, nameParser.parse("Mrs. Doreen Faber").getLeft());
    }

    @Test
    void testMmeCharlotteNoir(){
        StructuredName expected = new StructuredName(Gender.FEMALE, new ArrayList<>(), "Charlotte", "Noir", null);
        assertEquals(expected, nameParser.parse("Mme. Charlotte Noir").getLeft());
    }

    @Test
    void testEstobarYGonzales(){
        StructuredName expected = new StructuredName(null, new ArrayList<>(), "Estobar", "y Gonzales", null);
        assertEquals(expected, nameParser.parse("Estobar y Gonzales").getLeft());
    }

    @Test
    void testFrauProfDrMariaVonLeuthauser(){
        StructuredName expected = new StructuredName(Gender.FEMALE, new ArrayList<>(List.of("Prof.", "Dr.rer.nat.")), "Maria", "von Leuthäuser-Schnarrenberger", null);
        assertEquals(expected, nameParser.parse("Frau Prof. Dr. rer. nat. Maria von Leuthäuser-Schnarrenberger").getLeft());
    }

    @Test
    void testHerrDiplIngMaxVonMuller(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(List.of("Dipl.-Ing.")), "Max", "von Müller", null);
        assertEquals(expected, nameParser.parse("Herr Dipl. Ing. Max von Müller").getLeft());
    }

    @Test
    void testDrRusswurmWinfried(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(List.of("Dr.")), "Winfried", "Russwurm", null);
        assertEquals(expected, nameParser.parse("Dr. Russwurm, Winfried").getLeft());
    }

    @Test
    void testHerrDrIngDrRerNatDrHcMultPaulSteffens(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(List.of("Dr.rer.nat.", "Dr.-Ing.", "Dr.h.c.mult.")), "Paul", "Steffens", null);
        assertEquals(expected, nameParser.parse("Herr Dr.-Ing. Dr. rer. nat. Dr. h.c. mult. Paul Steffens").getLeft());
    }

    @Test
    void multipleSameGenders(){
        StructuredName expected = new StructuredName(Gender.MALE, new ArrayList<>(List.of("Prof.", "Dr.")), "Paul", "Steffens", null);
        assertEquals(expected, nameParser.parse("Herr Professor Doktor Paul Steffens").getLeft());
    }
}
