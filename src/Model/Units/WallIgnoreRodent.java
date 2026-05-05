package Model.Units;

import Model.Effects.WallIgnoreEffect;
import Model.GameField.Cell;
import Model.Snake.Snake;

public class WallIgnoreRodent extends Rodent {
    public WallIgnoreRodent() {
        super(new WallIgnoreEffect());
    }

    @Override
    public boolean canBelongTo(Cell cell) {
        return cell != null && cell.isEmpty();
    }

    @Override
    public void onSnakeEntered(Snake snake) {
        snake.addWallIgnoreCharge();
        super.onSnakeEntered(snake);
    }
}