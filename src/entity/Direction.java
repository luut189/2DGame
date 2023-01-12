package entity;

public enum Direction {
    NONE,
    UP,
    DOWN,
    RIGHT,
    LEFT;

    public static Direction getOppositeDirection(Direction dir) {
        return switch(dir) {
            case UP -> DOWN;
            case DOWN -> UP;
            case RIGHT -> LEFT;
            case LEFT -> RIGHT;
            default -> NONE;
        };
    }
}