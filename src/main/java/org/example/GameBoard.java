package org.example;


import org.example.data_structures.Dimension;
import org.example.data_structures.Matrix;

import java.util.Optional;

import static org.example.Shape.*;


public class GameBoard extends Matrix<Optional<Piece>> {


    public GameBoard() {
        super(new Dimension(8, 8), coordinate -> {
            String foo = "RNBQKBNRPPPPPPPP................................pppppppprnbqkbnr";
            char pieceChar = foo.charAt(coordinate.toIndex(8));
            if (pieceChar == '.') {
                return Optional.empty();
            }
            return Optional.of(new Piece(Character.isUpperCase(pieceChar) ? Color.BLACK : Color.WHITE,
                    switch (Character.toLowerCase(pieceChar)) {
                        case 'r' -> ROOK;
                        case 'n' -> KNIGHT;
                        case 'b' -> BISHOP;
                        case 'k' -> KING;
                        case 'q' -> QUEEN;
                        case 'p' -> PAWN;
                        default -> throw new IllegalStateException("Unexpected value:");
                    }, false));
        });
    }
}
