package Model.Snake;

import Model.GameField.Cell;
import Model.GameField.Direction;
import java.util.LinkedList;
import java.util.List;

public class SnakeBody {

    private final LinkedList<SnakeSegment> _segments = new LinkedList<>();

    public SnakeSegment head() { return _segments.getFirst(); }
    public SnakeSegment tail() { return _segments.getLast(); }
    public List<SnakeSegment> all() { return _segments; }
    public int size() { return _segments.size(); }
    public boolean isEmpty() { return _segments.isEmpty(); }

    public void addHead(SnakeSegment seg) { _segments.addFirst(seg); }
    public void addTail(SnakeSegment seg) { _segments.addLast(seg); }
    public void removeTail() { _segments.removeLast(); }

    public void updateDirections() {
        for (int i = 1; i < _segments.size(); i++) {
            SnakeSegment prev = _segments.get(i - 1);
            SnakeSegment curr = _segments.get(i);
            Direction dir = curr.getPos().getDirectionTo(prev.getPos());
            if (dir != null) curr.setDirection(dir);
        }
    }

    /**
     * —оздаЄт новую голову, стара€ голова становитс€ телом.
     * ”даление хвоста больше не происходит здесь.
     * @param targetCell клетка дл€ новой головы
     * @param direction направление движени€
     * @return true если успешно
     */
    public boolean addNewHead(Cell targetCell, Direction direction) {
        head().setHead(false);
        SnakeSegment newHead = new SnakeSegment(true, 1.0f, null);
        newHead.setDirection(direction);
        if (!targetCell.putUnit(newHead)) {
            return false;
        }
        addHead(newHead);
        updateDirections();
        return true;
    }
}