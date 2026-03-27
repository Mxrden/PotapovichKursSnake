package Model.Snake;

import Model.Effects.RodentEffect;
import Model.Effects.RodentEffectEnum;
import Model.GameField.Cell;
import Model.GameField.Direction;
import Model.Units.Rodent;
import Model.Units.Stone;
import Model.Units.Unit;

import java.util.ArrayList;
import java.util.List;

public class Snake {

    // -----------------------------
    // Поля состояния
    // -----------------------------

    private final List<SnakePart> _parts = new ArrayList<>();
    private int _life;
    private final int _minLenght;
    private int _growthQueue;
    private final List<RodentEffect> _effects = new ArrayList<>();


    // -----------------------------
    // Конструктор
    // -----------------------------

    public Snake(int minLenght) {
        _minLenght = minLenght;
        _growthQueue = 0;
        _life = 3;
    }


    // -----------------------------
    // Доступ к частям тела
    // -----------------------------

    public SnakePart getHead() {
        return _parts.getFirst();
    }

    public SnakePart getTail() {
        return _parts.getLast();
    }

    public List<SnakePart> getParts() {
        return _parts;
    }

    public void joinNewPart(SnakePart part) {
        _parts.add(part);
    }


    // -----------------------------
    // Перемещение
    // -----------------------------

    private boolean moveTo(Cell target) {
        if (isDead()) return false;

        Unit unit = target.getUnit();

        // столкновение с телом змеи
        if (unit != null && _parts.contains(unit)) {
            if (unit != getTail()) {
                kill();
                return false;
            }
        }

        // реакция юнита
        if (unit != null) {
            unit.onSteppedBy(this);
            if (isDead()) return false;
        }

        moveBody(target);
        return true;
    }



    public boolean move(Direction direction) {

        if (direction == getHead().getDirection().opposite()) return false;

        Cell headCell = getHead().getPos();
        Cell target = headCell.getNeighbor(direction);

        if (headCell.getWall(direction) != null) {
            kill();
            return false;
        }

        if (!moveTo(target) || isDead()) return false;

        getHead().setDirection(direction);

        return true;
    }


    // -----------------------------
    // Логика перемещения тела
    // -----------------------------

    private void moveBody(Cell newHeadCell) {

        List<Cell> oldPositions = new ArrayList<>();
        for (SnakePart part : _parts) {
            oldPositions.add(part.getPos());
        }

        SnakePart head = getHead();
        head.moveTo(newHeadCell);

        for (int i = 1; i < _parts.size(); i++) {
            SnakePart part = _parts.get(i);
            part.moveTo(oldPositions.get(i - 1));
            part.setDirection(_parts.get(i - 1).getDirection());
        }

        if (_growthQueue > 0) {
            decreaseGrowthQueue();
        } else {
            shrink();
        }
    }


    // -----------------------------
    // Уменьшение (удаление хвоста)
    // -----------------------------

    public void shrink() {
        SnakePart tail = getTail();
        tail.getPos().extractUnit();
        _parts.removeLast();
    }


    // -----------------------------
    // Жизнь
    // -----------------------------

    public void increaseLife() {
        _life++;
    }

    public void decreaseLife() {
        _life--;
    }

    public boolean isDead() {
        return _life <= 0;
    }

    public void kill() {
        _life = 0;
    }


    // -----------------------------
    // Очередь роста
    // -----------------------------

    public int getGrowthQueue() {
        return _growthQueue;
    }

    public void increaseGrowthQueue() {
        _growthQueue++;
    }

    public void decreaseGrowthQueue() {
        _growthQueue--;
    }


    // -----------------------------
    // Эффекты
    // -----------------------------

    public boolean putEffect(RodentEffect effect) {
        if (effect.getType() == RodentEffectEnum.NO_EFFECT) return false;
        _effects.add(effect);
        return true;
    }
}
