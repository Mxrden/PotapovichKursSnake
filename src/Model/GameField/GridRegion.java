package Model.GameField;

import java.util.Iterator;

public class GridRegion implements Iterable<Cell> {

    private final Cell _leftTop;
    private final int _width;
    private final int _height;

    public GridRegion(Cell leftTop, int width, int height) {
        _leftTop = leftTop;
        _width = width;
        _height = height;
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

    @Override
    public Iterator<Cell> iterator() {
        return new GridRegionIterator(this);
    }

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
