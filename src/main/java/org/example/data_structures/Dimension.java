package org.example.data_structures;

public record Dimension(int width, int height) {
    public Region toRegion() {
        return new Region(new Coordinate(0, 0), new Coordinate(width - 1, height - 1));
    }
    
    public boolean isSmallerThan(Dimension other) {
        return this.height < other.height || this.width < other.width;
    }
}
