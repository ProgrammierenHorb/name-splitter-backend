package com.example.namesplitter.api;

import com.example.namesplitter.model.*;
import com.example.namesplitter.storage.interfaces.TitleStorageService;
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
        when(titleStorageService.getAllTitles()).thenReturn(Collections.singletonMap("Regex", "TitleDTO"));
        List<String> titles = restService.getTitles();
        assertEquals(1, titles.size());
        assertEquals("TitleDTO", titles.get(0));
    }

    @Test
    void testAddTitle() {
        TitleDTO titleDTO = new TitleDTO("TitleDTO", "Regex");
        assertTrue(restService.addTitle(titleDTO));
        // Verify that titleDTO is added to the titleDTO storage service
        verify(titleStorageService, times(1)).addTitle("TitleDTO", "Regex");
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
