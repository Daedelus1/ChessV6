package org.example.machinery;

public class IllegalMoveException extends RuntimeException {
    public IllegalMoveException(String message) {
        super(message);
    }

    public IllegalMoveException() {
        super();
    }
}
