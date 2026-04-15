package Model;

import Model.GameField.Direction;
import Model.Snake.Snake;

public interface SnakeMovedListener {
    /**
     * Вызывается после успешного шага змеи (после обновления позиций).
     * @param snake текущая змея
     * @param direction направление, в котором сделан шаг
     */
    void onSnakeMoved(Snake snake, Direction direction);

    /**
     * Вызывается когда грызун съеден (до респавна).
     * @param snake змея, которая съела грызуна
     */
    void onRodentEaten(Snake snake);

    /**
     * Вызывается когда игра закончена.
     */
    void onGameOver();
}
