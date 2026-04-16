package Model.Snake;

import Model.GameField.Cell;
import Model.GameField.Direction;

public class TemporaryExpansion {
    private final SnakeSegment _left;
    private final SnakeSegment _right;
    private int _lifetime;

    public TemporaryExpansion(Cell headCell, Direction headDir, int lifetimeTicks) {
        Cell leftCell = getSideCell(headCell, headDir, true);
        Cell rightCell = getSideCell(headCell, headDir, false);

        if (!isValidExpansionCell(leftCell) || !isValidExpansionCell(rightCell)) {
            throw new IllegalStateException("Cannot place expansion");
        }

        _lifetime = lifetimeTicks;
        _left = new SnakeSegment(false, 1.5f, null);
        _right = new SnakeSegment(false, 1.5f, null);
        _left.set_direction(headDir);
        _right.set_direction(headDir);

        leftCell.putUnit(_left);
        rightCell.putUnit(_right);
        _left.setPosition(leftCell);
        _right.setPosition(rightCell);
    }

    private Cell getSideCell(Cell center, Direction dir, boolean left) {
        if (dir.equals(Direction.east()) || dir.equals(Direction.west())) {
            return left ? center.getNeighbor(Direction.north()) : center.getNeighbor(Direction.south());
        } else {
            return left ? center.getNeighbor(Direction.west()) : center.getNeighbor(Direction.east());
        }
    }

    private boolean isValidExpansionCell(Cell cell) {
        return cell != null && cell.isEmpty();
    }

    public boolean tick() {
        if (_lifetime <= 0) return false;
        _lifetime--;
        if (_lifetime <= 0) {
            dispose();
            return false;
        }
        return true;
    }

    public void dispose() {
        if (_left != null && _left.getPos() != null) _left.getPos().extractUnit();
        if (_right != null && _right.getPos() != null) _right.getPos().extractUnit();
    }
}