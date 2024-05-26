package org.example.machinery;

import org.example.data_structures.Coordinate;

public enum Direction {
    NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST;
    
    public Coordinate shift(Coordinate start, int distance) {
        return start.add((switch (this) {
            case NORTH -> new Coordinate(0, 1);
            case EAST -> new Coordinate(1, 0);
            case SOUTH -> new Coordinate(0, -1);
            case WEST -> new Coordinate(-1, 0);
            case NORTH_EAST -> new Coordinate(1, 1);
            case NORTH_WEST -> new Coordinate(1, -1);
            case SOUTH_WEST -> new Coordinate(-1, -1);
            case SOUTH_EAST -> new Coordinate(-1, 1);
        }).multiply(distance));
    }
}
