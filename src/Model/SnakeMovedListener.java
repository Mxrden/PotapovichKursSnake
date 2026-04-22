package Model;

import Model.GameField.Direction;
import Model.Snake.Snake;

@FunctionalInterface
public interface SnakeMovedListener {
    /**
     * Вызывается после успешного перемещения змеи.
     *
     * @param snake экземпляр змеи
     * @param direction направление последнего хода
     */
    void onSnakeMoved(Snake snake, Direction direction);
}
