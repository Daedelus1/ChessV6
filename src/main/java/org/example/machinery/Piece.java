package org.example.machinery;

import com.google.common.collect.ImmutableSet;
import org.example.ConsoleColors;
import org.example.data_structures.Coordinate;

import static org.example.ConsoleColors.ColorType.*;
import static org.example.ConsoleColors.ColorValue.BLACK;

public record Piece(Team team, Shape shape, boolean hasMoved) {
    private ImmutableSet<Coordinate> getAllPossibleMoves(GameBoard board, Coordinate pointOfOrigin) {
        return board.getAllPoints().stream().filter(poi -> board.getItemAtCoordinate(poi).isEmpty() ||
                board.getItemAtCoordinate(poi).get().team != this.team).filter(c -> switch (this.shape) {
            case PAWN -> pointOfOrigin.subtract(c).y() == (this.team != Team.WHITE ? 1 : -1)
                    && pointOfOrigin.difference(c).x() <= 1 || !this.hasMoved()
                    && pointOfOrigin.subtract(c).equals(new Coordinate(0, this.team != Team.WHITE ? 2 : -2));
            case ROOK -> pointOfOrigin.difference(c).x() == 0
                    || pointOfOrigin.difference(c).y() == 0;
            case BISHOP -> pointOfOrigin.difference(c).x() == pointOfOrigin.difference(c).y();
            case QUEEN -> pointOfOrigin.subtract(c).x() == 0
                    || pointOfOrigin.subtract(c).y() == 0
                    || pointOfOrigin.difference(c).x() == pointOfOrigin.difference(c).y();
            case KNIGHT -> pointOfOrigin.difference(c).equals(new Coordinate(1, 2))
                    || pointOfOrigin.difference(c).equals(new Coordinate(2, 1));
            case KING -> pointOfOrigin.difference(c).y() <= 1
                    && pointOfOrigin.difference(c).x() <= 1;
            default -> throw new IllegalArgumentException(this + " DOES NOT HAVE ANY MOVES");
        }).collect(ImmutableSet.toImmutableSet());
    }

    public ImmutableSet<Coordinate> getAllValidMoves(GameBoard board, Coordinate pointOfOrigin) {
        return this.getAllPossibleMoves(board, pointOfOrigin);
        //TODO: Add advanced legality checks
    }

    @Override
    public String toString() {
        if (shape == Shape.EN_PASSANT_GHOST) {
            return ConsoleColors.colorize(".", BLACK, BRIGHT);
        }
        return ConsoleColors.colorize(shape.toString(), team.toColor(), team.toColor() == BLACK ? BOLD : BOLD_BRIGHT);
    }
}
