package org.example.data_structures;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Matrix<T> {
    protected final ImmutableList<T> matrix;
    private final Dimension matrixDimensions;
    
    public Matrix(Dimension matrixDimensions, Function<Coordinate, T> CoordinateItemConverter) {
        this.matrixDimensions = matrixDimensions;
        List<T> tempData = new ArrayList<>(matrixDimensions.width() * matrixDimensions.height());
        
        matrixDimensions.toRegion().allCoordinatesInRegion().stream().sorted(Coordinate::compareTo).forEach(coordinate ->
                tempData.add(CoordinateItemConverter.apply(coordinate)));
        
        if (tempData.contains(null)) {
            throw new IllegalArgumentException("DATA CONTAINS NULL");
        }
        matrix = ImmutableList.copyOf(tempData);
    }
    
    public ImmutableSet<Coordinate> getAllPoints() {
        return this.matrixDimensions.toRegion().allCoordinatesInRegion();
    }
    
    public T getItemAtCoordinate(Coordinate point) {
        Region region = matrixDimensions.toRegion();
        if (!region.contains(point)) {
            throw new IndexOutOfBoundsException();
        }
        return matrix.get(point.y() * matrixDimensions.width() + point.x());
    }
    
    public Dimension getMatrixDimensions() {
        return matrixDimensions;
    }
    
    private ImmutableList<T> getMatrix() {
        return matrix;
    }
    
    @Override
    public boolean equals(Object other) {
        return other instanceof Matrix<?> &&
               this.matrixDimensions.equals(((Matrix<?>) other).matrixDimensions) &&
               this.matrix.equals(((Matrix<?>) other).getMatrix());
    }
    
    @Override
    public String toString() {
        return String.format("Matrix[Dimensions: %s | Data = %s]", matrixDimensions, matrix.toString());
    }
    
    
    public String toDisplayString(Function<T, String> itemConverter, String delimiter) {
        StringBuilder out = new StringBuilder();
        matrix.stream().map(itemConverter).forEach(out::append);
        IntStream.range(this.matrixDimensions.height(), -1).forEach(i ->
                out.insert(i * this.getMatrixDimensions().width(), delimiter));
        return out.toString();
    }
}
