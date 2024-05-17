package org.example.machinery;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.example.ConsoleColors;
import org.example.data_structures.Coordinate;

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
import static org.example.machinery.Direction.EAST;
import static org.example.machinery.Direction.NORTH;
import static org.example.machinery.Direction.NORTH_EAST;
import static org.example.machinery.Direction.NORTH_WEST;
import static org.example.machinery.Direction.SOUTH;
import static org.example.machinery.Direction.SOUTH_EAST;
import static org.example.machinery.Direction.SOUTH_WEST;
import static org.example.machinery.Direction.WEST;
import static org.example.machinery.Shape.EN_PASSANT_GHOST;
import static org.example.machinery.Shape.ROOK;

public record Piece(Team team, Shape shape, boolean hasMoved) {
    private ImmutableSet<Coordinate> getAllPossibleMoves(GameBoard board, Coordinate pointOfOrigin) {
        Function<Direction[], ImmutableSet<Coordinate>> rayCaster = directions -> {
            ImmutableSet.Builder<Coordinate> builder = ImmutableSet.builder();
            Arrays.stream(directions).map(direction -> drawRay(board, pointOfOrigin, direction))
                    .forEach(builder::addAll);
            return builder.build();
        };
        return switch (this.shape) {
            case ROOK -> rayCaster.apply(new Direction[]{NORTH, EAST, SOUTH, WEST});
            case BISHOP -> rayCaster.apply(new Direction[]{NORTH_EAST, NORTH_WEST, SOUTH_WEST, SOUTH_EAST});
            case QUEEN -> rayCaster.apply(new Direction[]{NORTH_EAST, NORTH_WEST, SOUTH_WEST, SOUTH_EAST, NORTH, EAST, SOUTH, WEST});
            case KNIGHT -> ImmutableSet.<Coordinate>builder()
                    .add(pointOfOrigin.add(1, 2))
                    .add(pointOfOrigin.add(-1, 2))
                    .add(pointOfOrigin.add(2, 1))
                    .add(pointOfOrigin.add(-2, 1))
                    .add(pointOfOrigin.add(1, -2))
                    .add(pointOfOrigin.add(-1, -2))
                    .add(pointOfOrigin.add(2, -1))
                    .add(pointOfOrigin.add(-2, -1))
                    .build().stream().filter(c -> board.getItemAtCoordinate(c).isEmpty() ||
                                                  board.getItemAtCoordinate(c).get().team != this.team)
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
                        .filter(c -> board.getItemAtCoordinate(c).isEmpty()
                                     || board.getItemAtCoordinate(c).get().team != this.team)
                        .forEach(builder::add);
                Direction[] order = new Direction[]{EAST, WEST};
                for (Direction direction : order) {
                    ImmutableList<Coordinate> ray = drawRay(board, pointOfOrigin, direction);
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
    
    private ImmutableList<Coordinate> drawRay(GameBoard board, Coordinate startExcl, Direction direction) {
        List<Coordinate> list = new ArrayList<>();
        Piece movingPiece = board.getItemAtCoordinate(startExcl).get();
        for (Coordinate p = direction.shift(startExcl, 1);
             board.getMatrixDimensions().toRegion().contains(p);
             p = direction.shift(p, 1)) {
            Optional<Piece> item = board.getItemAtCoordinate(p);
            if (item.isPresent()) {
                if (item.get().team != movingPiece.team) {
                    list.add(p);
                }
                break;
            }
            list.add(p);
        }
        return ImmutableList.copyOf(list);
    }
    
    public ImmutableSet<Coordinate> getAllValidMoves(GameBoard board, Coordinate pointOfOrigin) {
        return this.getAllPossibleMoves(board, pointOfOrigin).stream()
                .filter(poi -> !board.blindMove(pointOfOrigin, poi).isInCheck(this.team))
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
    
    
}
