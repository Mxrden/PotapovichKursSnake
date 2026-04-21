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

    public void set_rodentCellForExpansion(Cell cell) {
        _rodentCellForExpansion = cell;
    }

    public boolean tryAddExpansion(Cell expansionCell) {
        int currentLength = _body.size();
        if (currentLength <= 0) return false;
        Direction dir = _movement.getDirection();
        Cell headCell = _body.head().getPos();

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
        if (_requestedDirection != null) {
            Direction currentDir = _movement.getDirection();
            if (!currentDir.isOpposite(_requestedDirection)) {
                _movement.setDirection(_requestedDirection);
            }
            _requestedDirection = null;
        }

        _rodentEaten = false;

        if (_body.isEmpty()) {
            _hunger.kill();
            return false;
        }

        Cell headCell = _body.head().getPos();
        SnakeMovement.MoveResult move = _movement.computeMove(headCell, _ignoreNextWall, _ignoreNextStone);

        boolean wallIgnored = (move.obstacle == Unit.Obstacle.WALL_IGNORED);
        boolean stoneIgnored = (move.obstacle == Unit.Obstacle.STONE_IGNORED);
        if (wallIgnored || stoneIgnored) {
            _ignoreNextWall = false;
            _ignoreNextStone = false;
        }

        if (move.target == null) {
            _hunger.kill();
            return false;
        }

        Cell target = move.target;
        boolean grow = _hunger.shouldGrow();

        if (!target.isEmpty()) {
            Unit unit = target.getUnit();
            Unit.UnitType type = (unit != null) ? unit.getType() : null;

            if (type == Unit.UnitType.RODENT) {
                _rodentEaten = true;
                Cell rodentCell = target;
                unit.onSteppedBy(this);
                _rodentCellForExpansion = rodentCell;
            } else if (wallIgnored || stoneIgnored) {
                target.extractUnit();
            } else {
                unit.onSteppedBy(this);
                if (_hunger.isDead()) return false;
            }
        }

        boolean success = _body.addNewHead(target, _movement.getDirection());
        if (!success) {
            _hunger.kill();
            return false;
        }

        if (!grow) {
            Cell tailCell = _body.tail().getPos();
            if (tailCell != null && tailCell.getUnit() == _body.tail()) {
                tailCell.extractUnit();
            }
            _body.removeTail();
        } else {
            _hunger.consumeGrowth();
        }

        boolean needShrink = _hunger.applyHunger(_body.size());
        if (needShrink) {
            Cell tailCell = _body.tail().getPos();
            if (tailCell != null && tailCell.getUnit() == _body.tail()) {
                tailCell.extractUnit();
            }
            _body.removeTail();
        }

        updateExpansions();
        return !_hunger.isDead();
    }
}