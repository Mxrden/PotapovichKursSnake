package Model.Snake;

import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.Units.Unit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Класс змеи - основной игровой объект.
 * Реализует принцип единственной ответственности - управление состоянием и движением змеи.
 */
public class Snake {

    private final SnakeBody body = new SnakeBody();
    private final SnakeMovement movement = new SnakeMovement();
    private final SnakeHunger hunger;

    private boolean ignoreNextWall = false;
    private boolean ignoreNextStone = false;
    private boolean rodentEaten = false;
    private final java.util.List<TemporaryExpansion> expansions = new java.util.ArrayList<>();
    private Direction requestedDirection = null;

    public Snake(int minLength, int initialLife,
                 int shrinkInterval, int hpLossInterval, int hungerDamage) {
        this.hunger = new SnakeHunger(minLength, initialLife,
                shrinkInterval, hpLossInterval, hungerDamage);
    }

    public void activateIgnoreWall() { ignoreNextWall = true; }
    public void activateIgnoreStone() { ignoreNextStone = true; }

    public void setDirection(Direction dir) {
        if (dir == null) return;
        requestedDirection = dir;
    }

    public void setDirectionImmediate(Direction dir) {
        movement.setDirection(dir);
    }

    public Direction getDirection() { return movement.getDirection(); }
    public SnakeBody getBody() { return body; }
    public java.util.List<SnakeSegment> getSegments() {
        return new java.util.ArrayList<>(body.all());
    }
    public SnakeSegment getHead() { return body.head(); }
    public boolean isDead() { return hunger.isDead(); }

    /**
     * Убивает змею и очищает все временные расширения.
     */
    public void kill() {
        hunger.kill();
        for (TemporaryExpansion exp : expansions) exp.dispose();
        expansions.clear();
    }
    public void increaseGrowthQueue() { hunger.addGrowth(); }
    public boolean wasRodentEaten() { return rodentEaten; }

    public boolean tryAddExpansion(Cell expansionCell) {
        int currentLength = body.size();
        if (currentLength <= 0) return false;
        Direction dir = movement.getDirection();
        try {
            TemporaryExpansion exp = new TemporaryExpansion(expansionCell, dir, currentLength);
            expansions.add(exp);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    private void updateExpansions() {
        java.util.Iterator<TemporaryExpansion> it = expansions.iterator();
        while (it.hasNext()) {
            TemporaryExpansion exp = it.next();
            boolean alive = exp.tick();
            if (!alive) {
                it.remove();
                growFromExpansion();
            }
        }
    }

    private void growFromExpansion() {
        if (body.isEmpty()) return;
        SnakeSegment tail = body.tail();
        Direction tailDirection = tail.getDirection();
        if (tailDirection == null) return;
        Cell tailCell = tail.getPos();
        Cell newCell = tailCell.getNeighbor(tailDirection.opposite());
        if (newCell != null && newCell.isEmpty()) {
            SnakeSegment newSegment = new SnakeSegment(false, 1.0f, null);
            newSegment.setDirection(tailDirection);
            newCell.putUnit(newSegment);
            newSegment.setPosition(newCell);
            body.addTail(newSegment);
        } else {
            kill();
        }
    }

    public boolean move() {
        if (requestedDirection != null) {
            Direction currentDir = movement.getDirection();
            if (!currentDir.isOpposite(requestedDirection)) {
                movement.setDirection(requestedDirection);
            }
            requestedDirection = null;
        }

        rodentEaten = false;

        if (body.isEmpty()) {
            hunger.kill();
            return false;
        }

        Cell headCell = body.head().getPos();
        SnakeMovement.MoveResult move = movement.computeMove(headCell, ignoreNextWall, ignoreNextStone);

        boolean wallIgnored = (move.obstacle == SnakeMovement.Obstacle.WALL_IGNORED);
        boolean stoneIgnored = (move.obstacle == SnakeMovement.Obstacle.STONE_IGNORED);
        if (wallIgnored || stoneIgnored) {
            ignoreNextWall = false;
            ignoreNextStone = false;
        }

        if (move.target == null) {
            hunger.kill();
            return false;
        }

        Cell target = move.target;
        boolean grow = hunger.shouldGrow();

        if (!target.isEmpty()) {
            Unit unit = target.getUnit();
            Unit.UnitType type = (unit != null) ? unit.getType() : null;

            if (type == Unit.UnitType.RODENT) {
                rodentEaten = true;
                unit.onSteppedBy(this);
            } else if (wallIgnored || stoneIgnored) {
                target.extractUnit();
            } else {
                unit.onSteppedBy(this);
                if (hunger.isDead()) return false;
            }
        }

        boolean success = body.addNewHead(target, movement.getDirection());
        if (!success) {
            hunger.kill();
            return false;
        }

        if (!grow) {
            Cell tailCell = body.tail().getPos();
            if (tailCell != null && tailCell.getUnit() == body.tail()) {
                tailCell.extractUnit();
            }
            body.removeTail();
        } else {
            hunger.consumeGrowth();
        }

        boolean needShrink = hunger.applyHunger(body.size());
        if (needShrink) {
            Cell tailCell = body.tail().getPos();
            if (tailCell != null && tailCell.getUnit() == body.tail()) {
                tailCell.extractUnit();
            }
            body.removeTail();
        }

        updateExpansions();
        return !hunger.isDead();
    }
}