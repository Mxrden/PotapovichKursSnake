package Model.Snake;

import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.Units.*;

public class SnakeMovement {

    private Direction direction = Direction.east();

    public void setDirection(Direction dir) {
        if (dir == null) return;
        direction = dir;
    }

    public Direction getDirection() {
        return direction;
    }

    public MoveResult computeMove(Cell headCell, boolean ignoreWall, boolean ignoreStone) {
        Cell target = headCell.getNeighbor(direction);
        if (target == null) {
            return new MoveResult(null, Obstacle.BOUNDARY);
        }

        Unit unit = target.getUnit();

        if (unit instanceof WallUnit) {
            if (ignoreWall) {
                return new MoveResult(target, Obstacle.WALL_IGNORED);
            } else {
                return new MoveResult(null, Obstacle.WALL);
            }
        }

        if (unit instanceof Stone) {
            if (ignoreStone) {
                return new MoveResult(target, Obstacle.STONE_IGNORED);
            } else {
                return new MoveResult(null, Obstacle.STONE);
            }
        }

        return new MoveResult(target, Obstacle.NONE);
    }

    public enum Obstacle {
        NONE, BOUNDARY, WALL, STONE, WALL_IGNORED, STONE_IGNORED
    }

    public static class MoveResult {
        public final Cell target;
        public final Obstacle obstacle;
        public MoveResult(Cell target, Obstacle obstacle) {
            this.target = target;
            this.obstacle = obstacle;
        }
    }
}