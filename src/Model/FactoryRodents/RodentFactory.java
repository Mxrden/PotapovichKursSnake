package Model.FactoryRodents;

import Model.GameField.Cell;
import Model.Units.Rodent;

/**
 * Фабрика для создания грызунов. Позволяет подменять тип создаваемого Rodent
 * без изменения Spawner или Game.
 */
public interface RodentFactory {
    /**
     * Создать грызуна и поместить его в указанную клетку.
     * Реализация должна положить созданный объект в cell (cell.putUnit(...)).
     *
     * @param cell клетка, в которую нужно поместить грызуна
     * @return созданный Rodent (или null, если не удалось)
     */
    Rodent createRodent(Cell cell);
}
