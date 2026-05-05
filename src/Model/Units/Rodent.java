package Model.Units;

import Model.Effects.RodentEffect;
import Model.GameField.Cell;
import Model.Snake.Snake;

public abstract class Rodent extends Unit {

    protected RodentEffect _effect;

    public Rodent(RodentEffect effect) {
        this._effect = effect;
    }

    private void onEaten() {
        Cell cell = getPos();
        if (cell != null && cell.getTopUnit() == this) {
            cell.removeUnit(this);
        }
        setPosition(null);
    }

    @Override
    public void onSteppedBy(Snake snake) {
        this.onEaten();
    }

    @Override
    public boolean canSnakeEnter(Snake snake) {
        return true;
    }

    @Override
    public void onSnakeEntered(Snake snake) {
        onSteppedBy(snake);
    }

    @Override
    public boolean grantsExpansion() {
        return true;
    }
}