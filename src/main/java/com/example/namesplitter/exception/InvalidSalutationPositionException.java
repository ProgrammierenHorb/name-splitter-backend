package com.example.namesplitter.exception;

import com.example.namesplitter.model.Position;

/**
 * This exception indicates that the input string contains a salutation in an invalid position
 */
public class InvalidSalutationPositionException extends NameSplitterException{
    public InvalidSalutationPositionException(Position loc) {
        super("Anreden müssen am Anfang der Eingabe stehen", loc);
    }
}
