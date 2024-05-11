package org.example;

import com.google.common.collect.ImmutableSet;
import org.example.data_structures.Coordinate;

import static org.example.ConsoleColors.ColorType.BOLD;
import static org.example.ConsoleColors.ColorType.BOLD_BRIGHT;
import static org.example.ConsoleColors.ColorType.BRIGHT;
import static org.example.ConsoleColors.ColorValue.BLACK;

public record Piece(Team team, Shape shape, boolean hasMoved) {
    private ImmutableSet<Coordinate> getAllPossibleMoves() {
        return null;
    }


    @Override
    public String toString() {
        if (shape == Shape.EN_PASSANT_GHOST) {
            return ConsoleColors.colorize(".", BLACK, BRIGHT);
        }
        return ConsoleColors.colorize(shape.toString(), team.toColor(), team.toColor() == BLACK ? BOLD : BOLD_BRIGHT);
    }
}
