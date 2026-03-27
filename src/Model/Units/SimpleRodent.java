package Model.Units;

import Model.Effects.NoEffect;
import Model.GameField.Cell;

public class SimpleRodent extends Rodent {

    public SimpleRodent() {
        super(new NoEffect());
    }

    @Override
    public boolean canBelongTo(Cell cell) {
        return cell.isEmpty();
    }

    // Ќикакой дополнительной логики не нужно
}
