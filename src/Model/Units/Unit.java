package Model.Units;

import Model.GameField.Cell;

public abstract class Unit {
    private Cell _pos;

    public Cell getPos() {
        return _pos;
    }

    public abstract boolean canBelongTo(Cell cell);

}
