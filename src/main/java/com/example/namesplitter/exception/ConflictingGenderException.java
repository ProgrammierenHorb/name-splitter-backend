package com.example.namesplitter.exception;

import com.example.namesplitter.model.Position;

public class ConflictingGenderException extends NameSplitterException{
    public ConflictingGenderException(Position loc) {
        super("Conflicting Genders", loc);
    }
}
