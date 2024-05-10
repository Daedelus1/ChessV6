package org.example.data_structures;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Matrix<T> {
    private final ImmutableList<T> matrix;
    private final Dimension matrixDimensions;

    public Matrix(Dimension matrixDimensions, Function<Coordinate, T> CoordinateItemConverter) {
        this.matrixDimensions = matrixDimensions;
        List<T> tempData = new ArrayList<>(matrixDimensions.width() * matrixDimensions.height());

        matrixDimensions.toRegion().allCoordinatesInRegion().forEach(coordinate ->
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
        return matrix.get(point.toIndex(matrixDimensions.width()));
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

    public String toDisplayString() {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < matrix.size(); i++) {
            out.append(matrix.get(i).toString());
            if ((i + 1) % matrixDimensions.width() == 0) {
                out.append("\n");
            }
        }
        return out.toString();
    }
}
