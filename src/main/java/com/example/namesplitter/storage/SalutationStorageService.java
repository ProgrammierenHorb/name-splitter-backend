package com.example.namesplitter.storage;

import com.example.namesplitter.model.Gender;

import java.util.Collection;
import java.util.Map;

public interface SalutationStorageService {
    Map<String, Gender> getAllSalutations();
}
