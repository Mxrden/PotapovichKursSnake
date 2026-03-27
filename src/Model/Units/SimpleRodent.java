package Model.Units;

import Model.Effects.RodentEffect;

public class SimpleRodent extends Rodent{

    public SimpleRodent() {
        super(RodentEffect.NO_EFFECT);
    }

    @Override
    public void onEaten(Snake snake) { }
}
