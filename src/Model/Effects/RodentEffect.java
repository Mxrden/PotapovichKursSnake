package Model.Effects;

public abstract class RodentEffect {

    protected final RodentEffectEnum type;

    public RodentEffect(RodentEffectEnum type) {
        this.type = type;
    }

    public RodentEffectEnum getType() {
        return type;
    }

}
