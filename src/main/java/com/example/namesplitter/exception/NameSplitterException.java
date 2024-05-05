package com.example.namesplitter.exception;

import com.example.namesplitter.model.Location;

public class NameSplitterException extends RuntimeException {

    private Location loc;

    public NameSplitterException(String message) {
        super(message);
    }

    public NameSplitterException(String message, Location loc) {
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
