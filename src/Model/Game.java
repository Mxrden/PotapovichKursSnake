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

    private final int _width;
    private final int _height;
    private final int _snakeMinLength;
    private final RodentFactory _rodentFactory;

    private GameField _field;
    private Labirint _labirint;
    private Snake _snake;
    private Spawner _spawner;

    private Rodent _rodent;
    private boolean _isOver = false;
    private int _score = 0;

    private final List<SnakeMovedListener> _snakeMovedListeners = new ArrayList<>();
    private final List<RodentEatenListener> _rodentEatenListeners = new ArrayList<>();
    private final List<GameOverListener> _gameOverListeners = new ArrayList<>();
    private final Random _rnd = new Random();

    public Game(int width, int height, int snakeMinLength) {
        this(width, height, snakeMinLength, new DefaultRodentFactory());
    }

    public Game(int width, int height, int snakeMinLength, RodentFactory rodentFactory) {
        _width = width;
        _height = height;
        _snakeMinLength = snakeMinLength;
        _rodentFactory = rodentFactory;
        initializeGameState();
    }

    private void initializeGameState() {
        createField();
        createLabirint();
        createSpawner();
        createSnake();
        placeInitialEntities();
        resetRuntimeState();
    }

    private void createField() {
        _field = new GameField(_height, _width);
    }

    private void createLabirint() {
        int labW = _width / 2;
        int labH = _height / 2;
        int labLeft = _rnd.nextInt(_width - labW);
        int labTop = _rnd.nextInt(_height - labH);
        Cell labStart = _field.getCell(labTop, labLeft);
        GridRegion region = new GridRegion(labStart, labW, labH);
        _labirint = new Labirint(region);
        _labirint.generateSimple();
    }

    private void createSpawner() {
        _spawner = new Spawner(_field, _labirint, _rodentFactory);
    }

    private void createSnake() {
        _snake = new Snake(
                _snakeMinLength,
                generateInitialSnakeLife(_width, _height),
                30,
                8,
                1
        );
    }

    private void placeInitialEntities() {
        _spawner.placeSnake(_snake, _snakeMinLength);
        _rodent = _spawner.spawnRodent();
        _spawner.spawnStones(3);
    }

    private void resetRuntimeState() {
        _isOver = false;
        _score = 0;
    }

    private int generateInitialSnakeLife(int width, int height) {
        double minLife = width + (height / 2.0);
        double maxLife = width + (height / 1.5);
        int lowerBound = (int) Math.ceil(minLife);
        int upperBound = (int) Math.floor(maxLife);
        if (upperBound < lowerBound) upperBound = lowerBound;
        return lowerBound + _rnd.nextInt(upperBound - lowerBound + 1);
    }

    public void restart() {
        initializeGameState();
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
                _snake.tryAddExpansion(null);   // параметр остался
                _rodent = _spawner.spawnRodent();
                _score++;
                notifyRodentEaten();
            }
        }
        return moved;
    }

    // ... остальные методы (листенеры, геттеры) без изменений ...
    public void addSnakeMovedListener(SnakeMovedListener l) {
        if (l != null && !_snakeMovedListeners.contains(l)) _snakeMovedListeners.add(l);
    }
    public void removeSnakeMovedListener(SnakeMovedListener l) { _snakeMovedListeners.remove(l); }
    public void addRodentEatenListener(RodentEatenListener l) {
        if (l != null && !_rodentEatenListeners.contains(l)) _rodentEatenListeners.add(l);
    }
    public void removeRodentEatenListener(RodentEatenListener l) { _rodentEatenListeners.remove(l); }
    public void addGameOverListener(GameOverListener l) {
        if (l != null && !_gameOverListeners.contains(l)) _gameOverListeners.add(l);
    }
    public void removeGameOverListener(GameOverListener l) { _gameOverListeners.remove(l); }
    private void notifySnakeMoved() {
        for (SnakeMovedListener l : _snakeMovedListeners) l.onSnakeMoved(_snake, _snake.getDirection());
    }
    private void notifyRodentEaten() {
        for (RodentEatenListener l : _rodentEatenListeners) l.onRodentEaten(_snake);
    }
    private void notifyGameOver() {
        for (GameOverListener l : _gameOverListeners) l.onGameOver();
    }
    public boolean isOver() { return _isOver; }
    public int getScore() { return _score; }
    public GameField getField() { return _field; }
    public Labirint getLabirint() { return _labirint; }
    public Snake getSnake() { return _snake; }
    public Rodent getRodent() { return _rodent; }
}