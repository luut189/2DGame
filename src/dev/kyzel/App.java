package dev.kyzel;

import dev.kyzel.gfx.Window;
import dev.kyzel.game.world.gen.LevelGenerator;

/**
 * The main class.
 */
public class App {

    /**
     * The main method.
     * 
     * @param args ARGS!!
     */
    public static void main(String[] args) {

        /*
        Use the method below to set the seed for world generation.
        If it wasn't set by the user, a random value will be used.
        A decimal value works better with this.
        Values that are in the same range (e.g. 0.2 and 0.8) will generate almost identical world.
         */
        LevelGenerator.setSeedValue(3.6);
        
        Window.init(1280, 720, 60);
    }
}
