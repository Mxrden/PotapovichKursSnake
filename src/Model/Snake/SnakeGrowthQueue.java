package Model.Snake;

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
