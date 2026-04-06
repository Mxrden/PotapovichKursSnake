package Model.Effects;

public abstract class RodentEffect {

    protected final RodentEffectEnum type;
    // 0 или 1 Ч есть эффект или уже израсходован
    private int duration;

    public RodentEffect(RodentEffectEnum type) {
        this.type = type;
        this.duration = 1; // один раз по умолчанию
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
