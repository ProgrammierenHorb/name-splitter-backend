package com.example.namesplitter.exception;

import com.example.namesplitter.model.Position;

public class InvalidSalutationPositionException extends NameSplitterException{
    public InvalidSalutationPositionException(Position loc) {
        super("Anreden müssen am Anfang der Eingabe stehen", loc);
    }
}
