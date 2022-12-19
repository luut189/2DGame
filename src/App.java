import gfx.GUI;

public class App {
    public static void main(String[] args) throws Exception {

        /**
         * use the method below to set the seed for world generation
         * if not set by user, a random value will be used
         */
        // GUI.setSeedValue(12);
        
        new GUI(800, 600, 60);
    }
}
