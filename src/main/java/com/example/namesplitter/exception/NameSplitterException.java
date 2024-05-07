package com.example.namesplitter.exception;

import com.example.namesplitter.model.Position;

public class NameSplitterException extends Exception {

    private Position loc;

    public NameSplitterException(String message) {
        super(message);
    }

    public NameSplitterException(String message, Position loc) {
        super(message);
        this.loc = loc;
    }

    public int getStartPos(){
        return loc.start();
    }

    public int getEndPos(){
        return loc.end();
    }
}
