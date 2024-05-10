package org.example;

import com.google.common.collect.ImmutableSet;
import org.example.data_structures.Coordinate;

public record Piece(Color color, Shape shape, boolean hasMoved) {
    private ImmutableSet<Coordinate> getAllPossibleMoves() {
        return null;
    }
}
