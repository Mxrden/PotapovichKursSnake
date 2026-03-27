package Model.Effects;

import Model.Units.Snake;

public abstract class RodentEffect {

    protected final RodentEffectEnum type;

    public RodentEffect(RodentEffectEnum type) {
        this.type = type;
    }

    public RodentEffectEnum getType() {
        return type;
    }

    /**
     * Применяется один раз, когда змея съедает грызуна.
     */
    public final void applyTo(Snake snake) {
        if (!hasEffect()) return;
        applyEffect(snake);
    }

    /**
     * Конкретное действие эффекта.
     */
    protected abstract void applyEffect(Snake snake);

    public boolean hasEffect() {
        return type != RodentEffectEnum.NO_EFFECT;
    }

}
