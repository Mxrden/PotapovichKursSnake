package Model.UnitsView;

import Model.Units.Unit;
import Model.Units.Wall;

import java.awt.*;

/**
 * Класс для отрисовки стен.
 */
public class WallView extends UnitView {

    @Override
    public void draw(Graphics g, Unit unit) {
        if (!(unit instanceof Wall)) {
            return;
        }
        g.setColor(new Color(180, 100, 60));
        int size = CELL_SIZE;
        g.fillRect(4, 4, size - 8, size - 8);
        g.setColor(Color.BLACK);
        g.drawRect(4, 4, size - 8, size - 8);
    }
}