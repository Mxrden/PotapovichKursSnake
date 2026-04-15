package Model.Units;

import Model.GameField.Cell;
import Model.Snake.Snake;

/**
 * Камень - препятствие для змеи.
 * Реализует принцип открытости/закрытости - новый тип юнита без изменения существующего кода.
 */
public class Stone extends Unit{

    @Override
    public boolean canBelongTo(Cell cell) {
        return cell != null && cell.isEmpty();
    }

    @Override
    public void onSteppedBy(Snake snake) {
        snake.kill();
    }

    @Override
    public UnitType getType() {
        return UnitType.STONE;
    }

    @Override
    public Obstacle getObstacle() {
        return Obstacle.STONE;
    }
}
