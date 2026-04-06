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

    /**
     * Обновляет направление каждого сегмента (кроме головы),
     * чтобы оно указывало на следующий сегмент по направлению к голове.
     */
    public void updateDirections() {
        for (int i = 1; i < segments.size(); i++) {
            SnakeSegment prev = segments.get(i - 1);
            SnakeSegment curr = segments.get(i);
            Direction dir = curr.getPos().getDirectionTo(prev.getPos());
            if (dir != null) curr.setDirection(dir);
        }
    }

    /**
     * Выполняет сдвиг тела: новая голова в targetCell,
     * старая голова становится телом, при необходимости удаляется хвост.
     * @param targetCell клетка для новой головы
     * @param grow должна ли змея расти (не удалять хвост)
     * @param direction направление движения
     * @return true если успешно
     */
    public boolean shiftTo(Cell targetCell, boolean grow, Direction direction) {
        head().setHead(false);

        SnakeSegment newHead = new SnakeSegment(true, 1.0f, null);
        newHead.setDirection(direction);
        if (!targetCell.putUnit(newHead)) {
            return false;
        }
        addHead(newHead);

        updateDirections();

        if (!grow) {
            Cell tailCell = tail().getPos();
            if (tailCell != null && tailCell.getUnit() == tail()) {
                tailCell.extractUnit();
            }
            removeTail();
        }
        return true;
    }
}