package Model.Snake;

import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.Units.Unit;

import java.awt.*;

/**
 * Сегмент тела змеи.
 * Реализует принцип единственной ответственности - хранение состояния сегмента.
 */
public class SnakeSegment extends Unit {
    private boolean _isHead;
    private float _thickness;
    private Direction _direction;

    public SnakeSegment(boolean isHead, float thickness, Cell position) {
        _isHead = isHead;
        _thickness = thickness;
        if (position != null) setPosition(position);
    }

    public boolean isHead() { return _isHead; }
    public void setHead(boolean isHead) {_isHead = isHead; }
    public float getThickness() { return _thickness; }
    public Direction getDirection() { return _direction; }
    public void setDirection(Direction dir) { _direction = dir; }

    @Override
    public void onSteppedBy(Snake snake) { snake.kill(); }

    @Override
    public boolean canBelongTo(Cell cell) {
        if (cell == null) return false;
        for (Unit u : cell.getUnits()) {
            if (u instanceof SnakeSegment) return false;
        }
        return true;
    }
}
