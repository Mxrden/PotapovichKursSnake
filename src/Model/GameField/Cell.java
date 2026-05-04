package Model.GameField;

import Model.Units.Unit;
import Model.Snake.SnakeSegment;

import java.awt.geom.Point2D;
import java.util.*;

public class Cell {

    private final GameField _field;
    private final Point2D _position;
    private final List<Unit> _units = new ArrayList<>();

    public Cell(GameField field, int row, int col) {
        _field = field;
        _position = new Point2D.Double(row, col);
    }

    public GameField getField() { return _field; }
    public int getRow() { return (int) _position.getX(); }
    public int getCol() { return (int) _position.getY(); }

    private final Map<Direction, Cell> _neighbors = new HashMap<>();

    public void setNeighbor(Direction direction, Cell neighbor) { _neighbors.put(direction, neighbor); }
    public Map<Direction, Cell> getNeighbors() { return _neighbors; }
    public boolean isNeighbor(Cell other) { return _neighbors.containsValue(other); }
    public Cell getNeighbor(Direction direction) { return _neighbors.get(direction); }

    public List<Unit> getUnits() { return Collections.unmodifiableList(_units); }

    public Unit getTopUnit() {
        return _units.isEmpty() ? null : _units.get(_units.size() - 1);
    }

    public boolean putUnit(Unit unit) {
        if (unit == null) return false;
        if (!unit.canBelongTo(this)) return false;

        if (unit instanceof SnakeSegment) {
            for (Unit u : _units) {
                if (u instanceof SnakeSegment) return false;
            }
        } else {
            if (!_units.isEmpty()) return false;
        }

        _units.add(unit);
        unit.setPosition(this);
        return true;
    }

    public boolean removeUnit(Unit unit) {
        if (unit == null) return false;
        boolean removed = _units.remove(unit);
        if (removed) unit.setPosition(null);
        return removed;
    }

    public boolean isEmpty() {
        return _units.isEmpty();
    }

    public boolean hasWall() {
        for (Unit u : _units) {
            if (u instanceof Model.Units.Wall) return true;
        }
        return false;
    }

    public boolean hasStone() {
        for (Unit u : _units) {
            if (u instanceof Model.Units.Stone) return true;
        }
        return false;
    }

    public Direction getDirectionTo(Cell other) {
        if (other == null) return null;
        for (Map.Entry<Direction, Cell> entry : _neighbors.entrySet()) {
            if (entry.getValue() == other) return entry.getKey();
        }
        return null;
    }
}