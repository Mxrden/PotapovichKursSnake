package SnakeTests;

import Model.Snake.SnakeHunger;
import Model.Snake.SnakeGrowthQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SnakeHungerTest {

    private SnakeHunger hunger;
    private SnakeGrowthQueue growthQueue;
    private static final int MIN_LEN = 3;
    private static final int INIT_LIFE = 10;
    private static final int SHRINK_INTERVAL = 5;
    private static final int HP_LOSS_INTERVAL = 3;
    private static final int HUNGER_DAMAGE = 2;

    @BeforeEach
    void setUp() {
        hunger = new SnakeHunger(MIN_LEN, INIT_LIFE, SHRINK_INTERVAL, HP_LOSS_INTERVAL, HUNGER_DAMAGE);
        growthQueue = new SnakeGrowthQueue();
    }

    @Test
    void testInitialState() {
        assertFalse(hunger.isDead());
        assertTrue(growthQueue.isEmpty());
    }

    @Test
    void testAddAndConsumeGrowth() {
        growthQueue.addGrowth();
        assertTrue(growthQueue.shouldGrow());
        growthQueue.consumeGrowth();
        assertFalse(growthQueue.shouldGrow());
    }

    @Test
    void testMultipleGrowths() {
        growthQueue.addGrowth();
        growthQueue.addGrowth();
        assertTrue(growthQueue.shouldGrow());
        growthQueue.consumeGrowth();
        assertTrue(growthQueue.shouldGrow());
        growthQueue.consumeGrowth();
        assertFalse(growthQueue.shouldGrow());
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
        hunger.resetHunger();
        boolean needShrink = hunger.applyHunger(5);
        assertFalse(needShrink);
    }

    @Test
    void testResetHungerDoesNotQueueGrowth() {
        hunger.applyHunger(5);
        hunger.applyHunger(5);
        hunger.resetHunger();

        assertFalse(hunger.applyHunger(5));
        assertTrue(growthQueue.isEmpty());
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
