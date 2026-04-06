package SnakeTests;

import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.Snake.SnakeMovement;
import Model.Units.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SnakeMovementTest {

    @Mock private Cell headCell;
    @Mock private Cell targetCell;
    private SnakeMovement movement;

    @BeforeEach
    void setUp() {
        movement = new SnakeMovement();
        movement.setDirection(Direction.east());
    }

    @Test
    void testComputeMoveBoundary() {
        when(headCell.getNeighbor(Direction.east())).thenReturn(null);
        SnakeMovement.MoveResult result = movement.computeMove(headCell, false, false);
        assertEquals(SnakeMovement.Obstacle.BOUNDARY, result.obstacle);
        assertNull(result.target);
    }

    @Test
    void testComputeMoveWall() {
        when(headCell.getNeighbor(Direction.east())).thenReturn(targetCell);
        WallUnit wall = mock(WallUnit.class);
        when(targetCell.getUnit()).thenReturn(wall);
        SnakeMovement.MoveResult result = movement.computeMove(headCell, false, false);
        assertEquals(SnakeMovement.Obstacle.WALL, result.obstacle);
        assertNull(result.target);
    }

    @Test
    void testComputeMoveWallIgnored() {
        when(headCell.getNeighbor(Direction.east())).thenReturn(targetCell);
        WallUnit wall = mock(WallUnit.class);
        when(targetCell.getUnit()).thenReturn(wall);
        SnakeMovement.MoveResult result = movement.computeMove(headCell, true, false);
        assertEquals(SnakeMovement.Obstacle.WALL_IGNORED, result.obstacle);
        assertEquals(targetCell, result.target);
    }

    @Test
    void testComputeMoveStone() {
        when(headCell.getNeighbor(Direction.east())).thenReturn(targetCell);
        Stone stone = mock(Stone.class);
        when(targetCell.getUnit()).thenReturn(stone);
        SnakeMovement.MoveResult result = movement.computeMove(headCell, false, false);
        assertEquals(SnakeMovement.Obstacle.STONE, result.obstacle);
        assertNull(result.target);
    }

    @Test
    void testComputeMoveStoneIgnored() {
        when(headCell.getNeighbor(Direction.east())).thenReturn(targetCell);
        Stone stone = mock(Stone.class);
        when(targetCell.getUnit()).thenReturn(stone);
        SnakeMovement.MoveResult result = movement.computeMove(headCell, false, true);
        assertEquals(SnakeMovement.Obstacle.STONE_IGNORED, result.obstacle);
        assertEquals(targetCell, result.target);
    }

    @Test
    void testComputeMoveEmpty() {
        when(headCell.getNeighbor(Direction.east())).thenReturn(targetCell);
        when(targetCell.getUnit()).thenReturn(null);
        SnakeMovement.MoveResult result = movement.computeMove(headCell, false, false);
        assertEquals(SnakeMovement.Obstacle.NONE, result.obstacle);
        assertEquals(targetCell, result.target);
    }

    @Test
    void testSetDirectionOpposite() {
        movement.setDirection(Direction.west());
        assertEquals(Direction.east(), movement.getDirection());
    }

    @Test
    void testSetDirectionValid() {
        movement.setDirection(Direction.north());
        assertEquals(Direction.north(), movement.getDirection());
    }

    @Test
    void testSetDirectionOppositeIgnored() {
        movement.setDirection(Direction.west());
        assertEquals(Direction.east(), movement.getDirection());
    }
}