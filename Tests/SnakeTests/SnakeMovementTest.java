package SnakeTests;

import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.Snake.SnakeMovement;
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
        Cell result = movement.computeTarget(headCell);
        assertNull(result);
    }

    @Test
    void testComputeMoveReturnsTargetCell() {
        when(headCell.getNeighbor(Direction.east())).thenReturn(targetCell);
        Cell result = movement.computeTarget(headCell);
        assertEquals(targetCell, result);
    }


    @Test
    void testSetDirectionValid() {
        movement.setDirection(Direction.north());
        assertEquals(Direction.north(), movement.getDirection());
    }

}
