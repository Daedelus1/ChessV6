package org.example.machinery;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.example.ConsoleColors;
import org.example.data_structures.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.example.ConsoleColors.ColorType.BOLD;
import static org.example.ConsoleColors.ColorType.BOLD_BRIGHT;
import static org.example.ConsoleColors.ColorType.BRIGHT;
import static org.example.ConsoleColors.ColorValue.BLACK;
import static org.example.machinery.Shape.EN_PASSANT_GHOST;
import static org.example.machinery.Shape.ROOK;

public record Piece(Team team, Shape shape, boolean hasMoved) {
    private ImmutableSet<Coordinate> getAllPossibleMoves(GameBoard board, Coordinate pointOfOrigin) {
        return switch (this.shape) {
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
                Direction[] order = new Direction[]{Direction.EAST, Direction.WEST};
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
            case ROOK -> ImmutableSet.<Coordinate>builder()
                    .addAll(drawRay(board, pointOfOrigin, Direction.NORTH))
                    .addAll(drawRay(board, pointOfOrigin, Direction.SOUTH))
                    .addAll(drawRay(board, pointOfOrigin, Direction.EAST))
                    .addAll(drawRay(board, pointOfOrigin, Direction.WEST))
                    .build();
            case BISHOP -> ImmutableSet.<Coordinate>builder()
                    .addAll(drawRay(board, pointOfOrigin, Direction.NORTH_EAST))
                    .addAll(drawRay(board, pointOfOrigin, Direction.SOUTH_EAST))
                    .addAll(drawRay(board, pointOfOrigin, Direction.NORTH_WEST))
                    .addAll(drawRay(board, pointOfOrigin, Direction.SOUTH_WEST))
                    .build();
            case QUEEN -> ImmutableSet.<Coordinate>builder()
                    .addAll(drawRay(board, pointOfOrigin, Direction.NORTH_EAST))
                    .addAll(drawRay(board, pointOfOrigin, Direction.SOUTH_EAST))
                    .addAll(drawRay(board, pointOfOrigin, Direction.NORTH_WEST))
                    .addAll(drawRay(board, pointOfOrigin, Direction.SOUTH_WEST))
                    .addAll(drawRay(board, pointOfOrigin, Direction.NORTH))
                    .addAll(drawRay(board, pointOfOrigin, Direction.SOUTH))
                    .addAll(drawRay(board, pointOfOrigin, Direction.EAST))
                    .addAll(drawRay(board, pointOfOrigin, Direction.WEST))
                    .build();
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
    
    private enum Direction {
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
}
