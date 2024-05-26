package org.example.machinery;

public enum Shape {
    KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN, EN_PASSANT_GHOST;
    
    @Override
    public String toString() {
        return switch (this) {
            case KING -> "K";
            case QUEEN -> "Q";
            case ROOK -> "R";
            case BISHOP -> "B";
            case KNIGHT -> "N";
            case PAWN -> "P";
            case EN_PASSANT_GHOST -> ".";
        };
    }
}
