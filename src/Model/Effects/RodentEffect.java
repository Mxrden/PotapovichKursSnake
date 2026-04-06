package Model.Effects;

public abstract class RodentEffect {

    protected final RodentEffectEnum type;
    private int duration;

    public RodentEffect(RodentEffectEnum type) {
        this.type = type;
        this.duration = 1;
    }

    public RodentEffectEnum getType() {
        return type;
    }

    public int getDuration() {
        return duration;
    }

    public void decreaseDuration() {
        if (duration > 0) {
            duration--;
        }
    }
}
