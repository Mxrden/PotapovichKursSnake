package Model.Units;

import Model.Effects.RodentEffect;
import Model.Units.Snake;

public abstract class Rodent {

    protected final RodentEffect _effect;

    public Rodent(RodentEffect effect) {
        this._effect = effect;
    }

    public RodentEffect getEffect() {
        return _effect;
    }

    /**
     * Вызывается, когда змея съедает грызуна.
     * Базовая реализация применяет эффект.
     * Наследники могут расширить поведение.
     */
    public void onEaten(Snake snake) {
        _effect.applyTo(snake);
    }
}
