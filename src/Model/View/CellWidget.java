package Model.View;

import Model.GameField.Cell;
import Model.Snake.SnakeSegment;
import Model.Units.Rodent;
import Model.Units.Stone;
import Model.Units.Unit;
import Model.Units.Wall;
import Model.UnitsView.RodentView;
import Model.UnitsView.SnakeSegmentView;
import Model.UnitsView.StoneView;
import Model.UnitsView.UnitView;
import Model.UnitsView.WallView;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Виджет клетки игрового поля.
 * Использует UnitView классы для отрисовки юнитов.
 */
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
        if (!cell.isEmpty()) {
            drawUnit(g);
        }
    }

    private void drawUnit(Graphics g) {
        Unit unit = cell.getUnit();
        if (unit == null) return;

        renderByType(g, unit);
    }

    private void renderByType(Graphics g, Unit unit) {
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
