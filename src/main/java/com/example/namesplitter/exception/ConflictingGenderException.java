package com.example.namesplitter.exception;

import com.example.namesplitter.model.Location;

public class ConflictingGenderException extends NameSplitterException{
    public ConflictingGenderException(Location loc) {
        super("Conflicting Genders", loc);
    }
}
