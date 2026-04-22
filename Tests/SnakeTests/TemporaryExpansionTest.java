package SnakeTests;

import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.GameField.GameField;
import Model.Snake.Snake;
import Model.Snake.SnakeSegment;
import Model.Snake.TemporaryExpansion;
import Model.Units.Wall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TemporaryExpansionTest {

    private GameField field;
    private Snake snake;
    private Cell headCell;

    @BeforeEach
    void setUp() {
        field = new GameField(10, 10);
        snake = new Snake(3, 10, 30, 4, 2);
        headCell = field.getCell(5, 5);

        Cell bodyCell = field.getCell(5, 4);
        Cell tailCell = field.getCell(5, 3);

        List<SnakeSegment> segments = new ArrayList<>();
        SnakeSegment head = new SnakeSegment(true, 1.0f, headCell);
        head.setDirection(Direction.east());
        segments.add(head);

        SnakeSegment body = new SnakeSegment(false, 1.0f, bodyCell);
        body.setDirection(Direction.east());
        segments.add(body);

        SnakeSegment tail = new SnakeSegment(false, 1.0f, tailCell);
        tail.setDirection(Direction.east());
        segments.add(tail);

        headCell.putUnit(head);
        bodyCell.putUnit(body);
        tailCell.putUnit(tail);
        snake.initializeBody(segments, Direction.east());
    }

    @Test
    void testExpansionCreationSuccess() {
        TemporaryExpansion exp = new TemporaryExpansion(snake, 5);
        assertNotNull(exp);

        Cell north = headCell.getNeighbor(Direction.north());
        Cell south = headCell.getNeighbor(Direction.south());
        assertTrue(north.getUnit() instanceof SnakeSegment);
        assertTrue(south.getUnit() instanceof SnakeSegment);
        assertEquals(1.5f, ((SnakeSegment) north.getUnit()).getThickness());
    }

    @Test
    void testExpansionCreationFailsWhenNorthBlocked() {
        Cell north = headCell.getNeighbor(Direction.north());
        north.putUnit(new Wall());
        assertThrows(IllegalStateException.class, () -> new TemporaryExpansion(snake, 5));
    }

    @Test
    void testExpansionCreationFailsWhenSouthBlocked() {
        Cell south = headCell.getNeighbor(Direction.south());
        south.putUnit(new Wall());
        assertThrows(IllegalStateException.class, () -> new TemporaryExpansion(snake, 5));
    }

    @Test
    void testExpansionFollowsSnakeBody() {
        TemporaryExpansion exp = new TemporaryExpansion(snake, 2);

        Cell initialNorth = headCell.getNeighbor(Direction.north());
        Cell initialSouth = headCell.getNeighbor(Direction.south());
        assertTrue(initialNorth.getUnit() instanceof SnakeSegment);
        assertTrue(initialSouth.getUnit() instanceof SnakeSegment);

        snake.move();
        assertTrue(exp.tick());

        assertTrue(initialNorth.getUnit() instanceof SnakeSegment);
        assertTrue(initialSouth.getUnit() instanceof SnakeSegment);

        snake.setDirection(Direction.north());
        snake.move();
        assertFalse(exp.tick());

        assertNull(initialNorth.getUnit());
        assertNull(initialSouth.getUnit());
    }

    @Test
    void testVerticalDirectionExpansion() {
        Snake verticalSnake = new Snake(3, 10, 30, 4, 2);
        GameField verticalField = new GameField(10, 10);
        Cell center = verticalField.getCell(5, 5);
        Cell body = verticalField.getCell(6, 5);
        Cell tail = verticalField.getCell(7, 5);

        List<SnakeSegment> segments = new ArrayList<>();
        SnakeSegment head = new SnakeSegment(true, 1.0f, center);
        head.setDirection(Direction.north());
        segments.add(head);
        SnakeSegment bodySeg = new SnakeSegment(false, 1.0f, body);
        bodySeg.setDirection(Direction.north());
        segments.add(bodySeg);
        SnakeSegment tailSeg = new SnakeSegment(false, 1.0f, tail);
        tailSeg.setDirection(Direction.north());
        segments.add(tailSeg);

        center.putUnit(head);
        body.putUnit(bodySeg);
        tail.putUnit(tailSeg);
        verticalSnake.initializeBody(segments, Direction.north());

        TemporaryExpansion exp = new TemporaryExpansion(verticalSnake, 5);
        Cell west = center.getNeighbor(Direction.west());
        Cell east = center.getNeighbor(Direction.east());
        assertTrue(west.getUnit() instanceof SnakeSegment);
        assertTrue(east.getUnit() instanceof SnakeSegment);
        assertNotNull(exp);
    }
}
