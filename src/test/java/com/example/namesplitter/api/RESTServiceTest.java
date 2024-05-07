package com.example.namesplitter.api;

import com.example.namesplitter.api.RESTService;
import com.example.namesplitter.exception.NameSplitterException;
import com.example.namesplitter.model.*;
import com.example.namesplitter.parser.NameParser;
import com.example.namesplitter.parser.Parser;
import com.example.namesplitter.storage.SQLiteNameGenderService;
import com.example.namesplitter.storage.interfaces.TitleStorageService;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RESTServiceTest {

    @Mock
    private RESTService restService;

    @Mock
    private TitleStorageService titleStorageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        restService = new RESTService();
        restService.titleStorageService = titleStorageService;
    }

    @Test
    void testParse() {
        APIResponse expected = new APIResponse( new ArrayList<>(), new StructuredName(Gender.MALE, new ArrayList<>(), "John", "Doe", null));
        assertEquals(expected, restService.parse("John Doe"));
    }

    @Test
    void testGetTitles() {
        // Mock title storage service
        when(titleStorageService.getAllTitles()).thenReturn(Collections.singletonMap("Regex", "Title"));
        List<String> titles = restService.getTitles();
        assertEquals(1, titles.size());
        assertEquals("Title", titles.get(0));
    }

    @Test
    void testAddTitle() {
        Title title = new Title("Title", "Regex");
        assertTrue(restService.addTitle(title));
        // Verify that title is added to the title storage service
        verify(titleStorageService, times(1)).addTitle("Title", "Regex");
    }

    @Test
    void testSave() {
        StructuredName name = new StructuredName(Gender.MALE, List.of("Dr."), "Jon","Doe",null);
        StructuredName savedName = restService.save(name);
        //TODO: assert if saved successful
    }

    @Test
    void testStatus() {
        assertEquals("online", restService.status());
    }
}
