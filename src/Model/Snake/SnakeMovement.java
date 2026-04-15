package Model.Snake;

import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.Units.Unit;

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
            return new MoveResult(null, Unit.Obstacle.BOUNDARY);
        }

        Unit unit = target.getUnit();
        if (unit == null) {
            return new MoveResult(target, Unit.Obstacle.NONE);
        }

        Unit.Obstacle obstacle = unit.getObstacle();
        if (obstacle == null) {
            return new MoveResult(target, Unit.Obstacle.NONE);
        }

        //
        if (ignoreWall && obstacle == Unit.Obstacle.WALL) {
            return new MoveResult(target, Unit.Obstacle.WALL_IGNORED);
        }
        if (ignoreStone && obstacle == Unit.Obstacle.STONE) {
            return new MoveResult(target, Unit.Obstacle.STONE_IGNORED);
        }

        return new MoveResult(target, obstacle);
    }

    public static class MoveResult {
        public final Cell target;
        public final Unit.Obstacle obstacle;
        public MoveResult(Cell target, Unit.Obstacle obstacle) {
            this.target = target;
            this.obstacle = obstacle;
        }
    }
}