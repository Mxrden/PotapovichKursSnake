package Model.Units;

import Model.Effects.StoneIgnoreEffect;
import Model.GameField.Cell;
import Model.Snake.Snake;

public class StoneIgnoreRodent extends Rodent {
    public StoneIgnoreRodent() {
        super(new StoneIgnoreEffect());
    }

    @Override
    public boolean canBelongTo(Cell cell) {
        return cell != null && cell.isEmpty();
    }

    @Override
    public void onSnakeEntered(Snake snake) {
        snake.addStoneIgnoreCharge();
        snake.incrementSpecialRodentsEaten();
        super.onSnakeEntered(snake);
    }
}