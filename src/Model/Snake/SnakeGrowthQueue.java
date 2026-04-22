package Model.Snake;

/**
 * Очередь роста змеи.
 * Отдельно хранит только факт накопленного роста, не смешивая это с голодом.
 */
public class SnakeGrowthQueue {

    private int _growthQueue = 0;

    public void addGrowth() {
        _growthQueue++;
    }

    public boolean shouldGrow() {
        return _growthQueue > 0;
    }

    public void consumeGrowth() {
        if (_growthQueue > 0) {
            _growthQueue--;
        }
    }

    public boolean isEmpty() {
        return _growthQueue == 0;
    }
}
