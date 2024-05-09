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
        when(titleStorageService.getAllAcademicTitles()).thenReturn(List.of(new TitleData("TitleDTO", "Regex", null, 100)));
        List<TitleData> titles = restService.getTitles();
        assertEquals(1, titles.size());
        assertEquals(new TitleData("TitleDTO", "Regex", null, 100), titles.getFirst());
    }

    @Test
    void testStatus() {
        assertEquals("online", restService.status());
    }
}
