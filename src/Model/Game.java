package Model;

import Model.FactoryRodents.DefaultRodentFactory;
import Model.FactoryRodents.RodentFactory;
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

    private final GameField _field;
    private final Labirint _labirint;
    private final Snake _snake;
    private final Spawner _spawner;

    private Rodent _rodent;
    private boolean _isOver = false;

    private final List<SnakeMovedListener> _snakeMovedListeners = new ArrayList<>();
    private final List<RodentEatenListener> _rodentEatenListeners = new ArrayList<>();
    private final List<GameOverListener> _gameOverListeners = new ArrayList<>();
    private final Random _rnd = new Random();

    public Game(int width, int height, int snakeMinLength) {
        this(width, height, snakeMinLength, new DefaultRodentFactory());
    }

    public Game(int width, int height, int snakeMinLength, RodentFactory rodentFactory) {
        _field = new GameField(height, width);

        int labW = width / 2;
        int labH = height / 2;

        int labLeft = _rnd.nextInt(width - labW);
        int labTop = _rnd.nextInt(height - labH);

        Cell labStart = _field.getCell(labTop, labLeft);
        GridRegion region = new GridRegion(labStart, labW, labH);

        _labirint = new Labirint(region);
        _labirint.generateSimple();

        _spawner = new Spawner(_field, _labirint, rodentFactory);

        _snake = new Snake(
                snakeMinLength,
                8,
                30,
                4,
                2
        );

        _spawner.placeSnake(_snake, snakeMinLength);

        _rodent = _spawner.spawnRodent();
        _spawner.spawnStones(3);
    }

    public boolean step() {
        if (_isOver) return false;

        boolean moved = _snake.move();

        if (_snake.isDead()) {
            _isOver = true;
            notifyGameOver();
            return false;
        }

        if (moved) {
            notifySnakeMoved();

            if (_snake.wasRodentEaten()) {
                _snake.tryAddExpansion(null);
                _rodent = _spawner.spawnRodent();
                notifyRodentEaten();
            }
        }

        return moved;
    }

    public void addSnakeMovedListener(SnakeMovedListener l) {
        if (l != null && !_snakeMovedListeners.contains(l)) {
            _snakeMovedListeners.add(l);
        }
    }

    public void addRodentEatenListener(RodentEatenListener l) {
        if (l != null && !_rodentEatenListeners.contains(l)) {
            _rodentEatenListeners.add(l);
        }
    }

    public void addGameOverListener(GameOverListener l) {
        if (l != null && !_gameOverListeners.contains(l)) {
            _gameOverListeners.add(l);
        }
    }

    private void notifySnakeMoved() {
        for (SnakeMovedListener l : _snakeMovedListeners) {
            l.onSnakeMoved(_snake, _snake.getDirection());
        }
    }

    private void notifyRodentEaten() {
        for (RodentEatenListener l : _rodentEatenListeners) {
            l.onRodentEaten(_snake);
        }
    }

    private void notifyGameOver() {
        for (GameOverListener l : _gameOverListeners) {
            l.onGameOver();
        }
    }

    public boolean isOver() { return _isOver; }
    public GameField getField() { return _field; }
    public Labirint getLabirint() { return _labirint; }
    public Snake getSnake() { return _snake; }
    public Rodent getRodent() { return _rodent; }
}
