package Model;

import Model.Snake.Snake;

/**
 * Интерфейс для уведомлений о поедании грызуна.
 */
public interface RodentEatenListener {
    /**
     * Вызывается когда грызун съеден (до респавна).
     * @param snake змея, которая съела грызуна
     */
    void onRodentEaten(Snake snake);
}