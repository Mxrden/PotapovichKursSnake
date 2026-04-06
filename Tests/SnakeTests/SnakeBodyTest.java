package SnakeTests;

import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.GameField.GameField;
import Model.Snake.SnakeBody;
import Model.Snake.SnakeSegment;
import Model.Units.Stone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SnakeBodyTest {

    private SnakeBody body;
    private GameField field;
    private Cell[] cells;

    @BeforeEach
    void setUp() {
        field = new GameField(10, 10);
        body = new SnakeBody();
        cells = new Cell[5];
        for (int i = 0; i < 5; i++) {
            cells[i] = field.getCell(0, i);
        }

        SnakeSegment head = new SnakeSegment(true, 1.0f, cells[2]);
        head.setDirection(Direction.east());
        body.addHead(head);
        SnakeSegment body1 = new SnakeSegment(false, 1.0f, cells[1]);
        body1.setDirection(Direction.east());
        body.addTail(body1);
        SnakeSegment tail = new SnakeSegment(false, 1.0f, cells[0]);
        tail.setDirection(Direction.east());
        body.addTail(tail);
    }

    @Test
    void testHeadAndTail() {
        assertEquals(cells[2], body.head().getPos());
        assertEquals(cells[0], body.tail().getPos());
        assertEquals(3, body.size());
    }

    @Test
    void testUpdateDirections() {
        body.updateDirections();
        assertEquals(Direction.east(), body.head().getDirection());
        assertEquals(Direction.east(), body.all().get(1).getDirection());
        assertEquals(Direction.east(), body.tail().getDirection());
    }

    @Test
    void testShiftToWithoutGrowth() {
        Cell newHeadCell = cells[3];
        boolean success = body.shiftTo(newHeadCell, false, Direction.east());
        assertTrue(success);
        assertEquals(3, body.size());
        assertEquals(newHeadCell, body.head().getPos());
        assertEquals(cells[2], body.all().get(1).getPos());
        assertEquals(cells[1], body.tail().getPos());
        assertTrue(body.head().isHead());
        assertFalse(body.all().get(1).isHead());
        assertEquals(Direction.east(), body.head().getDirection());
        assertEquals(Direction.east(), body.all().get(1).getDirection());
        assertEquals(Direction.east(), body.tail().getDirection());
    }

    @Test
    void testShiftToWithGrowth() {
        Cell newHeadCell = cells[3];
        int oldSize = body.size();
        boolean success = body.shiftTo(newHeadCell, true, Direction.east());
        assertTrue(success);
        assertEquals(oldSize + 1, body.size());
        assertEquals(newHeadCell, body.head().getPos());
        assertEquals(cells[2], body.all().get(1).getPos());
        assertEquals(cells[0], body.tail().getPos());
    }

    @Test
    void testShiftToFailsWhenTargetOccupied() {
        Cell target = cells[3];
        target.putUnit(new Stone());
        boolean success = body.shiftTo(target, false, Direction.east());
        assertFalse(success);
        assertEquals(3, body.size());
        assertEquals(cells[2], body.head().getPos());
    }
}