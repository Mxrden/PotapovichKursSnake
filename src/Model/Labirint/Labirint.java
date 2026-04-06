package Model.Labirint;

import Model.GameField.Cell;
import Model.GameField.GameField;
import Model.GameField.GridRegion;
import Model.Units.WallUnit;

public class Labirint {

    private final GridRegion region;

    public Labirint(GridRegion region) {
        this.region = region;
    }

    public GridRegion getRegion() {
        return region;
    }

    // ------------------------------------------------------------
    // Вспомогательные методы для работы со стенами-клетками
    // ------------------------------------------------------------
    private void putWall(Cell cell) {
        if (cell != null && cell.isEmpty() && !isEntranceOrExit(cell)) {
            cell.putUnit(new WallUnit());
        }
    }

    private void removeWall(Cell cell) {
        if (cell != null && cell.getUnit() instanceof WallUnit) {
            cell.extractUnit();
        }
    }

    private boolean isEntranceOrExit(Cell cell) {
        return cell != null && (cell == getEntranceCell() || cell == getExitCell());
    }

    private void clearWalls() {
        for (Cell cell : region) {
            if (cell.getUnit() instanceof WallUnit) {
                cell.extractUnit();
            }
        }
    }

    // ------------------------------------------------------------
    // Генерация простого лабиринта (шахматные стены-клетки)
    // ------------------------------------------------------------
    public void generateSimple() {
        clearWalls();

        GameField field = region.getField();
        int top = region.getTop();
        int left = region.getLeft();
        int right = region.getRight();
        int bottom = region.getBottom();
        int width = region.getWidth();
        int height = region.getHeight();

        // 1. Внешняя рамка (стены по краям региона)
        for (int c = left; c <= right; c++) {
            putWall(field.getCell(top, c));     // верхняя граница
            putWall(field.getCell(bottom, c));  // нижняя граница
        }
        for (int r = top + 1; r < bottom; r++) {
            putWall(field.getCell(r, left));    // левая граница
            putWall(field.getCell(r, right));   // правая граница
        }

        // 2. Вертикальные шахматные стены (как в старой логике, но теперь целые клетки)
        int segmentHeight = height / 2;

        for (int col = left + 2; col < right - 1; col += 3) {
            boolean offset = ((col - left) / 3) % 2 == 1;

            int startRow = offset ? top + segmentHeight : top + 1;
            int endRow   = offset ? bottom - 1 : top + segmentHeight;

            for (int r = startRow; r < endRow; r++) {
                putWall(field.getCell(r, col));
            }
        }

        // 3. Проходы в стенах (убираем одну клетку-стену)
        for (int col = left + 2; col < right - 1; col += 3) {
            boolean offset = ((col - left) / 3) % 2 == 1;
            int passRow = offset ? top + segmentHeight - 1 : top + segmentHeight + 1;

            if (passRow > top && passRow < bottom) {
                removeWall(field.getCell(passRow, col));
            }
        }

        // 4. Убираем стены на входе и выходе (на всякий случай, если попали)
        removeWall(getEntranceCell());
        removeWall(getExitCell());
    }

    // ------------------------------------------------------------
    // Вход и выход (клетки без стен)
    // ------------------------------------------------------------
    public Cell getEntranceCell() {
        return region.getField().getCell(
                region.getTop(),
                region.getLeft() + region.getWidth() / 2
        );
    }

    public Cell getExitCell() {
        return region.getField().getCell(
                region.getBottom(),
                region.getLeft() + region.getWidth() / 2
        );
    }
}