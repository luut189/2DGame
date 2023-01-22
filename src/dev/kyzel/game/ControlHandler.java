package dev.kyzel.game;

import dev.kyzel.utils.Control;
import dev.kyzel.utils.Keyboard;

public class ControlHandler {

    public static Control UP = new Control(Keyboard.KEY_W);
    public static Control DOWN = new Control(Keyboard.KEY_S);
    public static Control RIGHT = new Control(Keyboard.KEY_D);
    public static Control LEFT = new Control(Keyboard.KEY_A);
    public static Control ATTACK = new Control(Keyboard.KEY_SPACE);

    public static Control PAUSE = new Control(Keyboard.KEY_ESCAPE);
    public static Control TOGGLE_FULLSCREEN = new Control(Keyboard.KEY_F);

    public static Control TOGGLE_HUD = new Control(Keyboard.KEY_H);
    public static Control TOGGLE_MINIMAP = new Control(Keyboard.KEY_M);
    public static Control ZOOM_IN_MINIMAP = new Control(Keyboard.KEY_E);
    public static Control ZOOM_OUT_MINIMAP = new Control(Keyboard.KEY_Q);
    
}