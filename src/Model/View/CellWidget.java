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

/**
 * Виджет клетки игрового поля.
 * Определяет способ отрисовки по реальному типу объекта.
 */
public class CellWidget extends JComponent {

    private final Cell _cell;
    private static final int SIZE = 32;
    private static final Color BACKGROUND = new Color(30, 36, 50);

    public CellWidget(Cell cell) {
        _cell = cell;
        setPreferredSize(new Dimension(SIZE, SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(BACKGROUND);
        g.fillRect(0, 0, SIZE, SIZE);
        if (!_cell.isEmpty()) {
            drawUnit(g);
        }
    }

    private void drawUnit(Graphics g) {
        Unit unit = _cell.getUnit();
        if (unit == null) return;

        if (unit instanceof SnakeSegment) {
            SnakeSegment segment = (SnakeSegment) unit;
            if (segment.isHead()) {
                drawSnakeHead(g, segment);
            } else {
                drawSnakeBody(g, segment);
            }
        } else if (unit instanceof Wall) {
            drawWall(g);
        } else if (unit instanceof Stone) {
            drawStone(g);
        } else if (unit instanceof Rodent) {
            drawRodent(g);
        }
    }

    private void drawWall(Graphics g) {
        g.setColor(new Color(180, 100, 60));
        g.fillRect(4, 4, SIZE - 8, SIZE - 8);
        g.setColor(Color.BLACK);
        g.drawRect(4, 4, SIZE - 8, SIZE - 8);
    }

    private void drawStone(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(6, 6, SIZE - 12, SIZE - 12);
    }

    private void drawRodent(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(8, 8, SIZE - 16, SIZE - 16);
    }

    private void drawSnakeBody(Graphics g, SnakeSegment seg) {
        if (seg.getThickness() > 1.0f) {
            g.setColor(new Color(255, 140, 0));
        } else {
            g.setColor(Color.YELLOW);
        }
        g.fillRect(4, 4, SIZE - 8, SIZE - 8);
    }

    private void drawSnakeHead(Graphics g, SnakeSegment seg) {
        g.setColor(Color.GREEN);
        g.fillRect(4, 4, SIZE - 8, SIZE - 8);
        Direction d = seg.getDirection();
        if (d == null) return;
        g.setColor(Color.WHITE);
        int cx = SIZE / 2;
        int cy = SIZE / 2;
        int tip = 6;
        Polygon arrow = new Polygon();
        if (d.equals(Direction.north())) {
            arrow.addPoint(cx, cy - 10);
            arrow.addPoint(cx - tip, cy - 2);
            arrow.addPoint(cx + tip, cy - 2);
        } else if (d.equals(Direction.south())) {
            arrow.addPoint(cx, cy + 10);
            arrow.addPoint(cx - tip, cy + 2);
            arrow.addPoint(cx + tip, cy + 2);
        } else if (d.equals(Direction.west())) {
            arrow.addPoint(cx - 10, cy);
            arrow.addPoint(cx - 2, cy - tip);
            arrow.addPoint(cx - 2, cy + tip);
        } else if (d.equals(Direction.east())) {
            arrow.addPoint(cx + 10, cy);
            arrow.addPoint(cx + 2, cy - tip);
            arrow.addPoint(cx + 2, cy + tip);
        }
        g.fillPolygon(arrow);
    }
}
