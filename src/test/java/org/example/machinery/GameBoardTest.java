package org.example.machinery;

import com.google.common.collect.ImmutableSet;
import org.example.data_structures.Coordinate;
import org.example.data_structures.Dimension;
import org.example.data_structures.Matrix;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.example.machinery.Shape.BISHOP;
import static org.example.machinery.Shape.KING;
import static org.example.machinery.Shape.KNIGHT;
import static org.example.machinery.Shape.PAWN;
import static org.example.machinery.Shape.QUEEN;
import static org.example.machinery.Shape.ROOK;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SuppressWarnings("SpellCheckingInspection")
class GameBoardTest {
    
    @SuppressWarnings("SpellCheckingInspection")
    @Test
    void moveTest() {
        record TestCase(Piece pieceThatIsMoving, Coordinate pieceLocation,
                        Matrix<Optional<IllegalMoveException>> expectedOutput) {
        }
        //noinspection SpellCheckingInspection
        ImmutableSet<TestCase> cases = ImmutableSet.<TestCase>builder()
                .add(new TestCase(new Piece(Team.WHITE, ROOK, false), new Coordinate(3, 3),
                        new Matrix<>(new Dimension(8, 8), coord -> (
                                """
                                        oooxoooo
                                        oooxoooo
                                        oooxoooo
                                        xxxoxxxx
                                        oooxoooo
                                        oooxoooo
                                        oooxoooo
                                        oooxoooo""".replaceAll("\n", "").charAt(coord.toIndex(8)) == 'x') ? Optional.empty() :
                                Optional.of(new IllegalMoveException()))))
                .add(new TestCase(new Piece(Team.WHITE, PAWN, false), new Coordinate(3, 3),
                        new Matrix<>(new Dimension(8, 8), coord -> (
                                """
                                        oooooooo
                                        oooooooo
                                        oooooooo
                                        oooooooo
                                        oooxoooo
                                        oooxoooo
                                        oooooooo
                                        oooooooo""".replaceAll("\n", "").charAt(coord.toIndex(8)) == 'x') ? Optional.empty() :
                                Optional.of(new IllegalMoveException()))))
                .add(new TestCase(new Piece(Team.WHITE, PAWN, true), new Coordinate(3, 3),
                        new Matrix<>(new Dimension(8, 8), coord -> (
                                """
                                        oooooooo
                                        oooooooo
                                        oooooooo
                                        oooooooo
                                        oooxoooo
                                        oooooooo
                                        oooooooo
                                        oooooooo""".replaceAll("\n", "").charAt(coord.toIndex(8)) == 'x') ? Optional.empty() :
                                Optional.of(new IllegalMoveException()))))
                .add(new TestCase(new Piece(Team.BLACK, PAWN, false), new Coordinate(3, 3),
                        new Matrix<>(new Dimension(8, 8), coord -> (
                                """
                                        oooooooo
                                        oooxoooo
                                        oooxoooo
                                        oooooooo
                                        oooooooo
                                        oooooooo
                                        oooooooo
                                        oooooooo""".replaceAll("\n", "").charAt(coord.toIndex(8)) == 'x') ? Optional.empty() :
                                Optional.of(new IllegalMoveException()))))
                .add(new TestCase(new Piece(Team.BLACK, PAWN, true), new Coordinate(3, 3),
                        new Matrix<>(new Dimension(8, 8), coord -> (
                                """
                                        oooooooo
                                        oooooooo
                                        oooxoooo
                                        oooooooo
                                        oooooooo
                                        oooooooo
                                        oooooooo
                                        oooooooo""".replaceAll("\n", "").charAt(coord.toIndex(8)) == 'x') ? Optional.empty() :
                                Optional.of(new IllegalMoveException()))))
                .add(new TestCase(new Piece(Team.WHITE, BISHOP, false), new Coordinate(3, 3),
                        new Matrix<>(new Dimension(8, 8), coord -> (
                                """
                                        xoooooxo
                                        oxoooxoo
                                        ooxoxooo
                                        oooooooo
                                        ooxoxooo
                                        oxoooxoo
                                        xoooooxo
                                        ooooooox""".replaceAll("\n", "").charAt(coord.toIndex(8)) == 'x') ? Optional.empty() :
                                Optional.of(new IllegalMoveException()))))
                .add(new TestCase(new Piece(Team.WHITE, QUEEN, false), new Coordinate(3, 3),
                        new Matrix<>(new Dimension(8, 8), coord -> (
                                """
                                        xooxooxo
                                        oxoxoxoo
                                        ooxxxooo
                                        xxxoxxxx
                                        ooxxxooo
                                        oxoxoxoo
                                        xooxooxo
                                        oooxooox""".replaceAll("\n", "").charAt(coord.toIndex(8)) == 'x') ? Optional.empty() :
                                Optional.of(new IllegalMoveException()))))
                .add(new TestCase(new Piece(Team.WHITE, KNIGHT, false), new Coordinate(3, 3),
                        new Matrix<>(new Dimension(8, 8), coord -> (
                                """
                                        oooooooo
                                        ooxoxooo
                                        oxoooxoo
                                        oooooooo
                                        oxoooxoo
                                        ooxoxooo
                                        oooooooo
                                        oooooooo""".replaceAll("\n", "").charAt(coord.toIndex(8)) == 'x') ? Optional.empty() :
                                Optional.of(new IllegalMoveException()))))
                .add(new TestCase(new Piece(Team.WHITE, KING, false), new Coordinate(3, 3),
                        new Matrix<>(new Dimension(8, 8), coord -> (
                                """
                                        oooooooo
                                        oooooooo
                                        ooxxxooo
                                        ooxoxooo
                                        ooxxxooo
                                        oooooooo
                                        oooooooo
                                        oooooooo""".replaceAll("\n", "").charAt(coord.toIndex(8)) == 'x') ? Optional.empty() :
                                Optional.of(new IllegalMoveException()))))
                .build();
        cases.forEach(testCase -> {
            System.out.println(testCase);
            GameBoard board = new GameBoard(new Dimension(8, 8), coordinate -> {
                if (coordinate.equals(testCase.pieceLocation)) {
                    return Optional.of(testCase.pieceThatIsMoving());
                }
                return Optional.empty();
            });
            board.getMatrixDimensions().toRegion().allCoordinatesInRegion().forEach(poi -> {
                if (testCase.expectedOutput.getItemAtCoordinate(poi).isPresent()) {
                    assertThrows(testCase.expectedOutput.getItemAtCoordinate(poi).get().getClass(),
                            () -> board.move(testCase.pieceLocation, poi));
                } else {
                    assertDoesNotThrow(() -> board.move(testCase.pieceLocation, poi));
                }
            });
        });
    }
}
