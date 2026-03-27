package Model.Units;

import Model.GameField.Cell;

public class Stone extends Unit{

    @Override
    public boolean canBelongTo(Cell cell) {
        return cell.isEmpty();
    }
}
