package com.example.namesplitter.exception;

import com.example.namesplitter.model.Location;

public class NoLastNameGivenException extends NameSplitterException{
    public NoLastNameGivenException(Location loc) {
        super("Please provide at least a last name", loc);
    }
}
