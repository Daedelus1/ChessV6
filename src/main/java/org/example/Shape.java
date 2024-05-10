package org.example;

public enum Shape {
    PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING, EN_PASSANT_GHOST;

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


}
