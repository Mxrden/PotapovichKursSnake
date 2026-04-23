package SnakeTests;

import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.GameField.GameField;
import Model.Snake.Snake;
import Model.Snake.SnakeSegment;
import Model.Units.Rodent;
import Model.Units.SimpleRodent;
import Model.Units.Stone;
import Model.Units.Wall;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SnakeTest {

    private GameField field;
    private Snake snake;
    private Cell headCell, bodyCell, tailCell, targetCell;

    @BeforeEach
    void setUp() {
        field = new GameField(20, 20);
        snake = new Snake(3, 10, 30, 4, 2);
        headCell = field.getCell(5, 5);
        bodyCell = field.getCell(5, 4);
        tailCell = field.getCell(5, 3);
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
    void testMoveForward() {
        targetCell = field.getCell(5, 6);
        assertTrue(targetCell.isEmpty());
        boolean moved = snake.move();
        assertTrue(moved);
        assertFalse(snake.isDead());

        Cell headPos = snake.getHead().getPos();
        assertEquals(5, headPos.getRow());
        assertEquals(6, headPos.getCol());

        assertEquals(5, snake.getSegments().get(1).getPos().getRow());
        assertEquals(5, snake.getSegments().get(1).getPos().getCol());
        assertFalse(((SnakeSegment) headCell.getUnit()).isHead());

        assertEquals(5, snake.getSegments().get(2).getPos().getRow());
        assertEquals(4, snake.getSegments().get(2).getPos().getCol());

        assertNull(tailCell.getUnit());

        assertEquals(3, snake.getBodySize());
    }

    @Test
    void testMoveIntoWallDies() {
        targetCell = field.getCell(5, 6);
        targetCell.putUnit(new Wall());
        snake.move();
        assertTrue(snake.isDead());
    }

    @Test
    void testMoveIntoStoneDies() {
        targetCell = field.getCell(5, 6);
        targetCell.putUnit(new Stone());
        snake.move();
        assertTrue(snake.isDead());
    }

    @Test
    void testMoveIntoWallWithIgnorePassesThrough() {
        targetCell = field.getCell(5, 6);
        targetCell.putUnit(new Wall());
        snake.activateIgnoreWall();

        boolean moved = snake.move();

        assertTrue(moved);
        assertFalse(snake.isDead());
        assertTrue(targetCell.getUnit() instanceof SnakeSegment);
        assertEquals(targetCell, snake.getHead().getPos());
    }

    @Test
    void testMoveIntoStoneWithIgnorePassesThrough() {
        targetCell = field.getCell(5, 6);
        targetCell.putUnit(new Stone());
        snake.activateIgnoreStone();

        boolean moved = snake.move();

        assertTrue(moved);
        assertFalse(snake.isDead());
        assertTrue(targetCell.getUnit() instanceof SnakeSegment);
        assertEquals(targetCell, snake.getHead().getPos());
    }

    @Test
    void testMoveIntoOwnBodyDies() {
        GameField ownField = new GameField(20, 20);
        Snake ownSnake = new Snake(2, 10, 30, 4, 2);
        Cell ownHeadCell = ownField.getCell(5, 5);
        Cell ownTailCell = ownField.getCell(5, 6);
        SnakeSegment head = new SnakeSegment(true, 1.0f, ownHeadCell);
        head.setDirection(Direction.east());
        SnakeSegment tail = new SnakeSegment(false, 1.0f, ownTailCell);
        tail.setDirection(Direction.east());
        List<SnakeSegment> segments = new ArrayList<>();
        segments.add(head);
        segments.add(tail);
        ownHeadCell.putUnit(head);
        ownTailCell.putUnit(tail);
        ownSnake.initializeBody(segments, Direction.east());
        ownSnake.move();
        assertTrue(ownSnake.isDead());
    }

    @Test
    void testEatRodentAndExpansion() {
        targetCell = field.getCell(5, 6);
        Rodent rodent = new SimpleRodent();
        targetCell.putUnit(rodent);

        assertTrue(snake.tryAddExpansion());

        Cell north = headCell.getNeighbor(Direction.north());
        Cell south = headCell.getNeighbor(Direction.south());
        assertTrue(north.getUnit() instanceof SnakeSegment);
        assertTrue(south.getUnit() instanceof SnakeSegment);

        snake.move();
        assertTrue(snake.wasRodentEaten());

        Cell middleCell = snake.getSegments().get(1).getPos();
        Cell middleNorth = middleCell.getNeighbor(Direction.north());
        Cell middleSouth = middleCell.getNeighbor(Direction.south());
        assertTrue(middleNorth.getUnit() instanceof SnakeSegment);
        assertTrue(middleSouth.getUnit() instanceof SnakeSegment);
    }

    @Test
    void testGrowthQueueOldMechanism() {
        snake.increaseGrowthQueue();
        assertEquals(3, snake.getBodySize());
        targetCell = field.getCell(5, 6);
        snake.move();
        assertEquals(4, snake.getBodySize());
        assertEquals(targetCell, snake.getHead().getPos());
        assertEquals(tailCell, snake.getSegments().get(3).getPos());
    }

    @Test
    void testHungerShrink() {
        GameField bigField = new GameField(200, 200);
        Snake bigSnake = new Snake(3, 10, 30, 4, 2);
        int center = 100;
        Cell hCell = bigField.getCell(center, center);
        Cell bCell = bigField.getCell(center, center - 1);
        Cell tCell = bigField.getCell(center, center - 2);

        List<SnakeSegment> segments = new ArrayList<>();
        SnakeSegment head = new SnakeSegment(true, 1.0f, hCell);
        head.setDirection(Direction.east());
        segments.add(head);
        SnakeSegment body = new SnakeSegment(false, 1.0f, bCell);
        body.setDirection(Direction.east());
        segments.add(body);
        SnakeSegment tail = new SnakeSegment(false, 1.0f, tCell);
        tail.setDirection(Direction.east());
        segments.add(tail);
        hCell.putUnit(head);
        bCell.putUnit(body);
        tCell.putUnit(tail);
        bigSnake.initializeBody(segments, Direction.east());

        for (int i = 0; i < 2; i++) {
            bigSnake.increaseGrowthQueue();
            bigSnake.move();
        }
        assertEquals(5, bigSnake.getBodySize());

        for (int i = 0; i < 60; i++) {
            if (bigSnake.isDead()) break;
            bigSnake.move();
        }
        assertFalse(bigSnake.isDead(), "Snake died before shrinking");
        assertTrue(bigSnake.getBodySize() <= 3);
    }

    @Test
    void testSetDirectionValid() {
        snake.setDirection(Direction.north());
        snake.move();
        assertEquals(Direction.north(), snake.getDirection());
    }

    @Test
    void testCannotReverseDirection() {
        snake.setDirection(Direction.west());
        snake.move();
        assertEquals(Direction.east(), snake.getDirection());
    }

    @Test
    void testDieWhenHittingOwnBody() {
        GameField field = new GameField(20, 20);
        Snake snake = new Snake(2, 10, 30, 4, 2);
        Cell headCell = field.getCell(5, 5);
        Cell tailCell = field.getCell(5, 6);
        SnakeSegment head = new SnakeSegment(true, 1.0f, headCell);
        head.setDirection(Direction.east());
        SnakeSegment tail = new SnakeSegment(false, 1.0f, tailCell);
        tail.setDirection(Direction.east());
        List<SnakeSegment> segments = new ArrayList<>();
        segments.add(head);
        segments.add(tail);
        headCell.putUnit(head);
        tailCell.putUnit(tail);
        snake.initializeBody(segments, Direction.east());
        snake.move();
        assertTrue(snake.isDead());
    }

    @Test
    void testExpansionGrowsAfterLifetime() throws InterruptedException {
        targetCell = field.getCell(5, 6);
        Rodent rodent = new SimpleRodent();
        targetCell.putUnit(rodent);
        assertTrue(snake.tryAddExpansion());

        for (int i = 0; i < 3; i++) {

            Cell ahead = field.getCell(5, 6 + i);
            if (ahead.getUnit() != null) ahead.extractUnit();
            snake.move();
        }
        assertEquals(4, snake.getBodySize());
    }

    @Test
    void testExpansionMovesWithSnake() {
        targetCell = field.getCell(5, 6);
        Rodent rodent = new SimpleRodent();
        targetCell.putUnit(rodent);
        assertTrue(snake.tryAddExpansion());

        Cell northOfHead = headCell.getNeighbor(Direction.north());
        Cell southOfHead = headCell.getNeighbor(Direction.south());
        assertTrue(northOfHead.getUnit() instanceof SnakeSegment);
        assertTrue(southOfHead.getUnit() instanceof SnakeSegment);

        snake.move();

        Cell middleCell = snake.getSegments().get(1).getPos();
        Cell middleNorth = middleCell.getNeighbor(Direction.north());
        Cell middleSouth = middleCell.getNeighbor(Direction.south());
        assertTrue(middleNorth.getUnit() instanceof SnakeSegment);
        assertTrue(middleSouth.getUnit() instanceof SnakeSegment);

        snake.setDirection(Direction.north());
        snake.move();

        assertFalse(snake.isDead());
    }
}
