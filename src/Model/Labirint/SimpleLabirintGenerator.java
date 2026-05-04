package Model.Labirint;

import Model.GameField.Cell;
import Model.GameField.GridRegion;
import Model.Units.Wall;

public class SimpleLabirintGenerator implements LabirintGenerator {
    public static final int MIN_WIDTH = 3;
    public static final int MIN_HEIGHT = 3;

    @Override
    public Labirint generate(GridRegion region) {
        if (region.getWidth() < MIN_WIDTH || region.getHeight() < MIN_HEIGHT) {
            throw new IllegalArgumentException(
                    "Region too small for maze generation. Min width=" + MIN_WIDTH +
                            ", min height=" + MIN_HEIGHT + ", got " + region.getWidth() + "x" + region.getHeight()
            );
        }
        Labirint labirint = new Labirint(region);
        generateWalls(labirint);
        return labirint;
    }

    private void generateWalls(Labirint labirint) {
        clearWalls(labirint);
        GridRegion region = labirint.getRegion();
        int top = region.getTop();
        int left = region.getLeft();
        int bottom = region.getBottom();
        int right = region.getRight();
        int height = region.getHeight();

        for (int c = left; c <= right; c++) {
            putWall(labirint, region.getField().getCell(top, c));
            putWall(labirint, region.getField().getCell(bottom, c));
        }
        for (int r = top + 1; r < bottom; r++) {
            putWall(labirint, region.getField().getCell(r, left));
            putWall(labirint, region.getField().getCell(r, right));
        }

        int segmentHeight = height / 2;
        for (int col = left + 2; col < right - 1; col += 3) {
            boolean offset = ((col - left) / 3) % 2 == 1;
            int startRow = offset ? top + segmentHeight : top + 1;
            int endRow   = offset ? bottom - 1 : top + segmentHeight;
            for (int r = startRow; r < endRow; r++) {
                putWall(labirint, region.getField().getCell(r, col));
            }
        }

        for (int col = left + 2; col < right - 1; col += 3) {
            boolean offset = ((col - left) / 3) % 2 == 1;
            int passRow = offset ? top + segmentHeight - 1 : top + segmentHeight + 1;
            if (passRow > top && passRow < bottom) {
                removeWall(labirint, region.getField().getCell(passRow, col));
            }
        }

        removeWall(labirint, labirint.getEntranceCell());
        removeWall(labirint, labirint.getExitCell());
    }

    private void putWall(Labirint labirint, Cell cell) {
        if (cell != null && cell.isEmpty() && !labirint.isEntranceOrExit(cell)) {
            cell.putUnit(new Wall());
        }
    }

    private void removeWall(Labirint labirint, Cell cell) {
        if (cell != null) {
            for (Model.Units.Unit u : cell.getUnits()) {
                if (u instanceof Wall) {
                    cell.removeUnit(u);
                    break;
                }
            }
        }
    }

    private void clearWalls(Labirint labirint) {
        for (Cell cell : labirint.getRegion()) {
            for (Model.Units.Unit u : cell.getUnits()) {
                if (u instanceof Wall) {
                    cell.removeUnit(u);
                }
            }
        }
    }
}