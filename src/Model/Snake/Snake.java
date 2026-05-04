package Model.Snake;

import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.Units.Unit;

import java.util.LinkedList;
import java.util.List;

public class Snake {

    private final LinkedList<SnakeSegment> _segments = new LinkedList<>();
    private final SnakeMovement _movement = new SnakeMovement();
    private final SnakeHunger _hunger;
    private final SnakeGrowthQueue _growthQueue = new SnakeGrowthQueue();

    private boolean _rodentEaten = false;
    private final List<TemporaryExpansion> _expansions = new java.util.ArrayList<>();
    private Direction _requestedDirection = null;

    private int _wallIgnoreCharges = 0;
    private int _stoneIgnoreCharges = 0;
    private int _specialRodentsEaten = 0;

    public Snake(int minLength, int initialLife,
                 int shrinkInterval, int hpLossInterval, int hungerDamage) {
        _hunger = new SnakeHunger(minLength, initialLife,
                shrinkInterval, hpLossInterval, hungerDamage);
    }

    public void addWallIgnoreCharge() { _wallIgnoreCharges++; }
    public void addStoneIgnoreCharge() { _stoneIgnoreCharges++; }
    public void incrementSpecialRodentsEaten() { _specialRodentsEaten++; }
    public int getSpecialRodentsEaten() { return _specialRodentsEaten; }
    public int getWallIgnoreCharges() { return _wallIgnoreCharges; }
    public int getStoneIgnoreCharges() { return _stoneIgnoreCharges; }

    public boolean tryIgnoreWall() {
        if (_wallIgnoreCharges > 0) {
            _wallIgnoreCharges--;
            return true;
        }
        return false;
    }

    public boolean tryIgnoreStone() {
        if (_stoneIgnoreCharges > 0) {
            _stoneIgnoreCharges--;
            return true;
        }
        return false;
    }

    public void setDirection(Direction dir) {
        if (dir == null) return;
        _requestedDirection = dir;
    }
    public void setDirectionImmediate(Direction dir) { _movement.setDirection(dir); }
    public Direction getDirection() { return _movement.getDirection(); }
    public boolean isBodyEmpty() { return _segments.isEmpty(); }
    public int getBodySize() { return _segments.size(); }
    public int getLife() { return _hunger.getLife(); }
    public List<SnakeSegment> getSegments() { return new java.util.ArrayList<>(_segments); }
    public SnakeSegment getHead() { return _segments.peekFirst(); }
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

    public void initializeBody(List<SnakeSegment> segments, Direction direction) {
        if (segments == null || segments.isEmpty()) {
            throw new IllegalArgumentException("segments must not be empty");
        }
        if (direction == null) {
            throw new IllegalArgumentException("direction must not be null");
        }
        _segments.clear();
        for (SnakeSegment seg : segments) {
            if (seg != null) {
                _segments.add(seg);
            }
        }
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

    private Cell getHeadCell() {
        SnakeSegment head = getHead();
        return head != null ? head.getPos() : null;
    }

    private Cell getTargetCell() {
        Cell headCell = getHeadCell();
        return headCell == null ? null : _movement.computeTarget(headCell);
    }

    private boolean resolveTargetCell(Cell target) {
        Unit topUnit = target.getTopUnit();
        if (topUnit == null) return true;

        if (!topUnit.canSnakeEnter(this)) {
            topUnit.onSnakeBlocked(this);
            return false;
        }
        if (topUnit.grantsExpansion()) {
            _hunger.onRodentEaten();
            _rodentEaten = true;
        }
        topUnit.onSnakeEntered(this);
        return !_hunger.isDead();
    }

    private boolean addNewHead(Cell target) {
        SnakeSegment currentHead = getHead();
        if (currentHead == null || target == null) return false;

        SnakeSegment newHead = new SnakeSegment(true, 1.0f, null);
        newHead.setDirection(_movement.getDirection());
        if (!target.putUnit(newHead)) return false;

        _segments.addFirst(newHead);
        currentHead.setHead(false);
        updateDirections();
        return true;
    }

    private void removeTailSegment() {
        if (_segments.isEmpty()) return;
        SnakeSegment tail = _segments.peekLast();
        if (tail == null) return;
        Cell tailCell = tail.getPos();
        if (tailCell != null) {
            tailCell.removeUnit(tail);
        }
        _segments.removeLast();
    }

    private void applyHungerEffects() {
        if (_hunger.applyHunger(_segments.size())) {
            removeTailSegment();
        }
    }

    private boolean shouldGrow() { return _growthQueue.shouldGrow(); }
    private void consumeGrowth() { _growthQueue.consumeGrowth(); }

    private boolean canPlaceExpansion() {
        SnakeSegment head = getHead();
        if (head == null) return false;
        Cell headCell = head.getPos();
        if (headCell == null) return false;
        Direction dir = getDirection();
        if (dir == null) return false;
        Cell leftCell = getAdjacentCell(headCell, dir, true);
        Cell rightCell = getAdjacentCell(headCell, dir, false);
        if (leftCell == null || rightCell == null) return false;
        for (Unit u : leftCell.getUnits()) {
            if (u instanceof SnakeSegment) return false;
        }
        for (Unit u : rightCell.getUnits()) {
            if (u instanceof SnakeSegment) return false;
        }
        return true;
    }

    private Cell getAdjacentCell(Cell center, Direction dir, boolean left) {
        if (dir.equals(Direction.east()) || dir.equals(Direction.west())) {
            return left ? center.getNeighbor(Direction.north()) : center.getNeighbor(Direction.south());
        }
        return left ? center.getNeighbor(Direction.west()) : center.getNeighbor(Direction.east());
    }

    public boolean tryAddExpansion() {
        if (!canPlaceExpansion()) return false;
        int currentLength = _segments.size();
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
        SnakeSegment tail = _segments.peekLast();
        if (tail == null) return;
        Direction tailDirection = tail.getDirection();
        if (tailDirection == null) return;
        Cell tailCell = tail.getPos();
        Cell newCell = tailCell.getNeighbor(tailDirection.opposite());
        if (!growTail(newCell, tailDirection)) {
            kill();
        }
    }

    private boolean growTail(Cell cell, Direction direction) {
        if (cell == null || direction == null) return false;
        for (Unit u : cell.getUnits()) {
            if (u instanceof SnakeSegment) return false;
        }
        SnakeSegment newSegment = new SnakeSegment(false, 1.0f, null);
        newSegment.setDirection(direction);
        if (!cell.putUnit(newSegment)) return false;
        _segments.addLast(newSegment);
        return true;
    }

    private void updateDirections() {
        for (int i = 1; i < _segments.size(); i++) {
            SnakeSegment prev = _segments.get(i - 1);
            SnakeSegment curr = _segments.get(i);
            Direction dir = curr.getPos().getDirectionTo(prev.getPos());
            if (dir != null) curr.setDirection(dir);
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

    private boolean canAdvanceTo(Cell target) {
        if (_segments.isEmpty()) {
            _hunger.kill();
            return false;
        }
        if (target == null) {
            _hunger.kill();
            return false;
        }
        return true;
    }

    private void completeMove() {
        if (shouldGrow()) {
            consumeGrowth();
        } else {
            removeTailSegment();
        }
        applyHungerEffects();
        updateExpansions();
    }
}