package SnakeTests;

import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.GameField.GameField;
import Model.Snake.Snake;
import Model.Snake.SnakeSegment;
import Model.Units.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SnakeTest {

    private GameField field;
    private Snake snake;
    private Cell headCell, bodyCell, tailCell, targetCell;

    @BeforeEach
    void setUp() {
        field = new GameField(20, 20);
        for (Cell cell : field) {
            for (Model.Units.Unit u : new ArrayList<>(cell.getUnits())) {
                if (!(u instanceof SnakeSegment)) cell.removeUnit(u);
            }
        }
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
        assertFalse(((SnakeSegment) headCell.getTopUnit()).isHead());

        assertEquals(5, snake.getSegments().get(2).getPos().getRow());
        assertEquals(4, snake.getSegments().get(2).getPos().getCol());

        assertTrue(tailCell.isEmpty());

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
        snake.addWallIgnoreCharge();

        boolean moved = snake.move();

        assertTrue(moved);
        assertFalse(snake.isDead());
        assertEquals(snake.getHead().getPos(), targetCell);
        boolean wallStillPresent = false;
        for (Model.Units.Unit u : targetCell.getUnits()) {
            if (u instanceof Wall) wallStillPresent = true;
        }
        assertTrue(wallStillPresent);
    }

    @Test
    void testMoveIntoStoneWithIgnorePassesThrough() {
        targetCell = field.getCell(5, 6);
        targetCell.putUnit(new Stone());
        snake.addStoneIgnoreCharge();

        boolean moved = snake.move();

        assertTrue(moved);
        assertFalse(snake.isDead());
        assertEquals(snake.getHead().getPos(), targetCell);
        boolean stoneStillPresent = false;
        for (Model.Units.Unit u : targetCell.getUnits()) {
            if (u instanceof Stone) stoneStillPresent = true;
        }
        assertTrue(stoneStillPresent);
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
        assertNotNull(north.getTopUnit());
        assertNotNull(south.getTopUnit());
        assertTrue(north.getTopUnit() instanceof SnakeSegment);
        assertTrue(south.getTopUnit() instanceof SnakeSegment);

        snake.move();
        assertTrue(snake.wasRodentEaten());

        Cell middleCell = snake.getSegments().get(1).getPos();
        Cell middleNorth = middleCell.getNeighbor(Direction.north());
        Cell middleSouth = middleCell.getNeighbor(Direction.south());
        if (middleNorth != null && middleSouth != null) {
            assertTrue(middleNorth.getTopUnit() instanceof SnakeSegment);
            assertTrue(middleSouth.getTopUnit() instanceof SnakeSegment);
        }
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
    void testExpansionGrowsAfterLifetime() throws InterruptedException {
        targetCell = field.getCell(5, 6);
        Rodent rodent = new SimpleRodent();
        targetCell.putUnit(rodent);
        assertTrue(snake.tryAddExpansion());

        for (int i = 0; i < 3; i++) {
            Cell ahead = field.getCell(5, 6 + i);
            for (Model.Units.Unit u : new ArrayList<>(ahead.getUnits())) {
                if (!(u instanceof SnakeSegment)) ahead.removeUnit(u);
            }
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
        assertNotNull(northOfHead.getTopUnit());
        assertNotNull(southOfHead.getTopUnit());
        assertTrue(northOfHead.getTopUnit() instanceof SnakeSegment);
        assertTrue(southOfHead.getTopUnit() instanceof SnakeSegment);

        snake.move();

        Cell middleCell = snake.getSegments().get(1).getPos();
        Cell middleNorth = middleCell.getNeighbor(Direction.north());
        Cell middleSouth = middleCell.getNeighbor(Direction.south());
        if (middleNorth != null && middleSouth != null) {
            assertTrue(middleNorth.getTopUnit() instanceof SnakeSegment);
            assertTrue(middleSouth.getTopUnit() instanceof SnakeSegment);
        }

        snake.setDirection(Direction.north());
        snake.move();
        assertFalse(snake.isDead());
    }
}