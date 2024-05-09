package com.example.namesplitter.exception;

import com.example.namesplitter.model.Position;

public class MultipleSalutationException extends NameSplitterException{
    public MultipleSalutationException(Position location) {
        super("Es wurden mehrere Anreden angegeben", location);
    }
}
