package Model.FactoryRodents;

import Model.GameField.Cell;
import Model.FactoryRodents.RodentFactory;
import Model.Units.Rodent;
import Model.Units.SimpleRodent;

/**
 * Простая реализация RodentFactory, создающая SimpleRodent и помещающая его в клетку.
 * Должна находиться в том же пакете, что и интерфейс RodentFactory (package Model).
 */
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
