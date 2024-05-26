package org.example.data_structures;

import com.google.common.collect.ImmutableSet;

import java.util.stream.IntStream;

/**
 * @param startIncl Inclusive
 * @param endIncl   Inclusive
 */
public record Region(Coordinate startIncl, Coordinate endIncl) {
    public static Region newRegion(int startXIncl, int startYIncl, int endXIncl, int endYIncl) {
        return new Region(new Coordinate(startXIncl, startYIncl), new Coordinate(endXIncl, endYIncl));
    }
    
    public boolean contains(Coordinate point) {
        return point.x() >= startIncl.x() && point.x() <= endIncl.x() &&
               point.y() >= startIncl.y() && point.y() <= endIncl.y() ||
               point.x() <= startIncl.x() && point.x() >= endIncl.x() &&
               point.y() <= startIncl.y() && point.y() >= endIncl.y();
    }
    
    public Region shift(Coordinate offset) {
        return new Region(startIncl.add(offset), endIncl.add(offset));
    }
    
    public Region inverseShift(Coordinate offset) {
        return new Region(startIncl.subtract(offset), endIncl.subtract(offset));
    }
    
    public ImmutableSet<Coordinate> allCoordinatesInRegion() {
        return IntStream.range(startIncl.y(), endIncl.y() + 1).boxed().flatMap(row ->
                        IntStream.range(startIncl.x(), endIncl.x() + 1).mapToObj(col -> new Coordinate(col, row)))
                .sorted().collect(ImmutableSet.toImmutableSet());
    }
    
    
    public int getWidth() {
        return Math.abs(endIncl.x() - startIncl.x());
    }
    
    public int getHeight() {
        return Math.abs(endIncl.y() - startIncl.y());
    }
}
