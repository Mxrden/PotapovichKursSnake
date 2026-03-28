package Model.Spawner;

import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.GameField.GameField;
import Model.Labirint.Labirint;
import Model.Units.SimpleRodent;
import Model.Units.Stone;

import java.util.Random;

public class Spawner {

    private final GameField field;
    private final Labirint labirint;
    private final Random rnd = new Random();

    // фиксированный набор направлений (Direction не enum!)
    private static final Direction[] DIRS = {
            Direction.north(),
            Direction.east(),
            Direction.south(),
            Direction.west()
    };

    public Spawner(GameField field, Labirint labirint) {
        this.field = field;
        this.labirint = labirint;
    }

    // -----------------------------
    // Камни
    // -----------------------------
    public void spawnStones(int count) {
        for (int i = 0; i < count; i++) {
            Cell cell = getRandomFreeCell();
            cell.putUnit(new Stone());
        }
    }

    // -----------------------------
    // Грызун
    // -----------------------------
    public SimpleRodent spawnRodent() {
        Cell cell = getRandomFreeCell();
        SimpleRodent rodent = new SimpleRodent();
        cell.putUnit(rodent);
        return rodent;
    }

    // -----------------------------
    // Поиск свободной клетки
    // -----------------------------
    private Cell getRandomFreeCell() {
        while (true) {
            int row = rnd.nextInt(field.getHeight());
            int col = rnd.nextInt(field.getWidth());

            Cell cell = field.getCell(row, col);

            if (isCellFree(cell)) {
                return cell;
            }
        }
    }

    private boolean isCellFree(Cell cell) {

        // 1. Клетка должна быть внутри лабиринта
        if (!labirint.containsCell(cell)) return false;

        // 2. Нельзя ставить на вход/выход
        if (cell == labirint.getEntranceCell()) return false;
        if (cell == labirint.getExitCell()) return false;

        // 3. Клетка должна быть пустой (нет змеи, камня, грызуна)
        if (!cell.isEmpty()) return false;

        // 4. Клетка должна быть доступна (хотя бы одна сторона без стены)
        for (Direction dir : DIRS) {
            if (cell.getWall(dir) == null) {
                return true;
            }
        }

        return false;
    }
}
