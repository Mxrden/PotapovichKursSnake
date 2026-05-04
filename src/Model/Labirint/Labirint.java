package Model.Labirint;

import Model.GameField.Cell;
import Model.GameField.GridRegion;

public class Labirint {
    private final GridRegion _region;
    private final Cell _entrance;
    private final Cell _exit;

    public Labirint(GridRegion region) {
        _region = region;
        _entrance = region.getField().getCell(
                region.getTop(),
                region.getLeft() + region.getWidth() / 2
        );
        _exit = region.getField().getCell(
                region.getBottom(),
                region.getLeft() + region.getWidth() / 2
        );
    }

    public GridRegion getRegion() {
        return _region;
    }

    public Cell getEntranceCell() {
        return _entrance;
    }

    public Cell getExitCell() {
        return _exit;
    }

    public boolean isEntranceOrExit(Cell cell) {
        return cell != null && (cell == _entrance || cell == _exit);
    }
}