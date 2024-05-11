package org.example;


import org.example.data_structures.Coordinate;
import org.example.data_structures.Dimension;
import org.example.data_structures.Matrix;

import java.util.Optional;
import java.util.function.Function;

import static org.example.ConsoleColors.ColorType.BRIGHT;
import static org.example.ConsoleColors.ColorType.DEFAULT;
import static org.example.ConsoleColors.ColorValue.BLACK;
import static org.example.ConsoleColors.ColorValue.WHITE;
import static org.example.Shape.BISHOP;
import static org.example.Shape.KING;
import static org.example.Shape.KNIGHT;
import static org.example.Shape.PAWN;
import static org.example.Shape.QUEEN;
import static org.example.Shape.ROOK;


public class GameBoard extends Matrix<Optional<Piece>> {
    public GameBoard() {
        super(new Dimension(8, 8), coordinate -> {
            char pieceChar = "RNBQKBNRPPPPPPPP................................pppppppprnbqkbnr"
                    .charAt(coordinate.toIndex(8));
            return pieceChar == '.' ? Optional.empty() :
                    Optional.of(new Piece(Character.isUpperCase(pieceChar) ? Team.WHITE : Team.BLACK,
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

    private GameBoard(Dimension dimension, Function<Coordinate, Optional<Piece>> converter) {
        super(dimension, converter);
    }


    public GameBoard move(Coordinate start, Coordinate end) {
        return new GameBoard(this.getMatrixDimensions(), coordinate -> {
            if (coordinate.equals(start)) return Optional.empty();
            else if (coordinate.equals(end)) return this.getItemAtCoordinate(start);
            else return this.getItemAtCoordinate(coordinate);
        });
    }

    public GameBoard move(String moveEncoding) {
        return move(new Coordinate(moveEncoding.charAt(0) - 'a', moveEncoding.charAt(1) - '1'),
                new Coordinate(moveEncoding.charAt(2) - 'a', moveEncoding.charAt(3) - '1'));
    }

    @Override
    public String toDisplayString() {
        String header = "  a  b  c  d  e  f  g  h \n";
        StringBuilder out = new StringBuilder(header);
        for (int row = 7; row >= 0; row--) {
            out.append(row + 1);
            for (int col = 0; col < 8; col++) {
                Optional<Piece> itemAtCoordinate = this.getItemAtCoordinate(new Coordinate(col, row));
                if (itemAtCoordinate.isPresent()) {
                    out.append(itemAtCoordinate.get());
                } else {
                    boolean isOddTile = (row + col) % 2 == 0;
                    out.append(ConsoleColors.colorize(" x ", isOddTile ? BLACK : WHITE,
                            isOddTile ? BRIGHT : DEFAULT));
                }
            }
            out.append(row + 1).append("\n");
        }
        return out.append(header).toString();
    }
}
