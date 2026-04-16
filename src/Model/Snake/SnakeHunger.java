package Model.Snake;

public class SnakeHunger {

    private int _life;
    private final int _minLength;
    private int _growthQueue = 0;
    private final int _shrinkInterval;
    private final int _hpLossInterval;
    private final int _hungerDamage;
    private int _ticksSinceShrink = 0;
    private int _ticksSinceHpLoss = 0;

    public SnakeHunger(int minLength, int initialLife, int shrinkInterval, int hpLossInterval, int hungerDamage) {
        _minLength = Math.max(1, minLength);
        _life = Math.max(1, initialLife);
        _shrinkInterval = Math.max(1, shrinkInterval);
        _hpLossInterval = Math.max(1, hpLossInterval);
        _hungerDamage = Math.max(1, hungerDamage);
    }

    public boolean isDead() { return _life <= 0; }
    public void kill() { _life = 0; }

    public void addGrowth() {
        _growthQueue++;
        _ticksSinceShrink = 0;
        _ticksSinceHpLoss = 0;
    }

    public boolean shouldGrow() { return _growthQueue > 0; }
    public void consumeGrowth() { if (_growthQueue > 0) _growthQueue--; }

    public boolean applyHunger(int currentSize) {
        _ticksSinceShrink++;
        _ticksSinceHpLoss++;

        if (currentSize > _minLength) {
            if (_ticksSinceShrink >= _shrinkInterval) {
                _ticksSinceShrink = 0;
                return true;
            }
        } else {
            if (_ticksSinceHpLoss >= _hpLossInterval) {
                _ticksSinceHpLoss = 0;
                _life -= _hungerDamage;
                if (_life <= 0) kill();
            }
        }
        return false;
    }
}