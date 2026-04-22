package Model.Snake;

import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.Units.Unit;

/**
 * Класс змеи - основной игровой объект.
 * Реализует принцип единственной ответственности - управление состоянием и движением змеи.
 */
public class Snake {

    private final SnakeBody _body = new SnakeBody();
    private final SnakeMovement _movement = new SnakeMovement();
    private final SnakeHunger _hunger;

    private boolean _ignoreNextWall = false;
    private boolean _ignoreNextStone = false;
    private boolean _rodentEaten = false;
    private final java.util.List<TemporaryExpansion> _expansions = new java.util.ArrayList<>();
    private Direction _requestedDirection = null;
    private Cell _rodentCellForExpansion = null;

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

    public void setDirectionImmediate(Direction dir) {
        _movement.setDirection(dir);
    }

    public Direction getDirection() { return _movement.getDirection(); }
    public SnakeBody getBody() { return _body; }
    public java.util.List<SnakeSegment> getSegments() {
        return new java.util.ArrayList<>(_body.all());
    }
    public SnakeSegment getHead() { return _body.head(); }
    public boolean isDead() { return _hunger.isDead(); }

    /**
     * Убивает змею и очищает все временные расширения.
     */
    public void kill() {
        _hunger.kill();
        for (TemporaryExpansion exp : _expansions) exp.dispose();
        _expansions.clear();
    }
    public void increaseGrowthQueue() { _hunger.addGrowth(); }
    public boolean wasRodentEaten() { return _rodentEaten; }

    public void initializeBody(java.util.List<SnakeSegment> segments, Direction direction) {
        if (segments == null || segments.isEmpty()) {
            throw new IllegalArgumentException("segments must not be empty");
        }
        if (direction == null) {
            throw new IllegalArgumentException("direction must not be null");
        }

        _body.clear();
        _body.addHead(segments.get(0));
        for (int i = 1; i < segments.size(); i++) {
            _body.addTail(segments.get(i));
        }
        setDirectionImmediate(direction);
    }

    private void applyRequestedDirection() {
        if (_requestedDirection == null) {
            return;
        }

        Direction currentDir = _movement.getDirection();
        if (!currentDir.isOpposite(_requestedDirection)) {
            _movement.setDirection(_requestedDirection);
        }
        _requestedDirection = null;
    }

    private Cell getHeadCell() {
        return _body.head().getPos();
    }

    private Cell getTargetCell() {
        Cell headCell = getHeadCell();
        if (headCell == null) {
            return null;
        }
        return _movement.computeTarget(headCell);
    }

    private boolean handleTargetCell(Cell target) {
        if (target.isEmpty()) {
            return true;
        }

        Unit unit = target.getUnit();
        if (!unit.canSnakeEnter(this)) {
            unit.onSnakeBlocked(this);
            return false;
        }

        if (unit.grantsExpansion()) {
            _hunger.onRodentEaten();
            _rodentEaten = true;
            _rodentCellForExpansion = target;
        }

        unit.onSnakeEntered(this);
        return !_hunger.isDead();
    }

    private boolean addNewHead(Cell target) {
        return _body.addNewHead(target, _movement.getDirection());
    }

    private void removeTailSegment() {
        _body.removeTailFromField();
    }

    private void applyHungerEffects() {
        if (_hunger.applyHunger(_body.size())) {
            removeTailSegment();
        }
    }

    public void setRodentCellForExpansion(Cell cell) {
        _rodentCellForExpansion = cell;
    }

    public boolean tryAddExpansion(Cell expansionCell) {
        int currentLength = _body.size();
        if (currentLength <= 0) return false;
        Direction dir = _movement.getDirection();

        Cell actualCell = (expansionCell != null) ? expansionCell : _rodentCellForExpansion;
        if (actualCell == null) {
            return false;
        }
        try {
            TemporaryExpansion exp = new TemporaryExpansion(actualCell, dir, currentLength);
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
        if (_body.isEmpty()) return;
        SnakeSegment tail = _body.tail();
        Direction tailDirection = tail.getDirection();
        if (tailDirection == null) return;
        Cell tailCell = tail.getPos();
        Cell newCell = tailCell.getNeighbor(tailDirection.opposite());
        if (newCell != null && newCell.isEmpty()) {
            SnakeSegment newSegment = new SnakeSegment(false, 1.0f, null);
            newSegment.setDirection(tailDirection);
            newCell.putUnit(newSegment);
            newSegment.setPosition(newCell);
            _body.addTail(newSegment);
        } else {
            kill();
        }
    }

    public boolean move() {
        applyRequestedDirection();
        _rodentEaten = false;

        if (_body.isEmpty()) {
            _hunger.kill();
            return false;
        }

        Cell target = getTargetCell();
        if (target == null) {
            _hunger.kill();
            return false;
        }

        boolean grow = _hunger.shouldGrow();

        if (!handleTargetCell(target)) {
            return false;
        }

        if (!addNewHead(target)) {
            _hunger.kill();
            return false;
        }

        if (grow) {
            _hunger.consumeGrowth();
        } else {
            removeTailSegment();
        }
        applyHungerEffects();
        updateExpansions();
        return !_hunger.isDead();
    }
}
