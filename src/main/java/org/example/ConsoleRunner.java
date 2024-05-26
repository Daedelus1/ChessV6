package org.example;

import org.example.machinery.GameBoard;
import org.example.machinery.IllegalMoveException;
import org.example.machinery.WrongTeamException;

import java.util.Scanner;

public class ConsoleRunner {
    
    public static void newGame(GameBoard board) {
        Scanner input = new Scanner(System.in);
        int turn = 0;
        GameBoard gameBoard = board;
        while (true) {
            System.out.println(gameBoard.toDisplayString());
            do {
                try {
                    System.out.printf("%s is moving! what is your move?\n", gameBoard.getMovingTeam());
                    gameBoard = gameBoard.move(input.nextLine());
                    turn++;
                    break;
                } catch (IllegalMoveException e) {
                    System.out.println("That move is Illegal! please try again");
                    throw new RuntimeException(e);
                } catch (IllegalArgumentException e) {
                    System.out.println("Please Try Again");
                } catch (WrongTeamException e) {
                    System.out.printf("You must move a %s piece!\n", gameBoard.getMovingTeam());
                }
            } while (true);
        }
    }
    
    public static void newGame() {
        newGame(new GameBoard());
    }
    
    public static void newGame(String... startingMoves) {
        GameBoard board = new GameBoard();
        for (String move : startingMoves) {
            board = board.move(move);
        }
        newGame(board);
    }
}
