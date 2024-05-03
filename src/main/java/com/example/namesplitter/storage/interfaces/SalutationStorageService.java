package com.example.namesplitter.storage.interfaces;

import com.example.namesplitter.model.Gender;

import java.util.Map;

public interface SalutationStorageService {
    Map<String, Gender> getAllSalutations();
}
