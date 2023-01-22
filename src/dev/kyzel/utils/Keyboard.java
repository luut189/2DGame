package dev.kyzel.utils;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard {

    public static class Key {
        public boolean down, pressed, last;
    }

    public static final int KEY_ESCAPE = KeyEvent.VK_ESCAPE;

    public static final int KEY_SPACE = KeyEvent.VK_SPACE;
    
    public static final int KEY_A = KeyEvent.VK_A;

    public static final int KEY_B = KeyEvent.VK_B;

    public static final int KEY_C = KeyEvent.VK_C;

    public static final int KEY_D = KeyEvent.VK_D;

    public static final int KEY_E = KeyEvent.VK_E;

    public static final int KEY_F = KeyEvent.VK_F;

    public static final int KEY_G = KeyEvent.VK_G;

    public static final int KEY_H = KeyEvent.VK_H;

    public static final int KEY_I = KeyEvent.VK_I;

    public static final int KEY_J = KeyEvent.VK_J;

    public static final int KEY_K = KeyEvent.VK_K;

    public static final int KEY_L = KeyEvent.VK_L;

    public static final int KEY_M = KeyEvent.VK_M;

    public static final int KEY_N = KeyEvent.VK_N;

    public static final int KEY_O = KeyEvent.VK_O;

    public static final int KEY_P = KeyEvent.VK_P;

    public static final int KEY_Q = KeyEvent.VK_Q;

    public static final int KEY_R = KeyEvent.VK_R;

    public static final int KEY_S = KeyEvent.VK_S;

    public static final int KEY_T = KeyEvent.VK_T;

    public static final int KEY_U = KeyEvent.VK_U;

    public static final int KEY_V = KeyEvent.VK_V;

    public static final int KEY_W = KeyEvent.VK_W;

    public static final int KEY_X = KeyEvent.VK_X;

    public static final int KEY_Y = KeyEvent.VK_Y;

    public static final int KEY_Z = KeyEvent.VK_Z;

    public static final Key[] keys = new Key[128];

    static {
        for(int i = 0; i < keys.length; i++) {
            keys[i] = new Key();
        }
    }

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

    public static void update() {
        for(Key key : keys) {
            key.pressed = key.down && !key.last;
            key.last = key.down;
        }
    }

}