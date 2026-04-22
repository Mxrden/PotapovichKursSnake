package Model.Units;

import Model.Effects.RodentEffect;
import Model.GameField.Cell;
import Model.Snake.Snake;

/**
 * Базовый класс для грызунов.
 * Реализует шаблонный метод для обработки поедания.
 */
public abstract class Rodent extends Unit {

    protected final RodentEffect _effect;

    protected boolean _isActive;

    public Rodent(RodentEffect effect) {
        this._effect = effect;
        _isActive = true;
    }

    public RodentEffect getEffect() {
        return _effect;
    }

    private void onEaten() {
        _isActive = false;

        Cell cell = getPos();
        if (cell != null && cell.getUnit() == this) {
            cell.extractUnit();
        }

        setPosition(null);
    }

    /**
     * Вызывается когда змея наступает на грызуна.
     * @param snake змея которая наступила
     */
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
