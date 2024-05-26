package org.example.machinery;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.example.ConsoleColors;
import org.example.data_structures.Coordinate;
import org.example.data_structures.Dimension;
import org.example.data_structures.Matrix;

import java.util.Optional;
import java.util.function.Function;

import static org.example.ConsoleColors.ColorType.BRIGHT;
import static org.example.ConsoleColors.ColorType.DEFAULT;
import static org.example.ConsoleColors.ColorValue.BLACK;
import static org.example.ConsoleColors.ColorValue.WHITE;
import static org.example.machinery.Shape.BISHOP;
import static org.example.machinery.Shape.EN_PASSANT_GHOST;
import static org.example.machinery.Shape.KING;
import static org.example.machinery.Shape.KNIGHT;
import static org.example.machinery.Shape.PAWN;
import static org.example.machinery.Shape.QUEEN;
import static org.example.machinery.Shape.ROOK;


@SuppressWarnings("SpellCheckingInspection")
public class GameBoard extends Matrix<Optional<Piece>> {
    private final ImmutableList<ImmutableList<Optional<Piece>>> history;
    private final Team movingTeam;
    
    public GameBoard() {
        super(new Dimension(8, 8), coordinate -> {
            char pieceChar = "RNBQKBNRPPPPPPPP................................pppppppprnbqkbnr"
                    .charAt(coordinate.toIndex(8));
            return pieceChar == '.' ? Optional.empty() :
                    Optional.of(new Piece(Character.isUpperCase(pieceChar) ? Team.WHITE : Team.BLACK,
                            switch (Character.toLowerCase(pieceChar)) {
                                case 'r' -> ROOK;
                                case 'n' -> KNIGHT;
                                case 'b' -> BISHOP;
                                case 'k' -> KING;
                                case 'q' -> QUEEN;
                                case 'p' -> PAWN;
                                default -> throw new IllegalStateException("Unexpected value:");
                            }, false));
        });
        history = ImmutableList.of();
        movingTeam = Team.WHITE;
    }
    
    protected GameBoard(Dimension dimension, Function<Coordinate, Optional<Piece>> converter, ImmutableList<ImmutableList<Optional<Piece>>> history, Team movingTeam) {
        super(dimension, converter);
        this.history = history;
        this.movingTeam = movingTeam;
    }
    
    public Team getMovingTeam() {
        return movingTeam;
    }
    
    protected boolean isInCheck(Team teamWhoIsChecked) {
        Optional<Coordinate> kingLocation = this.getAllPoints().stream().filter(c -> {
            Optional<Piece> item = getItemAtCoordinate(c);
            return item.isPresent() && item.get().shape() == KING
                   && item.get().team() == teamWhoIsChecked;
        }).findFirst();
        return kingLocation.filter(value -> this.getMatrixDimensions().toRegion().allCoordinatesInRegion().stream()
                .anyMatch(coordinate -> this.getItemAtCoordinate(coordinate).isPresent()
                                        && this.getItemAtCoordinate(coordinate).get().team() != teamWhoIsChecked
                                        && this.getItemAtCoordinate(coordinate).get()
                                                .getAllPossibleMoves(this, coordinate).contains(value))).isPresent();
    }
    
    public ImmutableMap<Coordinate, ImmutableSet<Coordinate>> getAllValidMoves() {
        ImmutableMap.Builder<Coordinate, ImmutableSet<Coordinate>> builder = ImmutableMap.builder();
        this.getAllPoints().stream().filter(poi -> this.getItemAtCoordinate(poi).isPresent() &&
                                                   (this.getItemAtCoordinate(poi).get().team() == this.movingTeam))
                .forEach(poi -> builder.put(poi, this.getItemAtCoordinate(poi).get().getAllValidMoves(this, poi)));
        return builder.build();
    }
    
    
    
    
    protected boolean pointIsInBounds(Coordinate point) {
        return this.getMatrixDimensions().toRegion().contains(point);
    }
    
    
    public GameBoard move(Coordinate start, Coordinate end) {
        Optional<Piece> movingItem = getItemAtCoordinate(start);
        Optional<Piece> overridenItem = getItemAtCoordinate(end);
        if (movingItem.isEmpty()) {
            throw new EmptyTileException("TILE IS EMPTY");
        }
        if (movingItem.get().team() != this.movingTeam){
            throw new WrongTeamException(String.format("MOVE[%s, %s] IS USING THE WRONG TEAM", start, end));
        }
        if (!movingItem.get().getAllValidMoves(this, start).contains(end)) {
            throw new IllegalMoveException(start, end);
        }
        // en passant
        if (overridenItem.isPresent() && overridenItem.get().shape() == EN_PASSANT_GHOST && movingItem.get().shape() == PAWN) {
            Team ghostTeam = getItemAtCoordinate(end).get().team();
            return blindMove(start, end, movingTeam.invert()).deleteItem(end.add(0, ghostTeam == Team.WHITE ? 1 : -1));
        }
        // castling
        if (movingItem.get().shape() == KING && start.difference(end).equals(2, 0)) {
            return blindMove(start, end, movingTeam.invert())
                    .blindMove(start.add(3, 0), end.add(-1, 0), movingTeam.invert());
        }
        return blindMove(start, end, movingTeam.invert());
    }
    
    protected GameBoard blindMove(Coordinate start, Coordinate end, Team nextMovingTeam) {
        return new GameBoard(this.getMatrixDimensions(), coordinate -> {
            if (coordinate.equals(start)) {
                return Optional.empty();
            } else if (coordinate.equals(end)) {
                return this.getItemAtCoordinate(start);
            } else {
                return this.getItemAtCoordinate(coordinate);
            }
        }, this.history, nextMovingTeam);
    }
    private GameBoard deleteItem(Coordinate pointOfItem) {
        return new GameBoard(this.getMatrixDimensions(), coordinate -> {
            if (coordinate.equals(pointOfItem)) {
                return Optional.empty();
            } else {
                return this.getItemAtCoordinate(coordinate);
            }
        }, this.history, this.movingTeam);
    }
    
    public GameBoard move(String moveEncoding) {
        return move(new Coordinate(moveEncoding.charAt(0) - 'a', moveEncoding.charAt(1) - '1'),
                new Coordinate(moveEncoding.charAt(2) - 'a', moveEncoding.charAt(3) - '1'));
    }
    
    public String toDisplayString() {
        String header = "  a  b  c  d  e  f  g  h \n";
        StringBuilder out = new StringBuilder(header);
        for (int row = 7; row >= 0; row--) {
            out.append(row + 1);
            for (int col = 0; col < 8; col++) {
                Optional<Piece> itemAtCoordinate = this.getItemAtCoordinate(new Coordinate(col, row));
                if (itemAtCoordinate.isPresent()) {
                    out.append(' ').append(itemAtCoordinate.get()).append(' ');
                } else {
                    boolean isOddTile = (row + col) % 2 == 0;
                    out.append(ConsoleColors.colorize(" x ", isOddTile ? BLACK : WHITE,
                            isOddTile ? BRIGHT : DEFAULT));
                }
            }
            out.append(row + 1).append("\n");
        }
        return out.append(header).toString();
    }
}
