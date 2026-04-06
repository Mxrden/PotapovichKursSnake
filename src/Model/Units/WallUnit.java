package Model.Units;

import Model.GameField.Cell;
import Model.Snake.Snake;

public class WallUnit extends Unit {
    @Override
    public boolean canBelongTo(Cell cell) {
        return cell.isEmpty();
    }

    @Override
    public void onSteppedBy(Snake snake) {
        snake.kill();
    }
}