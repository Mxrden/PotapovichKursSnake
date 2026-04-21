package Model.Labirint;

import Model.GameField.Cell;
import Model.GameField.GameField;
import Model.GameField.GridRegion;
import Model.Units.Wall;

public class Labirint {

    private final GridRegion _region;

    public Labirint(GridRegion region) {
        _region = region;
    }

    public GridRegion get_region() {
        return _region;
    }

    // ------------------------------------------------------------
    // Вспомогательные методы для работы со стенами-клетками
    // ------------------------------------------------------------
    private void putWall(Cell cell) {
        if (cell != null && cell.isEmpty() && !isEntranceOrExit(cell)) {
            cell.putUnit(new Wall());
        }
    }

    private void removeWall(Cell cell) {
        if (cell != null && cell.getUnit() instanceof Wall) {
            cell.extractUnit();
        }
    }

    private boolean isEntranceOrExit(Cell cell) {
        return cell != null && (cell == getEntranceCell() || cell == getExitCell());
    }

    private void clearWalls() {
        for (Cell cell : _region) {
            if (cell.getUnit() instanceof Wall) {
                cell.extractUnit();
            }
        }
    }

    // ------------------------------------------------------------
    // Генерация простого лабиринта (шахматные стены-клетки)
    // ------------------------------------------------------------
    public void generateSimple() {
        clearWalls();

        GameField field = _region.getField();
        int top = _region.getTop();
        int left = _region.getLeft();
        int right = _region.getRight();
        int bottom = _region.getBottom();
        int width = _region.getWidth();
        int height = _region.getHeight();

        for (int c = left; c <= right; c++) {
            putWall(field.getCell(top, c));
            putWall(field.getCell(bottom, c));
        }
        for (int r = top + 1; r < bottom; r++) {
            putWall(field.getCell(r, left));
            putWall(field.getCell(r, right));
        }

        int segmentHeight = height / 2;

        for (int col = left + 2; col < right - 1; col += 3) {
            boolean offset = ((col - left) / 3) % 2 == 1;

            int startRow = offset ? top + segmentHeight : top + 1;
            int endRow   = offset ? bottom - 1 : top + segmentHeight;

            for (int r = startRow; r < endRow; r++) {
                putWall(field.getCell(r, col));
            }
        }

        for (int col = left + 2; col < right - 1; col += 3) {
            boolean offset = ((col - left) / 3) % 2 == 1;
            int passRow = offset ? top + segmentHeight - 1 : top + segmentHeight + 1;

            if (passRow > top && passRow < bottom) {
                removeWall(field.getCell(passRow, col));
            }
        }

        removeWall(getEntranceCell());
        removeWall(getExitCell());
    }

    // ------------------------------------------------------------
    // Вход и выход (клетки без стен)
    // ------------------------------------------------------------
    public Cell getEntranceCell() {
        return _region.getField().getCell(
                _region.getTop(),
                _region.getLeft() + _region.getWidth() / 2
        );
    }

    public Cell getExitCell() {
        return _region.getField().getCell(
                _region.getBottom(),
                _region.getLeft() + _region.getWidth() / 2
        );
    }
}