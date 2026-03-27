package Model.Labirint;

import Model.GameField.Cell;

import java.util.ArrayList;
import java.util.List;

public class Labirint {
    private List<Wall> _walls = new ArrayList<>();

    private final int _width;
    private final int _height;

    public Labirint(int width, int height) {
        this._width = width;
        this._height = height;
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

    public int getWidth() {
        return _width;
    }

    public int getHeight() {
        return _height;
    }
}
