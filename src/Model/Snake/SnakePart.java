package Model.Snake;

import Model.GameField.Cell;
import Model.Units.Unit;

public class SnakePart extends Unit {
    private boolean _isHead;
    private float _thickness;

    public boolean isHead() {
        return _isHead;
    }

    public void setHead(boolean flag) {
        _isHead = flag;
    }

    public float getThickness() {
        return _thickness;
    }

    public void setThickness(float thickness) {
        _thickness = thickness;
    }

    void moveTo(Cell cell) {
        setPosition(cell);
    }

    @Override
    public boolean canBelongTo(Cell cell) {
        return cell.isEmpty();
    }

    public SnakePart(boolean isHead, float thickness, Cell position) {
        setHead(isHead);
        setThickness(thickness);
        setPosition(position);
    }
}
