package Model.Units;

import Model.Effects.NoEffect;
import Model.GameField.Cell;
import Model.Snake.Snake;

public class SimpleRodent extends Rodent {

    public SimpleRodent() {
        super(new NoEffect());
    }

    @Override
    public boolean canBelongTo(Cell cell) {
        return cell.isEmpty();
    }

    @Override
    public void onSteppedBy(Snake snake) {
        snake.increaseGrowthQueue();
    }

}
