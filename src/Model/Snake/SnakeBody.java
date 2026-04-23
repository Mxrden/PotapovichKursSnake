package Model.Snake;

import Model.GameField.Cell;
import Model.GameField.Direction;

import java.util.LinkedList;
import java.util.List;

public class SnakeBody {

    private final LinkedList<SnakeSegment> _segments = new LinkedList<>();

    public SnakeSegment head() { return _segments.peekFirst(); }
    public SnakeSegment tail() { return _segments.peekLast(); }
    public List<SnakeSegment> all() { return new java.util.ArrayList<>(_segments); }
    public int size() { return _segments.size(); }
    public boolean isEmpty() { return _segments.isEmpty(); }

    public void addHead(SnakeSegment seg) { _segments.addFirst(seg); }
    public void addTail(SnakeSegment seg) { _segments.addLast(seg); }

    public void removeTail() { _segments.removeLast(); }

    public void clear() { _segments.clear(); }

     void loadSegments(List<SnakeSegment> segments) {
        clear();
        if (segments == null) {
            return;
        }
        for (SnakeSegment segment : segments) {
            if (segment != null) {
                _segments.add(segment);
            }
        }
    }

    public Cell headCell() {
        SnakeSegment head = head();
        return head != null ? head.getPos() : null;
    }

    public SnakeSegment tailSegment() {
        return tail();
    }

    public void removeTailFromField() {
        if (_segments.isEmpty()) {
            return;
        }

        SnakeSegment tail = tail();
        Cell tailCell = tail.getPos();
        if (tailCell != null && tailCell.getUnit() == tail) {
            tailCell.extractUnit();
        }
        removeTail();
    }

    public boolean growTail(Cell cell, Direction direction) {
        if (cell == null || direction == null || !cell.isEmpty()) {
            return false;
        }

        SnakeSegment newSegment = new SnakeSegment(false, 1.0f, null);
        newSegment.setDirection(direction);
        if (!cell.putUnit(newSegment)) {
            return false;
        }

        newSegment.setPosition(cell);
        addTail(newSegment);
        return true;
    }

    public void updateDirections() {
        for (int i = 1; i < _segments.size(); i++) {
            SnakeSegment prev = _segments.get(i - 1);
            SnakeSegment curr = _segments.get(i);
            Direction dir = curr.getPos().getDirectionTo(prev.getPos());
            if (dir != null) curr.setDirection(dir);
        }
    }

    /**
     * Добавляет новую голову змеи в указанную клетку.
     * Старая голова перестает быть головой, новая занимает целевую клетку.
     * @param targetCell клетка для новой головы
     * @param direction направление движения
     * @return true если добавление удалось
     */
    public boolean addNewHead(Cell targetCell, Direction direction) {
        SnakeSegment currentHead = head();
        if (currentHead == null || targetCell == null) {
            return false;
        }

        SnakeSegment newHead = new SnakeSegment(true, 1.0f, null);
        newHead.setDirection(direction);
        if (!targetCell.putUnit(newHead)) {
            return false;
        }
        addHead(newHead);
        currentHead.setHead(false);
        updateDirections();
        return true;
    }
}
