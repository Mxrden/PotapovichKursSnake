package Model.Snake;

import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.Units.Unit;

/**
 * Сегмент тела змеи.
 * Реализует принцип единственной ответственности - хранение состояния сегмента.
 */
public class SnakeSegment extends Unit {
    private boolean isHead;
    private float thickness;
    private Direction direction;

    public SnakeSegment(boolean isHead, float thickness, Cell position) {
        this.isHead = isHead;
        this.thickness = thickness;
        if (position != null) setPosition(position);
    }

    public boolean isHead() { return isHead; }
    public void setHead(boolean head) { isHead = head; }
    public float getThickness() { return thickness; }
    public void setThickness(float thickness) { this.thickness = thickness; }
    public Direction getDirection() { return direction; }
    public void setDirection(Direction dir) { direction = dir; }

    @Override
    public boolean canBelongTo(Cell cell) { return cell.isEmpty() && cell != null; }

    @Override
    public void onSteppedBy(Snake snake) { snake.kill(); }

    @Override
    public UnitType getType() {
        if (isHead()) return UnitType.SNAKE_HEAD;
        else return UnitType.SNAKE_BODY;
    }
}