package Model;

import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.GameField.GameField;
import Model.Labirint.Labirint;
import Model.Snake.Snake;
import Model.Snake.SnakePart;
import Model.Spawner.Spawner;
import Model.Units.Rodent;
import Model.Units.SimpleRodent;

public class Game {

    private final GameField _field;
    private final Labirint _labirint;
    private final Snake _snake;
    private final Spawner _spawner;

    private Rodent _rodent;
    private boolean _isOver = false;

    // -----------------------------
    // Конструктор
    // -----------------------------
    public Game(int width, int height, int snakeMinLength) {

        // 1. Поле
        _field = new GameField(width, height);

        // 2. Лабиринт
        Cell leftTop = _field.getCell(0, 0);
        _labirint = new Labirint(leftTop, width, height);
        _labirint.generateThreeColumnsWithGaps();

        // 3. Змея
        _snake = new Snake(snakeMinLength);

        Cell start = _field.getCell(height / 2, width / 2);
        SnakePart head = new SnakePart(true, 1.0f, start);
        head.setDirection(Direction.north());
        start.putUnit(head);
        _snake.joinNewPart(head);

        // 4. Спавнер
        _spawner = new Spawner(_field, _labirint);

        // 5. Первый грызун
        _rodent = _spawner.spawnRodent();

        // 6. Камни
        _spawner.spawnStones(3);
    }

    // -----------------------------
    // Один шаг игры
    // -----------------------------
    public boolean step(Direction dir) {

        if (_isOver) return false;

        boolean moved = _snake.move(dir);

        if (!_snake.isDead() && moved) {

            // Проверяем: голова в клетке грызуна?
            if (_snake.getHead().getPos() == _rodent.getPos()) {
                _rodent.onEaten();
                _rodent = _spawner.spawnRodent();
            }

            return true;
        }

        _isOver = true;
        return false;
    }

    // -----------------------------
    // Доступ к состоянию
    // -----------------------------
    public boolean isOver() {
        return _isOver;
    }

    public GameField getField() {
        return _field;
    }

    public Labirint getLabirint() {
        return _labirint;
    }

    public Snake getSnake() {
        return _snake;
    }

    public Rodent getRodent() {
        return _rodent;
    }
}
