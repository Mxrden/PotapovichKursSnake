package Model;

import Model.FactoryRodents.DefaultRodentFactory;
import Model.GameField.Cell;
import Model.GameField.GameField;
import Model.GameField.GridRegion;
import Model.Labirint.Labirint;
import Model.Snake.Snake;
import Model.Spawner.Spawner;
import Model.Units.Rodent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    private final GameField field;
    private final Labirint labirint;
    private final Snake snake;
    private final Spawner spawner;

    private Rodent rodent;
    private boolean isOver = false;

    private final List<GameListener> listeners = new ArrayList<>();
    private final Random rnd = new Random();

    public Game(int width, int height, int snakeMinLength) {
        this(width, height, snakeMinLength, new DefaultRodentFactory());
    }

    public Game(int width, int height, int snakeMinLength, Model.RodentFactory rodentFactory) {

        field = new GameField(height, width);

        // -----------------------------
        // —À”◊¿…ÕŒ≈ –¿«Ã≈Ÿ≈Õ»≈ À¿¡»–»Õ“¿
        // -----------------------------
        int labW = width / 2;
        int labH = height / 2;

        int labLeft = rnd.nextInt(width - labW);
        int labTop  = rnd.nextInt(height - labH);

        Cell labStart = field.getCell(labTop, labLeft);
        GridRegion region = new GridRegion(labStart, labW, labH);

        labirint = new Labirint(region);
        labirint.generateSimple();

        // -----------------------------
        // —œ¿¬Õ≈– » «Ã≈ﬂ
        // -----------------------------
        spawner = new Spawner(field, labirint, rodentFactory);

        snake = new Snake(
                snakeMinLength,
                8,
                30,
                4,
                2
        );

        spawner.placeSnake(snake, snakeMinLength);

        rodent = spawner.spawnRodent();
        spawner.spawnStones(3);
    }

    // -----------------------------
    // ÿ‡„ Ë„˚
    // -----------------------------
    public boolean step() {
        if (isOver) return false;

        boolean moved = snake.move();

        if (snake.isDead()) {
            isOver = true;
            notifyGameOver();
            return false;
        }

        if (moved) {
            notifySnakeMoved();

            if (snake.wasRodentEaten()) {
                notifyRodentEaten();
                rodent = spawner.spawnRodent();
            }
        }

        return moved;
    }

    // -----------------------------
    // —ÎÛ¯‡ÚÂÎË
    // -----------------------------
    public void addListener(GameListener l) {
        if (l != null && !listeners.contains(l)) listeners.add(l);
    }

    private void notifySnakeMoved() {
        for (GameListener l : listeners) {
            l.onSnakeMoved(snake, snake.getDirection());
        }
    }

    private void notifyRodentEaten() {
        for (GameListener l : listeners) {
            l.onRodentEaten(snake);
        }
    }

    private void notifyGameOver() {
        for (GameListener l : listeners) {
            l.onGameOver();
        }
    }

    // -----------------------------
    // √ÂÚÚÂ˚
    // -----------------------------
    public boolean isOver() { return isOver; }
    public GameField getField() { return field; }
    public Labirint getLabirint() { return labirint; }
    public Snake getSnake() { return snake; }
    public Rodent getRodent() { return rodent; }
}