package Model.FactoryRodents;

import Model.GameField.Cell;
import Model.Units.Rodent;
import Model.Units.SimpleRodent;
import Model.Units.WallIgnoreRodent;
import Model.Units.StoneIgnoreRodent;

import java.util.Random;

public class SpecialRodentFactory implements RodentFactory {
    private final Random _random = new Random();

    @Override
    public Rodent createRodent(Cell cell) {
        if (cell == null) return null;

        Rodent rodent;
        int r = _random.nextInt(100);
        if (r < 30) {
            rodent = new SimpleRodent();
        } else if (r < 65) {
            rodent = new WallIgnoreRodent();
        } else {
            rodent = new StoneIgnoreRodent();
        }

        rodent.setPosition(cell);
        if (!cell.putUnit(rodent)) {
            rodent.setPosition(null);
            return null;
        }
        return rodent;
    }
}