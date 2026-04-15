package Model.GameField;

import java.util.Iterator;

/**
 * Прямоугольная область клеток игрового поля.
 * Аналог CellularRegion, но адаптирован под класс Cell.
 */
public class GridRegion implements Iterable<Cell> {

    // -----------------------------
    // Поля состояния
    // -----------------------------

    private final Cell _leftTop;
    private final int _width;
    private final int _height;


    // -----------------------------
    // Конструкторы
    // -----------------------------

    public GridRegion(Cell leftTop, int width, int height) {
        _leftTop = leftTop;
        _width = width;
        _height = height;
    }

    // -----------------------------
    // Границы
    // -----------------------------

    public Cell getLeftTop() {
        return _leftTop;
    }

    public int getWidth() {
        return _width;
    }

    public int getHeight() {
        return _height;
    }

    public int getLeft() {
        return _leftTop.getCol();
    }

    public int getRight() {
        return getLeft() + _width - 1;
    }

    public int getTop() {
        return _leftTop.getRow();
    }

    public int getBottom() {
        return getTop() + _height - 1;
    }

    public GameField getField() {
        return _leftTop.getField();
    }

    // -----------------------------
    // Принадлежность клетки
    // -----------------------------

    public boolean contains(Cell cell) {
        boolean vertical = cell.getRow() >= getTop() && cell.getRow() <= getBottom();
        boolean horizontal = cell.getCol() >= getLeft() && cell.getCol() <= getRight();
        return vertical && horizontal;
    }


    // -----------------------------
    // Итератор
    // -----------------------------

    @Override
    public Iterator<Cell> iterator() {
        return new GridRegionIterator(this);
    }


    // -----------------------------
    // Пересечение
    // -----------------------------

    public GridRegion intersect(GridRegion other, GameField field) {

        int left = Math.max(this.getLeft(), other.getLeft());
        int right = Math.min(this.getRight(), other.getRight());
        int top = Math.max(this.getTop(), other.getTop());
        int bottom = Math.min(this.getBottom(), other.getBottom());

        if (left > right || top > bottom) {
            return new GridRegion(field.getCell(0, 0), 0, 0);
        }

        return new GridRegion(field.getCell(top, left),
                right - left + 1,
                bottom - top + 1);
    }


    // -----------------------------
    // Объединение
    // -----------------------------

    public GridRegion union(GridRegion other, GameField field) {

        int left = Math.min(this.getLeft(), other.getLeft());
        int right = Math.max(this.getRight(), other.getRight());
        int top = Math.min(this.getTop(), other.getTop());
        int bottom = Math.max(this.getBottom(), other.getBottom());

        return new GridRegion(field.getCell(top, left),
                right - left + 1,
                bottom - top + 1);
    }


    // -----------------------------
    // Итератор по клеткам
    // -----------------------------

    private static class GridRegionIterator implements Iterator<Cell> {

        private final GridRegion _region;
        private final GameField _field;
        private int _row;
        private int _col;

        public GridRegionIterator(GridRegion region) {
            _region = region;
            _field = region._leftTop.getField();
            _row = region.getTop();
            _col = region.getLeft();
        }

        @Override
        public boolean hasNext() {
            return _row <= _region.getBottom()
                    && _row < _field.getHeight();
        }


        @Override
        public Cell next() {
            if (!hasNext()) throw new IndexOutOfBoundsException();

            Cell cell = _field.getCell(_row, _col);

            _col++;
            if (_col > _region.getRight()) {
                _col = _region.getLeft();
                _row++;
            }

            return cell;
        }

    }
}
