package org.example;

import org.example.data_structures.Coordinate;
import org.example.machinery.GameBoard;

public class Main {
    public static void main(String[] args) {
        System.out.println(new GameBoard().toDisplayString());
        System.out.println(new GameBoard().getItemAtCoordinate(new Coordinate(3, 0)));
        System.out.println(new GameBoard().move(new Coordinate(0, 0), new Coordinate(0, 2)).toDisplayString());
    }
}
