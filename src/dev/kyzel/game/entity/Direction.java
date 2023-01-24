package dev.kyzel.game.entity;

/**
 * All possible directions that Entity can move in.
 */
public enum Direction {

    /**
     * No direction is used.
     */
    NONE,

    /**
     * Going up.
     */
    UP,

    /**
     * Going down.
     */
    DOWN,
    
    /**
     * Going right.
     */
    RIGHT,

    /**
     * Going left.
     */
    LEFT;

    /**
     * Gets the opposite direction of the given direction.
     * 
     * @param dir the given direction
     * @return the opposite direction
     */
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