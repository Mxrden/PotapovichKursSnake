package Model.Units;

import Model.GameField.Cell;
import Model.Snake.Snake;

public abstract class Unit {
    private Cell _pos;

    public Cell getPos() {
        return _pos;
    }

    protected void setPosition(Cell cell) {
        _pos = cell;
    }

    public abstract boolean canBelongTo(Cell cell);

    public abstract void onSteppedBy(Snake snake);
}
