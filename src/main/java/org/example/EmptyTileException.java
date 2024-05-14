package org.example;

public class EmptyTileException extends IllegalMoveException {

    public EmptyTileException(String message) {
        super(message);
    }

    public EmptyTileException() {
        super();
    }
}
