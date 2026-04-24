package Model.Units;

import Model.GameField.Cell;
import Model.Snake.Snake;

import java.awt.*;

public class Wall extends Unit {
    @Override
    public boolean canBelongTo(Cell cell) {
        return cell != null && cell.isEmpty();
    }

    @Override
    public void onSteppedBy(Snake snake) {
        snake.kill();
    }

    @Override
    public boolean canSnakeEnter(Snake snake) {
        return snake.tryIgnoreWall();
    }

    @Override
    public void onSnakeEntered(Snake snake) {
        Cell cell = getPos();
        if (cell != null && cell.getUnit() == this) {
            cell.extractUnit();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(180, 100, 60));
        int size = 32;
        g.fillRect(4, 4, size - 8, size - 8);
        g.setColor(Color.BLACK);
        g.drawRect(4, 4, size - 8, size - 8);
    }
}
