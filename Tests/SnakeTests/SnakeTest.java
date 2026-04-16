package SnakeTests;

import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.GameField.GameField;
import Model.Snake.Snake;
import Model.Snake.SnakeSegment;
import Model.Units.Rodent;
import Model.Units.SimpleRodent;
import Model.Units.Stone;
import Model.Units.WallUnit;
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
        SnakeSegment head = new SnakeSegment(true, 1.0f, headCell);
        head.set_direction(Direction.east());
        snake.get_body().addHead(head);
        SnakeSegment body = new SnakeSegment(false, 1.0f, bodyCell);
        body.set_direction(Direction.east());
        snake.get_body().addTail(body);
        SnakeSegment tail = new SnakeSegment(false, 1.0f, tailCell);
        tail.set_direction(Direction.east());
        snake.get_body().addTail(tail);
        headCell.putUnit(head);
        bodyCell.putUnit(body);
        tailCell.putUnit(tail);
        snake.setDirection(Direction.east());
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
        assertFalse(((SnakeSegment) headCell.getUnit()).is_isHead());

        assertEquals(5, snake.getSegments().get(2).getPos().getRow());
        assertEquals(4, snake.getSegments().get(2).getPos().getCol());

        assertNull(tailCell.getUnit());

        assertEquals(3, snake.get_body().size());
    }

    @Test
    void testMoveIntoWallDies() {
        targetCell = field.getCell(5, 6);
        targetCell.putUnit(new WallUnit());
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
    void testMoveIntoOwnBodyDies() {
        snake.setDirection(Direction.west());
        Cell extra = field.getCell(5, 6);
        SnakeSegment extraSeg = new SnakeSegment(false, 1.0f, extra);
        extraSeg.set_direction(Direction.east());
        snake.get_body().addHead(extraSeg);
    }

    @Test
    void testEatRodentAndExpansion() {
        targetCell = field.getCell(5, 6);
        Rodent rodent = new SimpleRodent();
        targetCell.putUnit(rodent);

        assertTrue(snake.tryAddExpansion(targetCell));

        Cell north = targetCell.getNeighbor(Direction.north());
        Cell south = targetCell.getNeighbor(Direction.south());
        assertTrue(north.getUnit() instanceof SnakeSegment);
        assertTrue(south.getUnit() instanceof SnakeSegment);

        snake.move();
        assertTrue(snake.wasRodentEaten());
        assertTrue(targetCell.getUnit() instanceof SnakeSegment);
        assertTrue(((SnakeSegment) targetCell.getUnit()).is_isHead());
    }

    @Test
    void testGrowthQueueOldMechanism() {
        snake.increaseGrowthQueue();
        assertTrue(snake.get_body().size() == 3);
        targetCell = field.getCell(5, 6);
        snake.move();
        assertEquals(4, snake.get_body().size());
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

        SnakeSegment head = new SnakeSegment(true, 1.0f, hCell);
        head.set_direction(Direction.east());
        bigSnake.get_body().addHead(head);
        SnakeSegment body = new SnakeSegment(false, 1.0f, bCell);
        body.set_direction(Direction.east());
        bigSnake.get_body().addTail(body);
        SnakeSegment tail = new SnakeSegment(false, 1.0f, tCell);
        tail.set_direction(Direction.east());
        bigSnake.get_body().addTail(tail);
        hCell.putUnit(head);
        bCell.putUnit(body);
        tCell.putUnit(tail);
        bigSnake.setDirection(Direction.east());

        for (int i = 0; i < 2; i++) {
            bigSnake.increaseGrowthQueue();
            bigSnake.move();
        }
        assertEquals(5, bigSnake.get_body().size());

        for (int i = 0; i < 60; i++) {
            if (bigSnake.isDead()) break;
            bigSnake.move();
        }
        assertFalse(bigSnake.isDead(), "Snake died before shrinking");
        assertTrue(bigSnake.get_body().size() <= 3);
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
        head.set_direction(Direction.east());
        snake.get_body().addHead(head);
        SnakeSegment tail = new SnakeSegment(false, 1.0f, tailCell);
        tail.set_direction(Direction.east());
        snake.get_body().addTail(tail);
        headCell.putUnit(head);
        tailCell.putUnit(tail);
        snake.setDirection(Direction.east());
        snake.move();
        assertTrue(snake.isDead());
    }

    @Test
    void testExpansionGrowsAfterLifetime() throws InterruptedException {
        targetCell = field.getCell(5, 6);
        Rodent rodent = new SimpleRodent();
        targetCell.putUnit(rodent);
        assertTrue(snake.tryAddExpansion(targetCell));

        for (int i = 0; i < 3; i++) {

            Cell ahead = field.getCell(5, 6 + i);
            if (ahead.getUnit() != null) ahead.extractUnit();
            snake.move();
        }
        assertEquals(4, snake.get_body().size());
    }

    @Test
    void testDieWhenHittingExpansionSegment() {
        targetCell = field.getCell(5, 6);
        Rodent rodent = new SimpleRodent();
        targetCell.putUnit(rodent);
        assertTrue(snake.tryAddExpansion(targetCell));
        Cell northOfRodent = targetCell.getNeighbor(Direction.north());
        Cell southOfRodent = targetCell.getNeighbor(Direction.south());
        assertTrue(northOfRodent.getUnit() instanceof SnakeSegment);
        assertTrue(southOfRodent.getUnit() instanceof SnakeSegment);

        snake.move();

        snake.setDirection(Direction.north());
        snake.move();

        assertTrue(snake.isDead());
    }
}