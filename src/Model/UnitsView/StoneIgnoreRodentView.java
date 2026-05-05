package Model.UnitsView;

import Model.Units.Rodent;
import Model.Units.Unit;
import java.awt.*;

public class StoneIgnoreRodentView extends RodentView {
    @Override
    public void draw(Graphics g, Unit unit) {
        if (!(unit instanceof Rodent)) return;
        super.draw(g, unit);
        g.setColor(Color.DARK_GRAY);
        int size = CELL_SIZE;
        int stoneW = 8, stoneH = 8;
        g.fillOval(size/2 - stoneW/2, size/2 - stoneH/2, stoneW, stoneH);
        g.setColor(Color.BLACK);
        g.drawOval(size/2 - stoneW/2, size/2 - stoneH/2, stoneW, stoneH);
    }
}