package Model.UnitsView;

import Model.Snake.SnakeSegment;
import Model.Units.Unit;
import Model.View.SnakeViewRenderer;

import java.awt.*;

public class SnakeSegmentView extends UnitView {

    private final SnakeViewRenderer renderer = new SnakeViewRenderer();

    @Override
    public void draw(Graphics g, Unit unit) {
        if (!(unit instanceof SnakeSegment segment)) {
            return;
        }
        renderer.drawSnakeSegment(g, segment);
    }
}