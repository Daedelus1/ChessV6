package org.example;

public class IllegalMoveException extends RuntimeException {
    public IllegalMoveException(String message) {
        super(message);
    }

    public IllegalMoveException() {
        super();
    }
}
