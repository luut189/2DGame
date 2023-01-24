package dev.kyzel.utils;

/**
 * A class to handle user's input.
 */
public class Control {

    private int[] keys;

    /**
     * Creates a new keybinding for some action.
     * 
     * @param keys the arbitrary list of input keys
     */
    public Control(int... keys) {
        this.keys = keys;
    }

    /**
     * Checks if any of the {@link #keys} is pressed down.
     * 
     * @return if any of the {@link #keys} is pressed down
     */
    public boolean down() {
        for(int key : keys) {
            if(Keyboard.keys[key].down) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if any of the {@link #keys} is pressed and released.
     * 
     * @return if any of the {@link #keys} is pressed and released
     */
    public boolean pressed() {
        for(int key : keys) {
            if(Keyboard.keys[key].pressed) {
                return true;
            }
        }
        return false;
    }
}