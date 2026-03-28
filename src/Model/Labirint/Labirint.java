package Model.Labirint;

import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.GameField.GridRegion;

public class Labirint {

    private final GridRegion _region;

    public Labirint(Cell leftTop, int width, int height) {
        _region = new GridRegion(leftTop, width, height);
    }

    public Labirint(GridRegion region) {
        _region = region;
    }

    // -----------------------------
    // Границы лабиринта
    // -----------------------------

    public boolean containsCell(Cell cell) {
        int row = cell.getRow();
        int col = cell.getCol();

        return row >= _region.getTop()
                && row <= _region.getBottom()
                && col >= _region.getLeft()
                && col <= _region.getRight();
    }

    public Cell getEntranceCell() {
        return _region.getField().getCell(
                _region.getBottom(),
                _region.getLeft()
        );
    }

    public Cell getExitCell() {
        return _region.getField().getCell(
                _region.getBottom(),
                _region.getRight()
        );
    }

    public int getWidth() {
        return _region.getWidth();
    }

    public int getHeight() {
        return _region.getHeight();
    }

    // -----------------------------
    // Генерация стен
    // -----------------------------

    private void setWall(Cell cell, Direction dir) {
        cell.setEdgeObject(dir, new Wall(true));

        Cell neighbor = cell.getNeighbor(dir);
        if (neighbor != null) {
            neighbor.setEdgeObject(dir.opposite(), new Wall(true));
        }
    }

    public void generateThreeColumnsWithGaps() {
        int left = _region.getLeft();
        int right = _region.getRight();
        int width = _region.getWidth();

        int col1 = left + width / 4;
        int col2 = left + width / 2;
        int col3 = left + 3 * width / 4;

        for (Cell cell : _region) {
            int col = cell.getCol();
            int row = cell.getRow();

            // Внешние стены
            if (row == _region.getTop()) setWall(cell, Direction.north());
            if (row == _region.getBottom()) setWall(cell, Direction.south());
            if (col == left) setWall(cell, Direction.west());
            if (col == right) setWall(cell, Direction.east());

            // Вертикальные колонны
            if (col == col1 || col == col2 || col == col3) {
                if (row % 3 != 0) {
                    setWall(cell, Direction.west());
                }
            }
        }

        // Вход и выход
        getEntranceCell().removeEdgeObject(Direction.south());
        getExitCell().removeEdgeObject(Direction.south());
    }
}
