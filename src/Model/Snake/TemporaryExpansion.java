package Model.Snake;

import Model.GameField.Cell;
import Model.GameField.Direction;

public class TemporaryExpansion {
    private final SnakeSegment left;
    private final SnakeSegment right;
    private int lifetime;

    public TemporaryExpansion(Cell headCell, Direction headDir, int lifetimeTicks) {
        System.out.println("=== TemporaryExpansion constructor START ===");
        System.out.println("headCell = " + headCell + " (" + headCell.getRow() + "," + headCell.getCol() + ")");
        System.out.println("headDir = " + headDir);
        System.out.println("lifetimeTicks = " + lifetimeTicks);

        Cell leftCell = getSideCell(headCell, headDir, true);
        Cell rightCell = getSideCell(headCell, headDir, false);
        System.out.println("leftCell = " + leftCell + (leftCell == null ? " (null)" : " (" + leftCell.getRow() + "," + leftCell.getCol() + ")"));
        System.out.println("rightCell = " + rightCell + (rightCell == null ? " (null)" : " (" + rightCell.getRow() + "," + rightCell.getCol() + ")"));

        if (!isValidExpansionCell(leftCell) || !isValidExpansionCell(rightCell)) {
            System.err.println("Cannot place expansion: leftValid=" + isValidExpansionCell(leftCell) + ", rightValid=" + isValidExpansionCell(rightCell));
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

        System.out.println("Expansion placed successfully at left=" + leftCell + ", right=" + rightCell);
        System.out.println("=== TemporaryExpansion constructor END ===");
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