package com.example.namesplitter.exception;

import com.example.namesplitter.model.Position;

/**
 * This exception is the base class for all exceptions thrown by the NameSplitter
 */
public class NameSplitterException extends RuntimeException {

    private Position loc;
    
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

    public void setPosition(Position loc){
        this.loc = loc;
    }
}
