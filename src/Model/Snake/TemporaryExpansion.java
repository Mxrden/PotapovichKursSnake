package Model.Snake;

import Model.GameField.Cell;
import Model.GameField.Direction;

public class TemporaryExpansion {
    private final SnakeSegment left;
    private final SnakeSegment right;
    private int lifetime; // количество оставшихся тиков

    public TemporaryExpansion(Snake snake, Cell headCell, Direction headDir, int lifetimeTicks) {
        Cell leftCell = getSideCell(headCell, headDir, true);
        Cell rightCell = getSideCell(headCell, headDir, false);
        if (!isValidExpansionCell(leftCell) || !isValidExpansionCell(rightCell)) {
            throw new IllegalStateException("Cannot place expansion");
        }
        this.lifetime = lifetimeTicks;
        left = new SnakeSegment(false, 1.5f, null);
        right = new SnakeSegment(false, 1.5f, null);
        left.setDirection(headDir);
        right.setDirection(headDir);
        leftCell.putUnit(left);
        rightCell.putUnit(right);
        left.setPosition(leftCell);
        right.setPosition(rightCell);
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

    /** Возвращает true, если расширение ещё живо, иначе false (удалено) */
    public boolean tick() {
        if (lifetime <= 0) return false;
        lifetime--;
        if (lifetime <= 0) {
            dispose();
            return false;
        }
        return true;
    }

    public void dispose() {
        if (left != null && left.getPos() != null) left.getPos().extractUnit();
        if (right != null && right.getPos() != null) right.getPos().extractUnit();
    }
}