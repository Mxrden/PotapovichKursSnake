package Model.Units;

import Model.GameField.Cell;

public class SimpleRodent extends Rodent {

    public SimpleRodent() {
        super(null);
    }

    @Override
    public boolean canBelongTo(Cell cell) {
        return cell != null && cell.isEmpty();
    }

}