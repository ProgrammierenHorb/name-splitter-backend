package com.example.namesplitter.exception;

import com.example.namesplitter.model.Position;

public class InvalidCharacterException extends NameSplitterException {
    public InvalidCharacterException(Position pos){
        super("Invalid character", pos);
    }
}
