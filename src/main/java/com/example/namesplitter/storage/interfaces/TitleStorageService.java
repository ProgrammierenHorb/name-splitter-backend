package com.example.namesplitter.storage.interfaces;

import java.util.Map;

public interface TitleStorageService {

    Map<String, String> getAllTitles();
    void addTitle(String regex, String title);
    void removeTitle(String title);
}
