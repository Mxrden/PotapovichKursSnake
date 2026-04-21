package Model.Spawner;

import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.GameField.GameField;
import Model.Labirint.Labirint;
import Model.Units.Rodent;
import Model.Units.Stone;
import Model.FactoryRodents.RodentFactory;
import Model.Snake.Snake;
import Model.Snake.SnakeSegment;

import java.util.Random;

public class Spawner {

    private final GameField _field;
    private final Labirint _labirint;
    private final Random _rnd = new Random();
    private final RodentFactory _rodentFactory;
    private Snake _snake;

    public Spawner(GameField field, Labirint labirint, RodentFactory rodentFactory) {
        if (field == null) throw new IllegalArgumentException("field must not be null");
        if (labirint == null) throw new IllegalArgumentException("labirint must not be null");
        if (rodentFactory == null) throw new IllegalArgumentException("rodentFactory must not be null");
        _field = field;
        _labirint = labirint;
        _rodentFactory = rodentFactory;
    }

    public void bindSnake(Snake snake) { _snake = snake; }

    public void placeSnake(Snake snake, int minLength) {
        bindSnake(snake);
        Direction[] dirs = { Direction.north(), Direction.east(), Direction.south(), Direction.west() };

        for (int attempt = 0; attempt < 2000; attempt++) {
            Cell headCell = getRandomFreeCell();
            if (headCell == null) throw new IllegalStateException("No free cell for snake head");

            for (Direction tailDir : dirs) {
                Cell current = headCell;
                boolean ok = true;
                for (int i = 1; i < minLength; i++) {
                    current = current.getNeighbor(tailDir);
                    if (current == null || !isCellFree(current)) {
                        ok = false;
                        break;
                    }
                }
                if (!ok) continue;

                // Проверка клетки перед головой
                Cell forwardCell = headCell.getNeighbor(tailDir.opposite());
                if (forwardCell == null || !forwardCell.isEmpty()) continue;

                // Размещаем голову
                SnakeSegment head = new SnakeSegment(true, 1.0f, null);
                head.set_direction(tailDir.opposite());
                head.setPosition(headCell);
                headCell.putUnit(head);
                snake.getBody().addHead(head);

                // Размещаем тело
                Cell prev = headCell;
                for (int i = 1; i < minLength; i++) {
                    Cell next = prev.getNeighbor(tailDir);
                    SnakeSegment part = new SnakeSegment(false, 1.0f, null);
                    part.setPosition(next);
                    next.putUnit(part);
                    snake.getBody().addTail(part);
                    prev = next;
                }

                snake.setDirectionImmediate(tailDir.opposite());
                return;
            }
        }
        throw new IllegalStateException("Failed to place snake with valid tail direction");
    }

    public void spawnStones(int count) {
        if (count <= 0) return;
        for (int i = 0; i < count; i++) {
            Cell cell = getRandomFreeCell();
            if (cell == null) break;
            cell.putUnit(new Stone());
        }
    }

    public Rodent spawnRodent() {
        Cell cell = getRandomFreeCell();
        if (cell == null) return null;
        return _rodentFactory.createRodent(cell);
    }

    public Cell getRandomFreeCell() {
        int attempts = 0;
        final int maxAttempts = 2000;
        while (attempts < maxAttempts) {
            attempts++;
            int row = _rnd.nextInt(_field.getHeight());
            int col = _rnd.nextInt(_field.getWidth());
            Cell cell = _field.getCell(row, col);
            if (isCellFree(cell)) return cell;
        }
        for (int r = 0; r < _field.getHeight(); r++) {
            for (int c = 0; c < _field.getWidth(); c++) {
                Cell cell = _field.getCell(r, c);
                if (isCellFree(cell)) return cell;
            }
        }
        return null;
    }

    public void respawnRodentAtSamePlace(Rodent oldRodent) {
        if (oldRodent == null) return;
        Cell cell = oldRodent.getPos();
        if (cell == null || !cell.isEmpty()) {
            cell = getRandomFreeCell();
        }
        if (cell != null) {
            _rodentFactory.createRodent(cell);
        }
    }

    private boolean isCellFree(Cell cell) {
        if (cell == null) return false;
        if (cell == _labirint.getEntranceCell()) return false;
        if (cell == _labirint.getExitCell()) return false;
        if (!cell.isEmpty()) return false;
        if (_snake != null) {
            for (SnakeSegment seg : _snake.getSegments()) {
                if (seg.getPos() == cell) return false;
            }
        }
        return true;
    }
}