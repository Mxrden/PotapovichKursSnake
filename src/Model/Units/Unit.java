package Model.Units;

import Model.GameField.Cell;
import Model.Snake.Snake;

import java.awt.*;

public abstract class Unit {
    private Cell _pos;

    public Cell getPos() {
        return _pos;
    }

    public void setPosition(Cell cell) {
        _pos = cell;
    }

    /**
     * Проверяет, может ли юнит быть размещён в указанной клетке.
     * @param cell клетка для проверки
     * @return true если размещение возможно
     */
    public abstract boolean canBelongTo(Cell cell);

    /**
     * Вызывается когда змея наступает на этот юнит.
     * @param snake змея которая наступила
     */
    public abstract void onSteppedBy(Snake snake);

    public boolean canSnakeEnter(Snake snake) {
        return false;
    }

    public void onSnakeEntered(Snake snake) {
    }

    public void onSnakeBlocked(Snake snake) {
        onSteppedBy(snake);
    }

    public boolean grantsExpansion() {
        return false;
    }

}
