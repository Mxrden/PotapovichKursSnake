package Model.Units;

import Model.GameField.Cell;
import Model.Snake.Snake;

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

    /**
     * Возвращает тип юнита для отрисовки.
     * @return enum типа юнита
     */
    public abstract UnitType getType();

    public Obstacle getObstacle() {
        return null;
    }

    /**
     * Типы юнитов для отрисовки без использования instanceof.
     */
    public enum UnitType {
        SNAKE_HEAD,
        SNAKE_BODY,
        WALL,
        STONE,
        RODENT
    }

    public enum Obstacle {
        NONE,
        BOUNDARY,
        WALL,
        STONE,
        WALL_IGNORED,
        STONE_IGNORED
    }
}
