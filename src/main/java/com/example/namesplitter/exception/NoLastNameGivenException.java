package com.example.namesplitter.exception;

import com.example.namesplitter.model.Position;

public class NoLastNameGivenException extends NameSplitterException{
    public NoLastNameGivenException(Position loc) {
        super("Please provide at least a last name", loc);
    }
}
