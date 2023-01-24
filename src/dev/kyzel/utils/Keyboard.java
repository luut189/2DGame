package dev.kyzel.utils;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * A class to handle keyboard input.
 */
public class Keyboard {

    /**
     * The class that holds properties of a key.
     */
    public static class Key {
        public boolean down, pressed, last;
    }

    /**
     * Constant to represent the Escape key.
     */
    public static final int KEY_ESCAPE = KeyEvent.VK_ESCAPE;

    /**
     * Constant to represent the Space key.
     */
    public static final int KEY_SPACE = KeyEvent.VK_SPACE;
    
    /**
     * Constant to represent the A key.
     */
    public static final int KEY_A = KeyEvent.VK_A;

    /**
     * Constant to represent the B key.
     */
    public static final int KEY_B = KeyEvent.VK_B;

    /**
     * Constant to represent the C key.
     */
    public static final int KEY_C = KeyEvent.VK_C;

    /**
     * Constant to represent the D key.
     */
    public static final int KEY_D = KeyEvent.VK_D;

    /**
     * Constant to represent the E key.
     */
    public static final int KEY_E = KeyEvent.VK_E;

    /**
     * Constant to represent the F key.
     */
    public static final int KEY_F = KeyEvent.VK_F;

    /**
     * Constant to represent the G key.
     */
    public static final int KEY_G = KeyEvent.VK_G;

    /**
     * Constant to represent the H key.
     */
    public static final int KEY_H = KeyEvent.VK_H;

    /**
     * Constant to represent the I key.
     */
    public static final int KEY_I = KeyEvent.VK_I;

    /**
     * Constant to represent the J key.
     */
    public static final int KEY_J = KeyEvent.VK_J;

    /**
     * Constant to represent the K key.
     */
    public static final int KEY_K = KeyEvent.VK_K;

    /**
     * Constant to represent the L key.
     */
    public static final int KEY_L = KeyEvent.VK_L;

    /**
     * Constant to represent the M key.
     */
    public static final int KEY_M = KeyEvent.VK_M;

    /**
     * Constant to represent the N key.
     */
    public static final int KEY_N = KeyEvent.VK_N;

    /**
     * Constant to represent the O key.
     */
    public static final int KEY_O = KeyEvent.VK_O;

    /**
     * Constant to represent the P key.
     */
    public static final int KEY_P = KeyEvent.VK_P;

    /**
     * Constant to represent the Q key.
     */
    public static final int KEY_Q = KeyEvent.VK_Q;

    /**
     * Constant to represent the R key.
     */
    public static final int KEY_R = KeyEvent.VK_R;

    /**
     * Constant to represent the S key.
     */
    public static final int KEY_S = KeyEvent.VK_S;

    /**
     * Constant to represent the T key.
     */
    public static final int KEY_T = KeyEvent.VK_T;

    /**
     * Constant to represent the U key.
     */
    public static final int KEY_U = KeyEvent.VK_U;

    /**
     * Constant to represent the V key.
     */
    public static final int KEY_V = KeyEvent.VK_V;

    /**
     * Constant to represent the W key.
     */
    public static final int KEY_W = KeyEvent.VK_W;

    /**
     * Constant to represent the X key.
     */
    public static final int KEY_X = KeyEvent.VK_X;

    /**
     * Constant to represent the Y key.
     */
    public static final int KEY_Y = KeyEvent.VK_Y;

    /**
     * Constant to represent the Z key.
     */
    public static final int KEY_Z = KeyEvent.VK_Z;
    
    /**
     * A list of 128 keys.
     * Would not be a magic number, but it works.
     */
    public static final Key[] keys = new Key[128];

    /**
     * Initializes the list of 128 keys.
     */
    static {
        for(int i = 0; i < keys.length; i++) {
            keys[i] = new Key();
        }
    }

    /**
     * Gets the KeyListener of this Keyboard class.
     * 
     * @return the key listener
     */
    public static KeyListener getListener() {
        return new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                Keyboard.keys[e.getKeyCode()].down = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                Keyboard.keys[e.getKeyCode()].down = false;
            }
            
        };
    }

    /**
     * Updates the states of each key.
     */
    public static void update() {
        for(Key key : keys) {
            key.pressed = key.down && !key.last;
            key.last = key.down;
        }
    }

}