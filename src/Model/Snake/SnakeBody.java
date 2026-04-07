package Model.Snake;

import Model.GameField.Cell;
import Model.GameField.Direction;
import java.util.LinkedList;
import java.util.List;

public class SnakeBody {

    private final LinkedList<SnakeSegment> segments = new LinkedList<>();

    public SnakeSegment head() { return segments.getFirst(); }
    public SnakeSegment tail() { return segments.getLast(); }
    public List<SnakeSegment> all() { return segments; }
    public int size() { return segments.size(); }
    public boolean isEmpty() { return segments.isEmpty(); }

    public void addHead(SnakeSegment seg) { segments.addFirst(seg); }
    public void addTail(SnakeSegment seg) { segments.addLast(seg); }
    public void removeTail() { segments.removeLast(); }

    public void updateDirections() {
        for (int i = 1; i < segments.size(); i++) {
            SnakeSegment prev = segments.get(i - 1);
            SnakeSegment curr = segments.get(i);
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