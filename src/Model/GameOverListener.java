package Model;

/**
 * Интерфейс для уведомлений об окончании игры.
 */
public interface GameOverListener {
    /**
     * Вызывается когда игра закончена.
     */
    void onGameOver();
}