package org.example.machinery;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.example.ConsoleColors;
import org.example.data_structures.Coordinate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.example.ConsoleColors.ColorType.BOLD;
import static org.example.ConsoleColors.ColorType.BOLD_BRIGHT;
import static org.example.ConsoleColors.ColorType.BRIGHT;
import static org.example.ConsoleColors.ColorValue.BLACK;
import static org.example.machinery.Piece.Direction.EAST;
import static org.example.machinery.Piece.Direction.NORTH;
import static org.example.machinery.Piece.Direction.NORTH_EAST;
import static org.example.machinery.Piece.Direction.NORTH_WEST;
import static org.example.machinery.Piece.Direction.SOUTH;
import static org.example.machinery.Piece.Direction.SOUTH_EAST;
import static org.example.machinery.Piece.Direction.SOUTH_WEST;
import static org.example.machinery.Piece.Direction.WEST;
import static org.example.machinery.Shape.EN_PASSANT_GHOST;
import static org.example.machinery.Shape.ROOK;

public record Piece(Team team, Shape shape, boolean hasMoved) {
    ImmutableSet<Coordinate> getAllPossibleMoves(GameBoard board, Coordinate pointOfOrigin) {
        Piece movingPiece = board.getItemAtCoordinate(pointOfOrigin).get();
        Function<Optional<Piece>, EndType> defaultDeterminer = item -> {
            if (item.isEmpty() || item.get().shape == EN_PASSANT_GHOST) {
                return EndType.CONTINUE;
            } else if (item.get().team == movingPiece.team) {
                return EndType.END_NOW;
            } else {
                return EndType.ADD_THEN_END;
            }
        };
        return switch (this.shape) {
            case ROOK -> drawRays(board, pointOfOrigin, defaultDeterminer, NORTH, SOUTH, EAST, WEST);
            case BISHOP ->
                    drawRays(board, pointOfOrigin, defaultDeterminer, NORTH_EAST, SOUTH_EAST, NORTH_WEST, SOUTH_WEST);
            case QUEEN ->
                    drawRays(board, pointOfOrigin, defaultDeterminer, NORTH, SOUTH, EAST, WEST, NORTH_EAST, SOUTH_EAST, NORTH_WEST, SOUTH_WEST);
            case KNIGHT -> ImmutableSet.<Coordinate>builder()
                    .add(pointOfOrigin.add(1, 2))
                    .add(pointOfOrigin.add(-1, 2))
                    .add(pointOfOrigin.add(2, 1))
                    .add(pointOfOrigin.add(-2, 1))
                    .add(pointOfOrigin.add(1, -2))
                    .add(pointOfOrigin.add(-1, -2))
                    .add(pointOfOrigin.add(2, -1))
                    .add(pointOfOrigin.add(-2, -1))
                    .build().stream()
                    .filter(poi -> board.getMatrixDimensions().toRegion().contains(poi))
                    .filter(c -> board.getItemAtCoordinate(c).isEmpty()
                                 || board.getItemAtCoordinate(c).get().team != this.team)
                    .collect(ImmutableSet.toImmutableSet());
            case KING -> {
                ImmutableSet.Builder<Coordinate> builder = ImmutableSet.builder();
                Stream.of(pointOfOrigin.add(1, 1),
                                pointOfOrigin.add(-1, 1),
                                pointOfOrigin.add(1, -1),
                                pointOfOrigin.add(-1, -1),
                                pointOfOrigin.add(1, 0),
                                pointOfOrigin.add(-1, 0),
                                pointOfOrigin.add(0, 1),
                                pointOfOrigin.add(0, -1))
                        .filter(poi -> board.getMatrixDimensions().toRegion().contains(poi))
                        .filter(c -> {
                            if (board.getItemAtCoordinate(c).isEmpty()) return true;
                            Piece piece = board.getItemAtCoordinate(c).get();
                            return piece.team != this.team;
                        })
                        .forEach(builder::add);
                Direction[] order = new Direction[]{EAST, WEST};
                for (Direction direction : order) {
                    ImmutableList<Coordinate> ray = drawRay(board, pointOfOrigin, direction,
                            item -> item.isPresent() && (item.get().shape() != EN_PASSANT_GHOST) ? EndType.ADD_THEN_END : EndType.CONTINUE);
                    if (ray.isEmpty()) {
                        continue;
                    }
                    Coordinate closestNeighbor = ray.get(ray.size() - 1);
                    if (board.getItemAtCoordinate(closestNeighbor)
                                .equals(Optional.of(new Piece(this.team, ROOK, false)))
                        && (closestNeighbor.difference(pointOfOrigin).equals(3, 0)
                            || closestNeighbor.difference(pointOfOrigin).equals(4, 0))) {
                        builder.add(direction.shift(pointOfOrigin, 2));
                    }
                }
                yield builder.build();
            }
            case PAWN -> {
                ImmutableSet.Builder<Coordinate> builder = ImmutableSet.builder();
                Coordinate point = pointOfOrigin.add(0, this.team == Team.WHITE ? 1 : -1);
                if (board.pointIsInBounds(point) && board.getItemAtCoordinate(point).isEmpty()) {
                    builder.add(point);
                    if (board.pointIsInBounds(point) && !this.hasMoved) {
                        point = pointOfOrigin.add(0, this.team == Team.WHITE ? 2 : -2);
                        if (board.pointIsInBounds(point) && board.getItemAtCoordinate(point).isEmpty()) {
                            builder.add(point);
                        }
                    }
                }
                point = pointOfOrigin.add(1, this.team == Team.WHITE ? 1 : -1);
                
                if (board.pointIsInBounds(point) && board.getItemAtCoordinate(point).isPresent()
                    && board.getItemAtCoordinate(point).get().team != this.team) {
                    builder.add(point);
                }
                point = pointOfOrigin.add(-1, this.team == Team.WHITE ? 1 : -1);
                if (board.pointIsInBounds(point) && board.getItemAtCoordinate(point).isPresent()
                    && (board.getItemAtCoordinate(point).get().team != this.team
                        || board.getItemAtCoordinate(point).get().shape == EN_PASSANT_GHOST)) {
                    builder.add(point);
                }
                yield builder.build();
            }
            default -> throw new IllegalArgumentException("PIECE HAS NO MOVES");
        };
    }
    
    private ImmutableList<Coordinate> drawRay(@NotNull GameBoard board, @NotNull Coordinate startExcl,
                                              @NotNull Direction direction, Function<Optional<Piece>, EndType> determiner) {
        List<Coordinate> list = new ArrayList<>();
        if (board.getItemAtCoordinate(startExcl).isEmpty()) {
            throw new EmptyTileException();
        }
        Piece movingPiece = board.getItemAtCoordinate(startExcl).get();
loop:
        for (Coordinate p = direction.shift(startExcl, 1);
             board.getMatrixDimensions().toRegion().contains(p);
             p = direction.shift(p, 1)) {
            Optional<Piece> item = board.getItemAtCoordinate(p);
            switch (determiner.apply(item)) {
                case END_NOW -> {
                    break loop;
                }
                case ADD_THEN_END -> {
                    list.add(p);
                    break loop;
                }
                case CONTINUE -> list.add(p);
            }
        }
        return ImmutableList.copyOf(list);
    }
    
    private ImmutableSet<Coordinate> drawRays(@NotNull GameBoard board, @NotNull Coordinate startExcl,
                                              @NotNull Function<Optional<Piece>, EndType> determiner,
                                              @NotNull Direction... directions) {
        ImmutableSet.Builder<Coordinate> builder = ImmutableSet.builder();
        Arrays.stream(directions).map(direction -> drawRay(board, startExcl, direction, determiner))
                .forEach(builder::addAll);
        return builder.build();
    }
    
    public ImmutableSet<Coordinate> getAllValidMoves(GameBoard board, Coordinate pointOfOrigin) {
        return this.getAllPossibleMoves(board, pointOfOrigin).stream()
                .filter(poi -> !board.blindMove(pointOfOrigin, poi,
                        board.getMovingTeam().invert()).isInCheck(this.team))
                .collect(ImmutableSet.toImmutableSet());
        //TODO: Add advanced legality checks
    }
    
    @Override
    public String toString() {
        if (shape == EN_PASSANT_GHOST) {
            return ConsoleColors.colorize(".", BLACK, BRIGHT);
        }
        return ConsoleColors.colorize(shape.toString(), team.toColor(), team.toColor() == BLACK ? BOLD : BOLD_BRIGHT);
    }
    
    enum Direction {
        NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST;
        
        public Coordinate shift(Coordinate start, int distance) {
            return start.add((switch (this) {
                case NORTH -> new Coordinate(0, 1);
                case EAST -> new Coordinate(1, 0);
                case SOUTH -> new Coordinate(0, -1);
                case WEST -> new Coordinate(-1, 0);
                case NORTH_EAST -> new Coordinate(1, 1);
                case NORTH_WEST -> new Coordinate(1, -1);
                case SOUTH_WEST -> new Coordinate(-1, -1);
                case SOUTH_EAST -> new Coordinate(-1, 1);
            }).multiply(distance));
        }
    }
    
    private enum EndType {
        END_NOW, ADD_THEN_END, CONTINUE
    }
}
