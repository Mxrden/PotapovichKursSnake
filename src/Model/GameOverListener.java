package Model;

/**
 * Слушатель завершения игры.
 */
@FunctionalInterface
public interface GameOverListener {
    /**
     * Вызывается, когда игра завершена.
     */
    void onGameOver();
}
