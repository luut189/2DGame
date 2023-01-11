package entity;

public enum Direction {
    NONE,
    UP,
    DOWN,
    RIGHT,
    LEFT;

    public static Direction getOppositeDirection(Direction dir) {
        switch(dir) {
            case UP: return DOWN;
            case DOWN: return UP;
            case RIGHT: return LEFT;
            case LEFT: return RIGHT;
            default: return NONE;
        }
    }
}