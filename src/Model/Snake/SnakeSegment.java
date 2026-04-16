package Model.Snake;

import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.Units.Unit;

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

    public boolean is_isHead() { return _isHead; }
    public void set_isHead(boolean isHead) {_isHead = isHead; }
    public float get_thickness() { return _thickness; }
    public void set_thickness(float thickness) {_thickness = thickness; }
    public Direction get_direction() { return _direction; }
    public void set_direction(Direction dir) { _direction = dir; }

    @Override
    public boolean canBelongTo(Cell cell) { return cell.isEmpty() && cell != null; }

    @Override
    public void onSteppedBy(Snake snake) { snake.kill(); }

    @Override
    public UnitType getType() {
        return _isHead ? UnitType.SNAKE_HEAD : UnitType.SNAKE_BODY;
    }

    @Override
    public Obstacle getObstacle() {
        return Obstacle.STONE;
    }
}