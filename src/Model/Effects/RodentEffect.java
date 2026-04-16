package Model.Effects;

public abstract class RodentEffect {

    protected final RodentEffectEnum _type;
    private int _duration;

    public RodentEffect(RodentEffectEnum type) {
        _type = type;
        _duration = 1;
    }

    public RodentEffectEnum get_type() {
        return _type;
    }

    public int get_duration() {
        return _duration;
    }

    public void decreaseDuration() {
        if (_duration > 0) {
            _duration--;
        }
    }
}
