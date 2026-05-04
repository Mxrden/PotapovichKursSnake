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

class EffectRodentTest {

    private GameField field;
    private Snake snake;
    private Cell headCell, targetCell;

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
        targetCell = field.getCell(5, 6);
        for (Model.Units.Unit u : new ArrayList<>(targetCell.getUnits())) {
            targetCell.removeUnit(u);
        }
    }

    @Test
    void testWallIgnoreChargeUsedToPassThroughWall() {
        snake.addWallIgnoreCharge();
        targetCell.putUnit(new Wall());
        assertEquals(1, snake.getWallIgnoreCharges());

        boolean moved = snake.move();
        assertTrue(moved);
        assertFalse(snake.isDead());
        assertEquals(targetCell, snake.getHead().getPos());
        assertEquals(0, snake.getWallIgnoreCharges());

        boolean wallStillPresent = false;
        for (Model.Units.Unit u : targetCell.getUnits()) {
            if (u instanceof Wall) wallStillPresent = true;
        }
        assertTrue(wallStillPresent);
    }

    @Test
    void testStoneIgnoreChargeUsedToPassThroughStone() {
        snake.addStoneIgnoreCharge();
        targetCell.putUnit(new Stone());
        assertEquals(1, snake.getStoneIgnoreCharges());

        boolean moved = snake.move();
        assertTrue(moved);
        assertFalse(snake.isDead());
        assertEquals(targetCell, snake.getHead().getPos());
        assertEquals(0, snake.getStoneIgnoreCharges());

        boolean stoneStillPresent = false;
        for (Model.Units.Unit u : targetCell.getUnits()) {
            if (u instanceof Stone) stoneStillPresent = true;
        }
        assertTrue(stoneStillPresent);
    }

    @Test
    void testMultipleChargesAccumulate() {
        snake.addWallIgnoreCharge();
        snake.addWallIgnoreCharge();
        assertEquals(2, snake.getWallIgnoreCharges());

        targetCell.putUnit(new Wall());
        boolean moved = snake.move();
        assertTrue(moved);
        assertEquals(1, snake.getWallIgnoreCharges());
        assertFalse(snake.isDead());

        Cell nextCell = field.getCell(5, 7);
        nextCell.putUnit(new Wall());
        snake.setDirection(Direction.east());
        moved = snake.move();
        assertTrue(moved);
        assertEquals(0, snake.getWallIgnoreCharges());
        assertFalse(snake.isDead());
    }

    @Test
    void testSimpleRodentDoesNotGiveCharge() {
        SimpleRodent rodent = new SimpleRodent();
        targetCell.putUnit(rodent);

        int beforeWall = snake.getWallIgnoreCharges();
        int beforeStone = snake.getStoneIgnoreCharges();
        boolean moved = snake.move();
        assertTrue(moved);
        assertTrue(snake.wasRodentEaten());
        assertEquals(beforeWall, snake.getWallIgnoreCharges());
        assertEquals(beforeStone, snake.getStoneIgnoreCharges());
    }

    @Test
    void testWallIgnoreRodentAlsoGrantsExpansion() {
        WallIgnoreRodent rodent = new WallIgnoreRodent();
        targetCell.putUnit(rodent);

        Cell north = headCell.getNeighbor(Direction.north());
        Cell south = headCell.getNeighbor(Direction.south());
        north.putUnit(new Stone());
        south.putUnit(new Stone());

        boolean moved = snake.move();
        assertTrue(moved);
        assertTrue(snake.wasRodentEaten());
        assertEquals(1, snake.getWallIgnoreCharges());
    }
}