package com.example.namesplitter.exception;

import com.example.namesplitter.model.Position;

/**
 * This exception indicates that the input string contains conflicting genders
 */
public class ConflictingGenderException extends NameSplitterException{
    public ConflictingGenderException(Position loc) {
        super("Widersprüchliche Geschlechter", loc);
    }
}
