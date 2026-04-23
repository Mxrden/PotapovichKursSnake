package Model.View;

import Model.GameField.Direction;
import Model.Snake.SnakeSegment;

import java.awt.*;

/**
 *    .
 *      .
 */
public class SnakeViewRenderer {

    private static final int CELL_SIZE = 32;

    /**
     *   .
     * @param g
     * @param segment
     */
    public void drawSnakeSegment(Graphics g, SnakeSegment segment) {
        if (segment == null) return;

        if (segment.isHead()) {
            drawSnakeHead(g, segment);
        } else {
            drawSnakeBody(g, segment);
        }
    }

    /**
     *   .
     * @param g
     * @param segment
     */
    private void drawSnakeBody(Graphics g, SnakeSegment segment) {
        if (segment.getThickness() > 1.0f) {
            g.setColor(new Color(255, 140, 0));
        } else {
            g.setColor(Color.YELLOW);
        }
        g.fillRect(4, 4, CELL_SIZE - 8, CELL_SIZE - 8);
    }

    /**
     *     .
     * @param g
     * @param segment
     */
    private void drawSnakeHead(Graphics g, SnakeSegment segment) {
        g.setColor(Color.GREEN);
        g.fillRect(4, 4, CELL_SIZE - 8, CELL_SIZE - 8);

        Direction d = segment.getDirection();
        if (d == null) return;

        g.setColor(Color.WHITE);
        int cx = CELL_SIZE / 2;
        int cy = CELL_SIZE / 2;
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