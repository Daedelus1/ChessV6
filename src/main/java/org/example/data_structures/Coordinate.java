package org.example.data_structures;

import org.jetbrains.annotations.NotNull;

public record Coordinate(int x, int y) implements Comparable<Coordinate> {
    public Coordinate add(Coordinate other) {
        return new Coordinate(this.x + other.x, this.y + other.y);
    }

    public Coordinate subtract(Coordinate other) {
        return new Coordinate(this.x - other.x, this.y - other.y);
    }

    public int toIndex(int width) {
        return this.y * width + this.x;
    }

    @Override
    public int compareTo(@NotNull Coordinate other) {
        Coordinate delta = this.subtract(other);
        return delta.x() != 0 ? delta.x() : delta.y();
    }
}
