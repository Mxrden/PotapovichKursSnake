package Model.Snake;

import Model.GameField.Cell;
import Model.GameField.Direction;

import java.util.List;

/**
 * Временное утолщение змеи.
 * Оно движется вдоль текущего тела змеи, а не остается на одной клетке поля.
 */
public class TemporaryExpansion {

    private final Snake _snake;
    private final SnakeSegment _left;
    private final SnakeSegment _right;
    private final int _lifetime;
    private int _age = 0;

    public TemporaryExpansion(Snake snake, int lifetimeTicks) {
        if (snake == null) {
            throw new IllegalArgumentException("snake must not be null");
        }
        if (lifetimeTicks <= 0) {
            throw new IllegalArgumentException("lifetimeTicks must be positive");
        }

        _snake = snake;
        _lifetime = lifetimeTicks;
        _left = new SnakeSegment(false, 1.5f, null);
        _right = new SnakeSegment(false, 1.5f, null);

        if (!placeCurrent()) {
            throw new IllegalStateException("Cannot place expansion");
        }
    }

    public boolean tick() {
        if (_age >= _lifetime) {
            dispose();
            return false;
        }

        clearCurrent();
        _age++;
        if (_age >= _lifetime) {
            dispose();
            return false;
        }

        if (!placeCurrent()) {
            dispose();
            return false;
        }

        return true;
    }

    public void dispose() {
        clearCurrent();
    }

    private boolean placeCurrent() {
        SnakeSegment center = getCenterSegment();
        if (center == null) {
            return false;
        }

        Cell centerCell = center.getPos();
        Direction direction = center.getDirection();
        if (centerCell == null || direction == null) {
            return false;
        }

        Cell leftCell = getSideCell(centerCell, direction, true);
        Cell rightCell = getSideCell(centerCell, direction, false);
        if (!isValidExpansionCell(leftCell) || !isValidExpansionCell(rightCell)) {
            return false;
        }

        _left.setDirection(direction);
        _right.setDirection(direction);

        if (!leftCell.putUnit(_left)) {
            return false;
        }
        if (!rightCell.putUnit(_right)) {
            leftCell.extractUnit();
            return false;
        }

        _left.setPosition(leftCell);
        _right.setPosition(rightCell);
        return true;
    }

    private void clearCurrent() {
        if (_left.getPos() != null && _left.getPos().getUnit() == _left) {
            _left.getPos().extractUnit();
        }
        if (_right.getPos() != null && _right.getPos().getUnit() == _right) {
            _right.getPos().extractUnit();
        }
    }

    private SnakeSegment getCenterSegment() {
        List<SnakeSegment> segments = _snake.getSegments();
        if (_age < 0 || _age >= segments.size()) {
            return null;
        }
        return segments.get(_age);
    }

    private Cell getSideCell(Cell center, Direction dir, boolean left) {
        if (dir.equals(Direction.east()) || dir.equals(Direction.west())) {
            return left ? center.getNeighbor(Direction.north()) : center.getNeighbor(Direction.south());
        }
        return left ? center.getNeighbor(Direction.west()) : center.getNeighbor(Direction.east());
    }

    private boolean isValidExpansionCell(Cell cell) {
        return cell != null && cell.isEmpty();
    }
}
