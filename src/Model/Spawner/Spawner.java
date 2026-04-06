package Model.Spawner;

import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.GameField.GameField;
import Model.Labirint.Labirint;
import Model.Units.SimpleRodent;
import Model.Units.Stone;
import Model.Units.WallUnit;
import Model.RodentFactory;
import Model.Snake.Snake;
import Model.Snake.SnakeSegment;

import java.util.Random;

public class Spawner {

    private final GameField field;
    private final Labirint labirint;
    private final Random rnd = new Random();
    private final RodentFactory rodentFactory;

    private Snake snake;

    public Spawner(GameField field, Labirint labirint, RodentFactory rodentFactory) {
        if (field == null) throw new IllegalArgumentException("field must not be null");
        if (labirint == null) throw new IllegalArgumentException("labirint must not be null");
        if (rodentFactory == null) throw new IllegalArgumentException("rodentFactory must not be null");

        this.field = field;
        this.labirint = labirint;
        this.rodentFactory = rodentFactory;
    }

    // ---------------------------------------------------------
    // Привязка змеи
    // ---------------------------------------------------------
    public void bindSnake(Snake snake) {
        this.snake = snake;
    }

    // ---------------------------------------------------------
    // Размещение змеи (голова + хвост)
    // ---------------------------------------------------------
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

                SnakeSegment head = new SnakeSegment(true, 1.0f, null);
                head.setDirection(tailDir.opposite());
                head.setPosition(headCell);
                headCell.putUnit(head);
                snake.getBody().addHead(head);

                Cell prev = headCell;
                for (int i = 1; i < minLength; i++) {
                    Cell next = prev.getNeighbor(tailDir);
                    SnakeSegment part = new SnakeSegment(false, 1.0f, null);
                    part.setPosition(next);
                    next.putUnit(part);
                    snake.getBody().addTail(part);
                    prev = next;
                }

                snake.setDirection(tailDir.opposite());

                return;
            }
        }
        throw new IllegalStateException("Failed to place snake with valid tail direction");
    }

    // ---------------------------------------------------------
    // Камни
    // ---------------------------------------------------------
    public void spawnStones(int count) {
        if (count <= 0) return;

        for (int i = 0; i < count; i++) {
            Cell cell = getRandomFreeCell();
            if (cell == null) break;
            cell.putUnit(new Stone());
        }
    }

    // ---------------------------------------------------------
    // Грызун
    // ---------------------------------------------------------
    public SimpleRodent spawnRodent() {
        Cell cell = getRandomFreeCell();
        if (cell == null) return null;
        return (SimpleRodent) rodentFactory.createRodent(cell);
    }

    // ---------------------------------------------------------
    // Поиск свободной клетки
    // ---------------------------------------------------------
    public Cell getRandomFreeCell() {
        int attempts = 0;
        final int maxAttempts = 2000;

        while (attempts < maxAttempts) {
            attempts++;

            int row = rnd.nextInt(field.getHeight());
            int col = rnd.nextInt(field.getWidth());

            Cell cell = field.getCell(row, col);

            if (isCellFree(cell)) return cell;
        }

        for (int r = 0; r < field.getHeight(); r++) {
            for (int c = 0; c < field.getWidth(); c++) {
                Cell cell = field.getCell(r, c);
                if (isCellFree(cell)) return cell;
            }
        }

        return null;
    }

    // ---------------------------------------------------------
    // Проверка клетки
    // ---------------------------------------------------------
    private boolean isCellFree(Cell cell) {
        if (cell == null) return false;

        if (cell == labirint.getEntranceCell()) return false;
        if (cell == labirint.getExitCell()) return false;

        if (!cell.isEmpty()) return false;

        if (cell.getUnit() instanceof WallUnit) return false;

        if (snake != null) {
            for (SnakeSegment seg : snake.getSegments()) {
                if (seg.getPos() == cell) return false;
            }
        }

        return true;
    }
}