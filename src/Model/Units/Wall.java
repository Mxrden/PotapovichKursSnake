package Model.Units;

import Model.GameField.Cell;
import Model.Snake.Snake;

public class Wall extends Unit {
    @Override
    public boolean canBelongTo(Cell cell) {
        return cell != null && cell.isEmpty();
    }

    @Override
    public void onSteppedBy(Snake snake) {
        snake.kill();
    }

    @Override
    public boolean canSnakeEnter(Snake snake) {
        return snake.tryIgnoreWall();
    }

    @Override
    public void onSnakeEntered(Snake snake) {
        Cell cell = getPos();
        if (cell != null && cell.getUnit() == this) {
            cell.extractUnit();
        }
    }
}
