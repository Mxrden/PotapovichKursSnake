package Model.UnitsView;

import Model.Units.Stone;
import Model.Units.Unit;

import java.awt.*;

public class StoneView extends UnitView {

    @Override
    public void draw(Graphics g, Unit unit) {
        if (!(unit instanceof Stone)) {
            return;
        }
        g.setColor(Color.DARK_GRAY);
        int size = CELL_SIZE;
        g.fillRect(6, 6, size - 12, size - 12);
    }
}