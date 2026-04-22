package Model;

import Model.Snake.Snake;

/**
 * Слушатель события поедания грызуна.
 */
@FunctionalInterface
public interface RodentEatenListener {
    /**
     * Вызывается после того, как змея съела грызуна.
     *
     * @param snake экземпляр змеи
     */
    void onRodentEaten(Snake snake);
}
