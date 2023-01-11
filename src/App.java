import gfx.GUI;
import world.gen.LevelGenerator;

public class App {
    public static void main(String[] args) throws Exception {

        /**
         * use the method below to set the seed for world generation
         * if not set by user, a random value will be used
         * decimal value works better
         * value in the same range (eg. 0.2 - 0.8) will generate almost identical world
         */
        LevelGenerator.setSeedValue(3.6);
        
        GUI.init(1280, 720, 60);
    }
}
