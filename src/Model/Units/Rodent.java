package Model.Units;

import Model.Effects.RodentEffect;
import Model.GameField.Cell;
import Model.Snake.Snake;

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

    public void onEaten() {
        _isActive = false;

        Cell cell = getPos();
        if (cell != null && cell.getUnit() == this) {
            cell.extractUnit();
        }

        setPosition(null);
    }

}
