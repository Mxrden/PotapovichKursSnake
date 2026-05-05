package Model.UnitsView;

import Model.Units.Rodent;
import java.awt.*;
import Model.Units.Unit;
import java.awt.*;

public class RodentView extends UnitView {

    @Override
    public void draw(Graphics g, Unit unit) {
        if (!(unit instanceof Rodent)) {
            return;
        }
        g.setColor(Color.RED);
        int size = CELL_SIZE;
        g.fillOval(8, 8, size - 16, size - 16);
    }
}