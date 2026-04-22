package Model.Snake;

import Model.GameField.Cell;
import Model.GameField.Direction;
public class SnakeMovement {

    private Direction _direction = Direction.east();

    public void setDirection(Direction dir) {
        if (dir == null) return;
        _direction = dir;
    }

    public Direction getDirection() {
        return _direction;
    }

    public Cell computeTarget(Cell headCell) {
        if (headCell == null) {
            return null;
        }
        return headCell.getNeighbor(_direction);
    }
}
