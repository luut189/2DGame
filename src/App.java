import gfx.GUI;

import utils.AssetManager;
import utils.TextureLoader;

public class App {
    public static void main(String[] args) throws Exception {
        AssetManager.loadTileImage(new TextureLoader());
        new GUI(800, 600, 60);
    }
}
