package Model.FactoryRodents;

import Model.GameField.Cell;
import Model.Units.Rodent;
import Model.Units.SimpleRodent;

public class DefaultRodentFactory implements RodentFactory {

    @Override
    public Rodent createRodent(Cell cell) {
        if (cell == null) return null;

        SimpleRodent r = new SimpleRodent();

        r.setPosition(cell);

        if (!cell.putUnit(r)) {
            r.setPosition(null);
            return null;
        }

        return r;
    }

}
