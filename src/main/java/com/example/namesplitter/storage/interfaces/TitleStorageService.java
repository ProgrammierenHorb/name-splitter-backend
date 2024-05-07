package com.example.namesplitter.storage.interfaces;

import com.example.namesplitter.model.TitleData;

import java.util.List;
import java.util.Map;

public interface TitleStorageService {

    List<TitleData> getAllTitles();
    boolean addTitle(String regex, String title);
    boolean removeTitle(String title);
}
