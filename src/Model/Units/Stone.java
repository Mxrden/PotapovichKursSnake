package Model.Units;

import Model.GameField.Cell;
import Model.Snake.Snake;

import java.awt.*;

/**
 * Камень - препятствие для змеи.
 * Реализует принцип открытости/закрытости - новый тип юнита без изменения существующего кода.
 */
public class Stone extends Unit{

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
        return snake.tryIgnoreStone();
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
        g.setColor(Color.DARK_GRAY);
        int size = 32;
        g.fillRect(6, 6, size - 12, size - 12);
    }
}
