package Model.View;

import Model.GameField.Cell;
import Model.Snake.SnakeSegment;
import Model.Units.Rodent;
import Model.Units.Stone;
import Model.Units.Unit;
import Model.Units.Wall;
import Model.Units.WallIgnoreRodent;
import Model.Units.StoneIgnoreRodent;
import Model.UnitsView.RodentView;
import Model.UnitsView.SnakeSegmentView;
import Model.UnitsView.StoneView;
import Model.UnitsView.UnitView;
import Model.UnitsView.WallView;
import Model.UnitsView.WallIgnoreRodentView;
import Model.UnitsView.StoneIgnoreRodentView;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CellWidget extends JComponent {

    private final Cell cell;
    private static final int SIZE = 32;
    private static final Color BACKGROUND = new Color(30, 36, 50);
    private static final Map<Class<?>, UnitView> UNIT_VIEWS = new HashMap<>();

    static {
        UNIT_VIEWS.put(Wall.class, new WallView());
        UNIT_VIEWS.put(Stone.class, new StoneView());
        UNIT_VIEWS.put(Rodent.class, new RodentView());
        UNIT_VIEWS.put(SnakeSegment.class, new SnakeSegmentView());
        UNIT_VIEWS.put(WallIgnoreRodent.class, new WallIgnoreRodentView());
        UNIT_VIEWS.put(StoneIgnoreRodent.class, new StoneIgnoreRodentView());
    }

    public CellWidget(Cell cell) {
        this.cell = cell;
        setPreferredSize(new Dimension(SIZE, SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(BACKGROUND);
        g.fillRect(0, 0, SIZE, SIZE);
        for (Unit unit : cell.getUnits()) {
            drawUnit(g, unit);
        }
    }

    private void drawUnit(Graphics g, Unit unit) {
        if (unit == null) return;
        Class<?> type = unit.getClass();
        while (type != null) {
            UnitView view = UNIT_VIEWS.get(type);
            if (view != null) {
                view.draw(g, unit);
                return;
            }
            type = type.getSuperclass();
        }
    }
}