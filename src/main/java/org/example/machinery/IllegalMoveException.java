package org.example.machinery;

import org.example.data_structures.Coordinate;

public class IllegalMoveException extends RuntimeException {
    public IllegalMoveException(String message) {
        super(message);
    }
    public IllegalMoveException(Coordinate start, Coordinate end) {
        super(String.format("MOVE[%s, %s] IS ILLEGAL", start, end));
    }

    public IllegalMoveException() {
        super();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IllegalMoveException && ((IllegalMoveException) obj).getMessage().equals(this.getMessage());
    }
}
