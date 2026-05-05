package Model.UnitsView;

import Model.Units.Unit;
import java.awt.*;

public abstract class UnitView {

    protected static final int CELL_SIZE = 32;

    public abstract void draw(Graphics g, Unit unit);
}