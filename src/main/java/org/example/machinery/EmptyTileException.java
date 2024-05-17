package org.example.machinery;

public class EmptyTileException extends IllegalMoveException {
    
    public EmptyTileException(String message) {
        super(message);
    }
    
    public EmptyTileException() {
        super();
    }
}
