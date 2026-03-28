package Model.Labirint;

import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.GameField.GridRegion;

import java.util.ArrayList;
import java.util.List;

public class Labirint {
    private final GridRegion _region;

    public Labirint(Cell leftTop, int width, int height) {
        _region = new GridRegion(leftTop, width, height);
    }

    public Labirint(GridRegion region) {
        _region = region;
    }


    public boolean containsCell(Cell position) {
        //TODO
        return true;
    }

    public int getWidth() {return _region.getWidth(); }

    public int getHeight() {
        return _region.getHeight();
    }

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

            // ¬нешние стены
            if (row == _region.getTop()) {
                setWall(cell, Direction.north());
            }
            if (row == _region.getBottom()) {
                setWall(cell, Direction.south());
            }
            if (col == left) {
                setWall(cell, Direction.west());
            }
            if (col == right) {
                setWall(cell, Direction.east());
            }

            // ¬ертикальные колонны Ч ставим стену между колонной и левым коридором
            if (col == col1 || col == col2 || col == col3) {

                // ƒелаем дырки каждые 3 клетки
                if (row % 3 != 0) {
                    setWall(cell, Direction.west());
                }
            }
        }

        // ¬ход и выход
        _region.getField().getCell(_region.getBottom(), left)
                .removeEdgeObject(Direction.south());
        _region.getField().getCell(_region.getBottom(), right)
                .removeEdgeObject(Direction.south());
    }



}
