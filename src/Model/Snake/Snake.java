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
    private List<SnakePart> _parts = new ArrayList<>();

    private int _life;

    private final int minLenght;

    private final int growthQueue;

    private Direction _direction;

    private List<RodentEffect> _effects = new ArrayList<>();

    public Snake(int minLenght, int growthQueue) {
        this.minLenght = minLenght;
        this.growthQueue = growthQueue;
    }

    public SnakePart getHead() {
        return _parts.getFirst();
    }

    public List<SnakePart> getParts() {
        return _parts;
    }

    private boolean moveTo(Cell target) {
        Unit targetUnit = target.getUnit();
        if (targetUnit instanceof Stone) return false;
        if(targetUnit instanceof Rodent) {
            Rodent rodent = (Rodent) targetUnit;
            RodentEffect effect = rodent.getEffect();
            putEffect(effect);
            rodent.onEaten();
            grow();

        }
        //TODO: перемещение тела змеи
        return true;
    }

    public boolean move(Direction direction) {
        if (isDead()) return false;
        Cell target = getHead().getPos().getNeighbor(direction);
        if (!moveTo(target)) return false;
        return true;
    }

    public void changeDirection (Direction newDir) {
        _direction = newDir;
    }

    public void grow() {
        //TODO
    }

    public void shrink() {
        //TODO
    }

    public void decreaseLife() {
        _life--;
    }

    public boolean isDead() {
        return _life<=0;
    }

    public boolean putEffect(RodentEffect effect) {
        if (effect.getType() == RodentEffectEnum.NO_EFFECT) return false;
        _effects.add(effect);
        return true;
    }

}
