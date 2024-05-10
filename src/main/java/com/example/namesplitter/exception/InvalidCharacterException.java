package com.example.namesplitter.exception;

import com.example.namesplitter.model.Position;

/**
 * This exception indicates that the input string contains invalid characters
 */
public class InvalidCharacterException extends NameSplitterException {
    public InvalidCharacterException(Position pos){
        super("Ungültiges Symbol", pos);
    }
}
