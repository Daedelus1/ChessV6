package org.example.data_structures;

import java.util.Arrays;
import java.util.function.Function;

public class MatrixFactory {
    public static <T> Matrix<T> stringToMatrix(String seed, Function<Character, T> converter) {
        String[] temp = seed.split("\\n");
        if (Arrays.stream(temp).anyMatch(line -> line.trim().length() != temp[0].trim().length())) {
            throw new IllegalArgumentException("STRING DOES NOT HAVE CONSISTENT WIDTH");
        }
        Dimension matrixDimension = new Dimension(temp[0].length(), temp.length);
        return new Matrix<>(matrixDimension, point ->
                converter.apply(temp[point.y()].charAt(point.x())));

    }
}
