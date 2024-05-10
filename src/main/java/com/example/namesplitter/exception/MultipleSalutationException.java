package com.example.namesplitter.exception;

import com.example.namesplitter.model.Position;

/**
 * This exception indicates that the input string contains multiple salutations
 */
public class MultipleSalutationException extends NameSplitterException{
    public MultipleSalutationException(Position location) {
        super("Es wurden mehrere Anreden angegeben", location);
    }
}
