package org.example.machinery;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.truth.Truth;
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
import static org.example.machinery.Team.BLACK;
import static org.example.machinery.Team.WHITE;

@SuppressWarnings("SpellCheckingInspection")
class GameBoardTest {
    
    private long count;
    
    @Test
    void basicMoveTest() {
        record TestCase(Piece pieceThatIsMoving, Coordinate pieceLocation,
                        Matrix<Optional<IllegalMoveException>> expectedOutput, Team movingTeam) {
        }
        ImmutableSet<TestCase> cases = ImmutableSet.<TestCase>builder()
                .add(new TestCase(new Piece(WHITE, ROOK, false), new Coordinate(3, 3),
                        new Matrix<>(new Dimension(8, 8), coord ->
                                ("""
                                         oooxoooo
                                         oooxoooo
                                         oooxoooo
                                         xxxoxxxx
                                         oooxoooo
                                         oooxoooo
                                         oooxoooo
                                         oooxoooo""".replaceAll("\n", "").charAt(coord.toIndex(8)) == 'x') ? Optional.empty() :
                                        Optional.of(new IllegalMoveException(new Coordinate(3, 3), coord))), WHITE))
                .add(new TestCase(new Piece(WHITE, PAWN, false), new Coordinate(3, 3),
                        new Matrix<>(new Dimension(8, 8), coord ->
                                ("""
                                         oooooooo
                                         oooooooo
                                         oooooooo
                                         oooooooo
                                         oooxoooo
                                         oooxoooo
                                         oooooooo
                                         oooooooo""".replaceAll("\n", "").charAt(coord.toIndex(8)) == 'x') ? Optional.empty() :
                                        Optional.of(new IllegalMoveException(new Coordinate(3, 3), coord))), WHITE))
                .add(new TestCase(new Piece(WHITE, PAWN, true), new Coordinate(3, 3),
                        new Matrix<>(new Dimension(8, 8), coord ->
                                ("""
                                         oooooooo
                                         oooooooo
                                         oooooooo
                                         oooooooo
                                         oooxoooo
                                         oooooooo
                                         oooooooo
                                         oooooooo""".replaceAll("\n", "").charAt(coord.toIndex(8)) == 'x') ? Optional.empty() :
                                        Optional.of(new IllegalMoveException(new Coordinate(3, 3), coord))), WHITE))
                .add(new TestCase(new Piece(BLACK, PAWN, false), new Coordinate(3, 3),
                        new Matrix<>(new Dimension(8, 8), coord ->
                                ("""
                                         oooooooo
                                         oooxoooo
                                         oooxoooo
                                         oooooooo
                                         oooooooo
                                         oooooooo
                                         oooooooo
                                         oooooooo""".replaceAll("\n", "").charAt(coord.toIndex(8)) == 'x') ? Optional.empty() :
                                        Optional.of(new IllegalMoveException(new Coordinate(3, 3), coord))), BLACK))
                .add(new TestCase(new Piece(BLACK, PAWN, true), new Coordinate(3, 3),
                        new Matrix<>(new Dimension(8, 8), coord ->
                                ("""
                                         oooooooo
                                         oooooooo
                                         oooxoooo
                                         oooooooo
                                         oooooooo
                                         oooooooo
                                         oooooooo
                                         oooooooo""".replaceAll("\n", "").charAt(coord.toIndex(8)) == 'x') ? Optional.empty() :
                                        Optional.of(new IllegalMoveException(new Coordinate(3, 3), coord))), BLACK))
                .add(new TestCase(new Piece(WHITE, BISHOP, false), new Coordinate(3, 3),
                        new Matrix<>(new Dimension(8, 8), coord ->
                                ("""
                                         xoooooxo
                                         oxoooxoo
                                         ooxoxooo
                                         oooooooo
                                         ooxoxooo
                                         oxoooxoo
                                         xoooooxo
                                         ooooooox""".replaceAll("\n", "").charAt(coord.toIndex(8)) == 'x') ? Optional.empty() :
                                        Optional.of(new IllegalMoveException(new Coordinate(3, 3), coord))), WHITE))
                .add(new TestCase(new Piece(WHITE, QUEEN, false), new Coordinate(3, 3),
                        new Matrix<>(new Dimension(8, 8), coord ->
                                ("""
                                         xooxooxo
                                         oxoxoxoo
                                         ooxxxooo
                                         xxxoxxxx
                                         ooxxxooo
                                         oxoxoxoo
                                         xooxooxo
                                         oooxooox""".replaceAll("\n", "").charAt(coord.toIndex(8)) == 'x') ? Optional.empty() :
                                        Optional.of(new IllegalMoveException(new Coordinate(3, 3), coord))), WHITE))
                .add(new TestCase(new Piece(WHITE, KNIGHT, false), new Coordinate(3, 3),
                        new Matrix<>(new Dimension(8, 8), coord ->
                                ("""
                                         oooooooo
                                         ooxoxooo
                                         oxoooxoo
                                         oooooooo
                                         oxoooxoo
                                         ooxoxooo
                                         oooooooo
                                         oooooooo""".replaceAll("\n", "").charAt(coord.toIndex(8)) == 'x') ? Optional.empty() :
                                        Optional.of(new IllegalMoveException(new Coordinate(3, 3), coord))), WHITE))
                .add(new TestCase(new Piece(WHITE, KING, false), new Coordinate(3, 3),
                        new Matrix<>(new Dimension(8, 8), coord ->
                                ("""
                                         oooooooo
                                         oooooooo
                                         ooxxxooo
                                         ooxoxooo
                                         ooxxxooo
                                         oooooooo
                                         oooooooo
                                         oooooooo""".replaceAll("\n", "").charAt(coord.toIndex(8)) == 'x') ? Optional.empty() :
                                        Optional.of(new IllegalMoveException(new Coordinate(3, 3), coord))), WHITE))
                .build();
        cases.forEach(testCase -> {
            System.out.println(testCase);
            GameBoard board = new GameBoard(new Dimension(8, 8), coordinate -> {
                if (coordinate.equals(testCase.pieceLocation)) {
                    return Optional.of(testCase.pieceThatIsMoving());
                }
                return Optional.empty();
            }, ImmutableList.of(), testCase.movingTeam);
            board.getMatrixDimensions().toRegion().allCoordinatesInRegion().forEach(poi -> {
                Optional<Exception> thrownItem;
                try {
                    board.move(testCase.pieceLocation, poi);
                    thrownItem = Optional.empty();
                } catch (Exception e) {
                    thrownItem = Optional.of(e);
                }
                Truth.assertThat(thrownItem).isEqualTo(testCase.expectedOutput.getItemAtCoordinate(poi));
            });
        });
    }
    
    @Test
    void perftTest() {
        for (int depth = 0; depth < 5; depth++) {
            count = 0;
            System.out.println(perft(new GameBoard(), depth));
        }
    }
    
    synchronized void addToCount(long amount) {
        count += amount;
    }
    
    private long perft(GameBoard gameBoard, int depth) {
        if (depth == 0){
            return 0;
        }
        record Move(Coordinate start, Coordinate end) {
        }
        ImmutableMap<Coordinate, ImmutableSet<Coordinate>> allMoves = gameBoard.getAllValidMoves();
        allMoves.entrySet().parallelStream().flatMap(entry -> {
            addToCount(entry.getValue().size());
            return entry.getValue().parallelStream().map(end -> new Move(entry.getKey(), end));
        }).map(move -> gameBoard.move(move.start, move.end)).forEach(newGameBoard -> perft(newGameBoard, depth - 1));
        return count;
    }
}
