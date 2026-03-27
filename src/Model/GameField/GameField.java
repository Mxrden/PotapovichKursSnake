package Model.GameField;

import java.util.Iterator;

public class GameField implements Iterable<Cell> {

    private final int _height;
    private final int _width;

    public int getHeight() {
        return _height;
    }

    public int getWidth() {
        return _width;
    }

    public Cell getCell(int row, int col) {
        return _cells[row][col];
    }

    private final Cell[][] _cells;

    public GameField(int height, int width) {
        _height = height;
        _width = width;

        _cells = new Cell[height][width];

        // создание клеток
        for (int row = 0; row < getHeight(); row++) {
            for (int col = 0; col < getWidth(); col++) {
                _cells[row][col] = new Cell(row, col);
            }
        }

        // установка соседей
        for (int row = 0; row < getHeight(); row++) {
            for (int col = 0; col < getWidth(); col++) {
                Cell cell = _cells[row][col];

                if (row > 0)
                    cell.setNeighbor(Direction.north(), _cells[row - 1][col]);

                if (row < getHeight() - 1)
                    cell.setNeighbor(Direction.south(), _cells[row + 1][col]);

                if (col > 0)
                    cell.setNeighbor(Direction.west(), _cells[row][col - 1]);

                if (col < getWidth() - 1)
                    cell.setNeighbor(Direction.east(), _cells[row][col + 1]);
            }
        }
    }

    @Override
    public Iterator<Cell> iterator() {
        return new GameFieldIterator(this);
    }

    private static class GameFieldIterator implements Iterator<Cell> {
        private final GameField _field;
        private int row = 0;
        private int col = 0;

        public GameFieldIterator(GameField field) {
            _field = field;
        }

        @Override
        public boolean hasNext() {
            return row < _field.getHeight();
        }

        @Override
        public Cell next() {
            Cell cell = _field.getCell(row, col);

            col++;
            if (col >= _field.getWidth()) {
                col = 0;
                row++;
            }

            return cell;
        }
    }
}
