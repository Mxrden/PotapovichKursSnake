package Model.Snake;

import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.Units.Rodent;
import Model.Units.Unit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Snake {

    private final SnakeBody body = new SnakeBody();
    private final SnakeMovement movement = new SnakeMovement();
    private final SnakeHunger hunger;

    // Одноразовые эффекты
    private boolean ignoreNextWall = false;
    private boolean ignoreNextStone = false;

    // Флаг, был ли съеден грызун на этом ходу (для уведомлений)
    private boolean rodentEaten = false;

    // Список активных расширений (временные боковые сегменты)
    private final List<TemporaryExpansion> expansions = new ArrayList<>();

    // Буферизированное направление, запрошенное игроком между тиками
    private Direction requestedDirection = null;

    public Snake(int minLength, int initialLife,
                 int shrinkInterval, int hpLossInterval, int hungerDamage) {
        this.hunger = new SnakeHunger(minLength, initialLife,
                shrinkInterval, hpLossInterval, hungerDamage);
    }

    // -----------------------------
    // Управление эффектами
    // -----------------------------
    public void activateIgnoreWall() {
        ignoreNextWall = true;
    }

    public void activateIgnoreStone() {
        ignoreNextStone = true;
    }

    // -----------------------------
    // Направление – теперь только запоминаем запрос
    // -----------------------------
    public void setDirection(Direction dir) {
        if (dir == null) return;
        requestedDirection = dir;
    }

    public Direction getDirection() {
        return movement.getDirection();
    }

    // -----------------------------
    // Доступ к телу
    // -----------------------------
    public SnakeBody getBody() {
        return body;
    }

    public List<SnakeSegment> getSegments() {
        return body.all();
    }

    public SnakeSegment getHead() {
        return body.head();
    }

    // -----------------------------
    // Состояние
    // -----------------------------
    public boolean isDead() {
        return hunger.isDead();
    }

    public void kill() {
        hunger.kill();
        // Удаляем все расширения при смерти змеи
        for (TemporaryExpansion exp : expansions) {
            exp.dispose();
        }
        expansions.clear();
    }

    // -----------------------------
    // Рост (старый механизм – немедленный)
    // -----------------------------
    public void increaseGrowthQueue() {
        hunger.addGrowth();
    }

    // -----------------------------
    // Новый механизм роста через расширение
    // -----------------------------
    public boolean tryAddExpansion() {
        int currentLength = body.size();
        if (currentLength <= 0) return false;
        Cell headPos = body.head().getPos();
        Direction dir = movement.getDirection();
        try {
            TemporaryExpansion exp = new TemporaryExpansion(this, headPos, dir, currentLength);
            expansions.add(exp);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    private void updateExpansions() {
        Iterator<TemporaryExpansion> it = expansions.iterator();
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

    public boolean wasRodentEaten() {
        return rodentEaten;
    }

    // -----------------------------
    // Основной шаг движения
    // -----------------------------
    public boolean move() {
        // Применяем буферизированное направление, если оно допустимо
        if (requestedDirection != null) {
            Direction currentDir = movement.getDirection();
            if (!currentDir.isOpposite(requestedDirection)) {
                movement.setDirection(requestedDirection);
            }
            requestedDirection = null; // сбрасываем запрос
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

        switch (move.obstacle) {
            case BOUNDARY:
            case WALL:
            case STONE:
                hunger.kill();
                return false;
            default:
                break;
        }

        Cell target = move.target;

        // Запрещаем наступать на любой сегмент змеи (включая хвост)
        if (target != null && target.getUnit() instanceof SnakeSegment) {
            hunger.kill();
            return false;
        }

        boolean grow = hunger.shouldGrow();

        if (target != null && !target.isEmpty()) {
            Unit unit = target.getUnit();
            if (unit instanceof Rodent) {
                rodentEaten = true;
            }
            if (wallIgnored || stoneIgnored) {
                target.extractUnit();
            } else {
                unit.onSteppedBy(this);
                if (hunger.isDead()) return false;
            }
        }

        boolean success = body.shiftTo(target, grow, movement.getDirection());
        if (!success) {
            hunger.kill();
            return false;
        }

        if (grow) {
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