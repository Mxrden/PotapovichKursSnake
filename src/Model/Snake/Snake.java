package Model.Snake;

import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.Units.Unit;

public class Snake {

    private SnakeBody _body = new SnakeBody();
    private final SnakeMovement _movement = new SnakeMovement();
    private final SnakeHunger _hunger;
    private final SnakeGrowthQueue _growthQueue = new SnakeGrowthQueue();

    private boolean _ignoreNextWall = false;
    private boolean _ignoreNextStone = false;
    private boolean _rodentEaten = false;
    private final java.util.List<TemporaryExpansion> _expansions = new java.util.ArrayList<>();
    private Direction _requestedDirection = null;

    public Snake(int minLength, int initialLife,
                 int shrinkInterval, int hpLossInterval, int hungerDamage) {
        _hunger = new SnakeHunger(minLength, initialLife,
                shrinkInterval, hpLossInterval, hungerDamage);
    }

    public void activateIgnoreWall() { _ignoreNextWall = true; }
    public void activateIgnoreStone() { _ignoreNextStone = true; }
    public boolean tryIgnoreWall() {
        if (!_ignoreNextWall) return false;
        _ignoreNextWall = false;
        return true;
    }
    public boolean tryIgnoreStone() {
        if (!_ignoreNextStone) return false;
        _ignoreNextStone = false;
        return true;
    }
    public void setDirection(Direction dir) {
        if (dir == null) return;
        _requestedDirection = dir;
    }
    public void setDirectionImmediate(Direction dir) { _movement.setDirection(dir); }
    public Direction getDirection() { return _movement.getDirection(); }
    public boolean isBodyEmpty() { return _body.isEmpty(); }
    public int getBodySize() { return _body.size(); }
    public int getLife() { return _hunger.getLife(); }
    public java.util.List<SnakeSegment> getSegments() {
        return new java.util.ArrayList<>(_body.all());
    }
    public SnakeSegment getHead() { return _body.head(); }
    public boolean isDead() { return _hunger.isDead(); }

    public void kill() {
        _hunger.kill();
        for (TemporaryExpansion exp : _expansions) exp.dispose();
        _expansions.clear();
    }
    public void increaseGrowthQueue() {
        _growthQueue.addGrowth();
        _hunger.resetHunger();
    }
    public boolean wasRodentEaten() { return _rodentEaten; }

    public void initializeBody(java.util.List<SnakeSegment> segments, Direction direction) {
        if (segments == null || segments.isEmpty()) {
            throw new IllegalArgumentException("segments must not be empty");
        }
        if (direction == null) {
            throw new IllegalArgumentException("direction must not be null");
        }
        _body = new SnakeBody(segments);
        setDirectionImmediate(direction);
    }

    private void applyRequestedDirection() {
        if (_requestedDirection == null) return;
        Direction currentDir = _movement.getDirection();
        if (!currentDir.isOpposite(_requestedDirection)) {
            _movement.setDirection(_requestedDirection);
        }
        _requestedDirection = null;
    }

    private Cell getHeadCell() { return _body.headCell(); }

    private Cell getTargetCell() {
        Cell headCell = getHeadCell();
        return headCell == null ? null : _movement.computeTarget(headCell);
    }

    private boolean resolveTargetCell(Cell target) {
        if (target.isEmpty()) return true;
        Unit unit = target.getUnit();
        if (!unit.canSnakeEnter(this)) {
            unit.onSnakeBlocked(this);
            return false;
        }
        if (unit.grantsExpansion()) {
            _hunger.onRodentEaten();
            _rodentEaten = true;
        }
        unit.onSnakeEntered(this);
        return !_hunger.isDead();
    }

    private boolean addNewHead(Cell target) {
        return _body.addNewHead(target, _movement.getDirection());
    }

    private void removeTailSegment() { _body.removeTailFromField(); }

    private void applyHungerEffects() {
        if (_hunger.applyHunger(_body.size())) {
            removeTailSegment();
        }
    }

    private boolean shouldGrow() { return _growthQueue.shouldGrow(); }
    private void consumeGrowth() { _growthQueue.consumeGrowth(); }


    public boolean tryAddExpansion() {
        int currentLength = _body.size();
        if (currentLength <= 0) return false;
        try {
            TemporaryExpansion exp = new TemporaryExpansion(this, currentLength);
            _expansions.add(exp);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    private void updateExpansions() {
        java.util.Iterator<TemporaryExpansion> it = _expansions.iterator();
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
        SnakeSegment tail = _body.tailSegment();
        if (tail == null) return;
        Direction tailDirection = tail.getDirection();
        if (tailDirection == null) return;
        Cell tailCell = tail.getPos();
        Cell newCell = tailCell.getNeighbor(tailDirection.opposite());
        if (!_body.growTail(newCell, tailDirection)) {
            kill();
        }
    }

    public boolean move() {
        prepareMove();
        Cell target = getTargetCell();
        if (!canAdvanceTo(target)) return false;
        if (!resolveTargetCell(target)) return false;
        if (!addNewHead(target)) {
            _hunger.kill();
            return false;
        }
        completeMove();
        return !isDead();
    }

    private void prepareMove() {
        applyRequestedDirection();
        _rodentEaten = false;
    }

    private boolean canAdvanceTo(Cell target) {   // переименовано
        if (_body.isEmpty()) {
            _hunger.kill();
            return false;
        }
        if (target == null) {
            _hunger.kill();
            return false;
        }
        return true;
    }

    private void completeMove() {   // переименовано
        if (shouldGrow()) {
            consumeGrowth();
        } else {
            removeTailSegment();
        }
        applyHungerEffects();
        updateExpansions();
    }
}