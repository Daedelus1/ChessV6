package org.example.machinery;

public enum Shape {
    KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN, EN_PASSANT_GHOST;
    
    public int getPointValue() {
        return switch (this) {
            case PAWN -> 1;
            case BISHOP, KNIGHT -> 3;
            case ROOK -> 5;
            case QUEEN -> 10;
            case KING -> 100000;
            case EN_PASSANT_GHOST -> throw new IllegalArgumentException("GHOST TILE HAS NO VALUE");
        };
    }
    
    @Override
    public String toString() {
        return switch (this) {
            case KING -> "♔";
            case QUEEN -> "♕";
            case ROOK -> "♖";
            case BISHOP -> "♗";
            case KNIGHT -> "♘";
            case PAWN -> "♙";
            case EN_PASSANT_GHOST -> ".";
        };
    }
}
