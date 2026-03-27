package Model.GameField;

import Model.Snake.Snake;

public class Wall {

    private final boolean _isWall;

    public Wall(boolean isWall) {
        _isWall = isWall;
    }

    public boolean isWall() {
        return _isWall;
    }

}
