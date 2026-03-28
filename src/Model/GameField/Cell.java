package Model.GameField;
import Model.Labirint.Wall;
import Model.Units.Unit;

import java.awt.geom.Point2D;
import java.util.*;

public class Cell {

    // -----------------------------
    // Порождение
    // -----------------------------

    private final GameField _field;

    public GameField getField() {
        return _field;
    }

    // -----------------------------
    // Позиция
    // -----------------------------

    private final Point2D _position;

    public Cell(GameField field, int row, int col) {
        _field = field;
        _position = new Point2D.Double(row, col);
    }

    public int getRow() {
        return (int) _position.getX();
    }

    public int getCol() {
        return (int) _position.getY();
    }

    // -----------------------------
    // Соседи
    // -----------------------------

    private final Map<Direction, Cell> _neighbors = new HashMap<>();

    public void setNeighbor(Direction direction, Cell neighbor) {
        _neighbors.put(direction, neighbor);
    }

    public Map<Direction, Cell> getNeighbors() {
        return _neighbors;
    }

    public boolean isNeighbor(Cell other) {
        return _neighbors.containsValue(other);
    }

    public Cell getNeighbor(Direction direction) {
        return _neighbors.get(direction);
    }

    // -----------------------------
    // Хранение юнита
    // -----------------------------

    private Unit _unit;

    public Unit getUnit() {
        return _unit;
    }

    public Unit extractUnit() {
        Unit remove = _unit;
        _unit = null;
        return remove;
    }

    public boolean putUnit(Unit unit) {
        if (!isEmpty()) return false;
        if (!unit.canBelongTo(this)) return false;

        _unit = unit;
        return true;
    }

    public boolean isEmpty() {
        return getUnit()==null;
    }

    // -----------------------------
    // Препятствия
    // -----------------------------

    private final Map<Direction, Wall> _walls = new HashMap<>();

    public void setEdgeObject(Direction dir, Wall obj) {
        _walls.put(dir, obj);
    }

    public Wall getWall(Direction dir) {
        return _walls.get(dir);
    }

    public void removeEdgeObject(Direction dir) {
        _walls.remove(dir);
    }

}
