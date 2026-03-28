package Model.Labirint;

import Model.GameField.Cell;
import Model.GameField.GridRegion;

import java.util.ArrayList;
import java.util.List;

public class Labirint {
    private List<Wall> _walls = new ArrayList<>();


    private final GridRegion _region;

    public Labirint(Cell leftTop, int width, int height) {
        _region = new GridRegion(leftTop, width, height);
    }

    public Labirint(GridRegion region) {
        _region = region;
    }

    public List<Wall> getWalls() {
        return _walls;
    }

    public void addNewWall(Wall wall) {
        _walls.add(wall);
    }

    public boolean containsCell(Cell position) {
        //TODO
        return true;
    }

    public int getWidth() {return _region.getWidth(); }

    public int getHeight() {
        return _region.getHeight();
    }
}
