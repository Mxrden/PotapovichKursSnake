package Model.Effects;

import Model.Units.Snake;

public class NoEffect extends RodentEffect {

    public NoEffect() {
        super(RodentEffectEnum.NO_EFFECT);
    }

    @Override
    protected void applyEffect(Snake snake) { }
}
