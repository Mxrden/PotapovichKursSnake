package SnakeTests;

import Model.Snake.SnakeHunger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SnakeHungerTest {

    private SnakeHunger hunger;
    private static final int MIN_LEN = 3;
    private static final int INIT_LIFE = 10;
    private static final int SHRINK_INTERVAL = 5;
    private static final int HP_LOSS_INTERVAL = 3;
    private static final int HUNGER_DAMAGE = 2;

    @BeforeEach
    void setUp() {
        hunger = new SnakeHunger(MIN_LEN, INIT_LIFE, SHRINK_INTERVAL, HP_LOSS_INTERVAL, HUNGER_DAMAGE);
    }

    @Test
    void testInitialState() {
        assertFalse(hunger.isDead());
        assertFalse(hunger.shouldGrow());
    }

    @Test
    void testAddAndConsumeGrowth() {
        hunger.addGrowth();
        assertTrue(hunger.shouldGrow());
        hunger.consumeGrowth();
        assertFalse(hunger.shouldGrow());
    }

    @Test
    void testMultipleGrowths() {
        hunger.addGrowth();
        hunger.addGrowth();
        assertTrue(hunger.shouldGrow());
        hunger.consumeGrowth();
        assertTrue(hunger.shouldGrow());
        hunger.consumeGrowth();
        assertFalse(hunger.shouldGrow());
    }

    @Test
    void testShrinkWhenAboveMinLength() {
        for (int i = 0; i < SHRINK_INTERVAL - 1; i++) {
            assertFalse(hunger.applyHunger(5));
        }
        assertTrue(hunger.applyHunger(5));
        assertFalse(hunger.applyHunger(5));
    }

    @Test
    void testHpLossWhenAtMinLength() {
        for (int i = 0; i < HP_LOSS_INTERVAL - 1; i++) {
            hunger.applyHunger(3);
            assertFalse(hunger.isDead());
        }
        hunger.applyHunger(3);
        assertFalse(hunger.isDead());
        for (int i = 0; i < (INIT_LIFE / HUNGER_DAMAGE); i++) {
            for (int j = 0; j < HP_LOSS_INTERVAL; j++) {
                hunger.applyHunger(3);
            }
        }
        assertTrue(hunger.isDead());
    }

    @Test
    void testKill() {
        hunger.kill();
        assertTrue(hunger.isDead());
    }

    @Test
    void testGrowthResetsTicks() {
        hunger.applyHunger(5);
        hunger.applyHunger(5);
        hunger.addGrowth();
        boolean needShrink = hunger.applyHunger(5);
        assertFalse(needShrink);
    }

    @Test
    void testShrinkMultipleTimes() {
        for (int i = 0; i < 4; i++) {
            assertFalse(hunger.applyHunger(5));
        }

        assertTrue(hunger.applyHunger(5));

        assertFalse(hunger.applyHunger(5));

        for (int i = 0; i < 3; i++) {
            assertFalse(hunger.applyHunger(5));
        }

        assertTrue(hunger.applyHunger(5));
    }
}