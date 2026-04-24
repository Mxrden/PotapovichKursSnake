package Model.UnitsView;

import Model.GameField.Cell;
import Model.Units.Unit;

import java.awt.*;

/**
 * Базовый класс для отрисовки юнитов.
 * Отвечает за визуальное представление игровых объектов.
 */
public abstract class UnitView {

    protected static final int CELL_SIZE = 32;

    /**
     * Отрисовывает юнит в указанном контексте Graphics.
     * @param g объект Graphics для отрисовки
     * @param unit юнит для отрисовки
     */
    public abstract void draw(Graphics g, Unit unit);
}