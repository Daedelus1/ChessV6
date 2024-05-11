package org.example;

import org.example.data_structures.Coordinate;

public class Main {
    public static void main(String[] args) {
        System.out.println(new GameBoard().toDisplayString());
        System.out.println(new GameBoard().getItemAtCoordinate(new Coordinate(3, 0)));
        System.out.println(new GameBoard().move(new Coordinate(0, 0), new Coordinate(2, 2)).toDisplayString());
    }
}
