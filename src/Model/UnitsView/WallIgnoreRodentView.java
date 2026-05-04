package Model.UnitsView;

import Model.Units.Rodent;
import Model.Units.Unit;

import java.awt.*;

public class WallIgnoreRodentView extends RodentView {
    @Override
    public void draw(Graphics g, Unit unit) {
        if (!(unit instanceof Rodent)) return;
        super.draw(g, unit);
        g.setColor(new Color(139, 69, 19));
        int size = CELL_SIZE;
        int brickW = 8, brickH = 5;
        g.fillRect(size/2 - brickW/2, size/2 - brickH/2, brickW, brickH);
        g.setColor(Color.BLACK);
        g.drawRect(size/2 - brickW/2, size/2 - brickH/2, brickW, brickH);
    }
}