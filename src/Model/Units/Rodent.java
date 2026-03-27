package Model.Units;

import Model.Effects.RodentEffect;
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

    /**
     * Вызывается, когда змея съедает грызуна.
     * Наследники могут расширить поведение.
     */
    public void onEaten() {
        _isActive = false;
        getPos().extractUnit();
    }
}
