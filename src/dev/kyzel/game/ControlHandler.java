package dev.kyzel.game;

import dev.kyzel.utils.Control;
import dev.kyzel.utils.Keyboard;

/**
 * All possible inputs/keybindings for the game.
 */
public class ControlHandler {

    /**
     * Keybinding for moving the player up.
     */
    public static Control UP = new Control(Keyboard.KEY_W);

    /**
     * Keybinding for moving the player down.
     */
    public static Control DOWN = new Control(Keyboard.KEY_S);

    /**
     * Keybinding for moving the player right.
     */
    public static Control RIGHT = new Control(Keyboard.KEY_D);

    /**
     * Keybinding for moving the player left.
     */
    public static Control LEFT = new Control(Keyboard.KEY_A);

    /**
     * Keybinding for the player to attack.
     */
    public static Control ATTACK = new Control(Keyboard.KEY_SPACE);

    /**
     * Keybinding for showing player's stats.
     */
    public static Control SHOW_STAT = new Control(Keyboard.KEY_E);

    /**
     * Keybinding for pausing the game.
     */
    public static Control PAUSE = new Control(Keyboard.KEY_ESCAPE);

    /**
     * Keybinding for toggling fullscreen.
     */
    public static Control TOGGLE_FULLSCREEN = new Control(Keyboard.KEY_F);

    /**
     * Keybinding for toggling the HUD.
     */
    public static Control TOGGLE_HUD = new Control(Keyboard.KEY_H);

    /**
     * Keybinding for toggling the minimap.
     */
    public static Control TOGGLE_MINIMAP = new Control(Keyboard.KEY_M);

    /**
     * Keybinding for zooming in the minimap.
     */
    public static Control ZOOM_IN_MINIMAP = new Control(Keyboard.KEY_Z);

    /**
     * Keybinding for zooming out the minimap.
     */
    public static Control ZOOM_OUT_MINIMAP = new Control(Keyboard.KEY_X);
    
}