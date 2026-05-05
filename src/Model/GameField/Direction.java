package Model.GameField;

public class Direction {

    private final int _hours;

    private Direction(int hours) {
        hours = hours % 12;
        if (hours < 0) hours += 12;
        _hours = hours;
    }

    private static final Direction NORTH = new Direction(0);
    private static final Direction EAST  = new Direction(3);
    private static final Direction SOUTH = new Direction(6);
    private static final Direction WEST  = new Direction(9);

    public static Direction north() { return NORTH; }
    public static Direction east()  { return EAST; }
    public static Direction south() { return SOUTH; }
    public static Direction west()  { return WEST; }

    public Direction clockwise() {
        return new Direction(_hours + 3);
    }

    public Direction anticlockwise() {
        return new Direction(_hours - 3);
    }

    public Direction opposite() {
        return new Direction(_hours + 6);
    }

    public Direction onRight() {
        return clockwise();
    }

    public Direction onLeft() {
        return anticlockwise();
    }


    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (!(other instanceof Direction)) return false;
        Direction direct = (Direction) other;
        return _hours == direct._hours;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(_hours);
    }

    @Override
    public String toString() {
        switch (_hours) {
            case 0:  return "N";
            case 3:  return "E";
            case 6:  return "S";
            case 9:  return "W";
        }
        return "?";
    }

    public static Direction[] all() {
        return new Direction[] {
                NORTH,
                EAST,
                SOUTH,
                WEST
        };
    }

    public boolean isOpposite(Direction other) {
        return opposite().equals(other);
    }

}
