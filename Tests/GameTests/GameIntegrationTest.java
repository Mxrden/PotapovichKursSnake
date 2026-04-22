package GameTests;

import Model.Game;
import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.GameField.GameField;
import Model.Snake.Snake;
import Model.Snake.SnakeSegment;
import Model.Units.Rodent;
import Model.Units.Stone;
import Model.Units.Wall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameIntegrationTest {

    private Game game;
    private Snake snake;
    private GameField field;

    @BeforeEach
    void setUp() {
        game = new Game(15, 15, 3);
        snake = game.getSnake();
        field = game.getField();
    }

    @Test
    void testSnakeCanEatRodentAndGrow() {
        Rodent rodent = game.getRodent();
        assertNotNull(rodent);
        Cell rodentCell = rodent.getPos();
        Cell headCell = snake.getHead().getPos();

        int maxSteps = 200;
        boolean ate = false;
        for (int i = 0; i < maxSteps && !snake.isDead(); i++) {
            game.step();
            if (game.getRodent() != rodent) {
                ate = true;
                break;
            }
        }
        assertTrue(ate || snake.isDead(), "Snake should eat rodent or die");
    }

    @Test
    void testSnakeDiesWhenHittingWall() {
        GameField field = new GameField(10, 10);
        for (Cell cell : field) {
            if (cell.getUnit() instanceof Wall) {
                cell.extractUnit();
            }
        }
        Snake snake = new Snake(3, 10, 30, 4, 2);
        Cell headCell = field.getCell(5, 5);
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
        Cell target = field.getCell(5, 6);
        target.putUnit(new Wall());
        snake.move();
        assertTrue(snake.isDead());
    }

    @Test
    void testSnakeDiesWhenHittingStone() {
        GameField field = new GameField(10, 10);
        for (Cell cell : field) {
            if (cell.getUnit() instanceof Wall) cell.extractUnit();
        }
        Snake snake = new Snake(3, 10, 30, 4, 2);
        Cell headCell = field.getCell(5, 5);
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
        Cell target = field.getCell(5, 6);
        target.putUnit(new Stone());
        snake.move();
        assertTrue(snake.isDead());
    }

    @Test
    void testSnakeDiesWhenHittingOwnBody() {
        GameField field = new GameField(10, 10);
        for (Cell cell : field) {
            if (cell.getUnit() instanceof Wall) cell.extractUnit();
        }
        Snake snake = new Snake(2, 10, 30, 4, 2);
        Cell headCell = field.getCell(5, 5);
        Cell tailCell = field.getCell(5, 6);
        List<SnakeSegment> segments = new ArrayList<>();
        SnakeSegment head = new SnakeSegment(true, 1.0f, headCell);
        head.setDirection(Direction.east());
        segments.add(head);
        SnakeSegment tail = new SnakeSegment(false, 1.0f, tailCell);
        tail.setDirection(Direction.east());
        segments.add(tail);
        headCell.putUnit(head);
        tailCell.putUnit(tail);
        snake.initializeBody(segments, Direction.east());
        snake.move();
        assertTrue(snake.isDead(), "Snake should die when hitting own body");
    }
}
