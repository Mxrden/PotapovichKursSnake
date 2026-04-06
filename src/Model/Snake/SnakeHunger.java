package Model.Snake;

public class SnakeHunger {

    private int life;
    private final int minLength;
    private int growthQueue = 0;
    private final int shrinkInterval;
    private final int hpLossInterval;
    private final int hungerDamage;
    private int ticksSinceShrink = 0;
    private int ticksSinceHpLoss = 0;

    public SnakeHunger(int minLength, int initialLife,
                       int shrinkInterval, int hpLossInterval, int hungerDamage) {
        this.minLength = Math.max(1, minLength);
        this.life = Math.max(1, initialLife);
        this.shrinkInterval = Math.max(1, shrinkInterval);
        this.hpLossInterval = Math.max(1, hpLossInterval);
        this.hungerDamage = Math.max(1, hungerDamage);
    }

    public boolean isDead() { return life <= 0; }
    public void kill() { life = 0; }

    public void addGrowth() {
        growthQueue++;
        ticksSinceShrink = 0;
        ticksSinceHpLoss = 0;
    }

    public boolean shouldGrow() { return growthQueue > 0; }
    public void consumeGrowth() { if (growthQueue > 0) growthQueue--; }

    /**
     * Применяет эффект голода.
     * @param currentSize текущая длина змеи
     * @return true, если нужно укоротить змею (удалить хвост), false иначе
     */
    public boolean applyHunger(int currentSize) {
        ticksSinceShrink++;
        ticksSinceHpLoss++;

        if (currentSize > minLength) {
            if (ticksSinceShrink >= shrinkInterval) {
                ticksSinceShrink = 0;
                return true;
            }
        } else {
            if (ticksSinceHpLoss >= hpLossInterval) {
                ticksSinceHpLoss = 0;
                life -= hungerDamage;
                if (life <= 0) kill();
            }
        }
        return false;
    }
}