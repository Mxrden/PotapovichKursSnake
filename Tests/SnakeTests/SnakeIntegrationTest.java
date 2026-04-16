package SnakeTests;

import Model.Game;
import Model.Snake.Snake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SnakeIntegrationTest {

    private Game game;
    private Snake snake;

    @BeforeEach
    void setUp() {
        game = new Game(20, 20, 3);
        snake = game.get_snake();
    }

    @Test
    void testSnakeMovesAndEats() {
        assertNotNull(game.get_rodent());
        for (int i = 0; i < 100; i++) {
            boolean moved = game.step();
            if (game.is_isOver()) break;
            assertTrue(moved);
        }
    }

}