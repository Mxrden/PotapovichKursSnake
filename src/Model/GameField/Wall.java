package Model.GameField;

import Model.Units.Snake;

public class Wall {

    private final boolean _isWall;

    public Wall(boolean isWall) {
        _isWall = isWall;
    }

    public boolean isWall() {
        return _isWall;
    }

    public boolean isPassableFor(Snake snake) {
        if (!_isWall) return true;

        return snake.canPassWalls();
    }
}
