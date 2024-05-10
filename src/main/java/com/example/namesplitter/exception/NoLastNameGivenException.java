package com.example.namesplitter.exception;

import com.example.namesplitter.model.Position;

/**
 * This exception indicates that no last name was given
 */
public class NoLastNameGivenException extends NameSplitterException{
    public NoLastNameGivenException(Position loc) {
        super("Please provide at least a last name", loc);
    }
}
