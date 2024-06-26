package com.example.namesplitter.storage.interfaces;

import com.example.namesplitter.model.TitleData;

import java.util.List;
import java.util.Map;

public interface TitleStorageService {

    List<TitleData> getAllAcademicTitles();
    List<TitleData> getAllNobilityTitles();
    boolean addTitle(TitleData title);
    boolean removeTitle(TitleData title);
}
