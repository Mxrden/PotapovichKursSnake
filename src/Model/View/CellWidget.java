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
 * Определяет способ отрисовки по реальному типу объекта.
 */
public class CellWidget extends JComponent {

    private final Cell cell;
    private static final int SIZE = 32;
    private static final Color BACKGROUND = new Color(30, 36, 50);
    private static final Map<Class<?>, BiConsumer<Graphics, Unit>> RENDERERS = new HashMap<>();
    private static final SnakeViewRenderer SNAKE_RENDERER = new SnakeViewRenderer();

    static {
        RENDERERS.put(Wall.class, (g, unit) -> drawWall(g));
        RENDERERS.put(Stone.class, (g, unit) -> drawStone(g));
        RENDERERS.put(Rodent.class, (g, unit) -> drawRodent(g));
        RENDERERS.put(SnakeSegment.class, (g, unit) -> SNAKE_RENDERER.drawSnakeSegment(g, (SnakeSegment) unit));
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
            BiConsumer<Graphics, Unit> renderer = RENDERERS.get(type);
            if (renderer != null) {
                renderer.accept(g, unit);
                return;
            }
            type = type.getSuperclass();
        }
    }

    private static void drawWall(Graphics g) {
        g.setColor(new Color(180, 100, 60));
        g.fillRect(4, 4, SIZE - 8, SIZE - 8);
        g.setColor(Color.BLACK);
        g.drawRect(4, 4, SIZE - 8, SIZE - 8);
    }

    private static void drawStone(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(6, 6, SIZE - 12, SIZE - 12);
    }

    private static void drawRodent(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(8, 8, SIZE - 16, SIZE - 16);
    }

}
