package Model.View;

import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.Snake.SnakeSegment;
import Model.Units.Rodent;
import Model.Units.Stone;
import Model.Units.Unit;
import Model.Units.Wall;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Виджет клетки игрового поля.
 * Использует метод draw() юнита для отрисовки.
 */
public class CellWidget extends JComponent {

    private final Cell cell;
    private static final int SIZE = 32;
    private static final Color BACKGROUND = new Color(30, 36, 50);

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
        unit.draw(g);
    }
}
