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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TemporaryExpansionTest {

    @Mock private Snake snake;
    private GameField field;
    private Cell headCell;
    private Direction dir = Direction.east();

    @BeforeEach
    void setUp() {
        field = new GameField(10, 10);
        headCell = field.getCell(5, 5);
    }

    @Test
    void testExpansionCreationSuccess() {
        TemporaryExpansion exp = new TemporaryExpansion(headCell, dir, 5);
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
        assertThrows(IllegalStateException.class, () -> new TemporaryExpansion(headCell, dir, 5));
    }

    @Test
    void testExpansionCreationFailsWhenSouthBlocked() {
        Cell south = headCell.getNeighbor(Direction.south());
        south.putUnit(new Wall());
        assertThrows(IllegalStateException.class, () -> new TemporaryExpansion(headCell, dir, 5));
    }

    @Test
    void testExpansionCreationFailsWhenSnakeBodyBlocksSpawnCell() {
        Cell north = headCell.getNeighbor(Direction.north());
        SnakeSegment body = new SnakeSegment(false, 1.0f, null);
        body.setDirection(Direction.east());
        assertTrue(north.putUnit(body));

        assertThrows(IllegalStateException.class, () -> new TemporaryExpansion(headCell, dir, 5));
    }

    @Test
    void testTickAndDispose() {
        TemporaryExpansion exp = new TemporaryExpansion(headCell, dir, 2);
        Cell north = headCell.getNeighbor(Direction.north());
        Cell south = headCell.getNeighbor(Direction.south());
        assertTrue(exp.tick());
        assertTrue(north.getUnit() instanceof SnakeSegment);
        assertTrue(south.getUnit() instanceof SnakeSegment);
        assertFalse(exp.tick());
        assertNull(north.getUnit());
        assertNull(south.getUnit());
    }

    @Test
    void testVerticalDirectionExpansion() {
        dir = Direction.north();
        headCell = field.getCell(5, 5);
        TemporaryExpansion exp = new TemporaryExpansion(headCell, dir, 5);
        Cell west = headCell.getNeighbor(Direction.west());
        Cell east = headCell.getNeighbor(Direction.east());
        assertTrue(west.getUnit() instanceof SnakeSegment);
        assertTrue(east.getUnit() instanceof SnakeSegment);
    }
}
