package entity.animal;

import gfx.Renderer;
import utils.AssetManager;

public class Ghost extends Animal {

    public Ghost(Renderer render, int x, int y, int speed) {
        super(render, x, y, speed);
    }

    @Override
    public void initTexture() {
        animalImage = AssetManager.ghostImage[imageIndex];
    }
    @Override
    public void update() {
        super.update();
        setCurrentGhostImage();
    }

    public void setCurrentGhostImage() {
        if(spriteCounter > 12) {
            imageIndex = increaseImageIndex(imageIndex, 2);
            animalImage = AssetManager.ghostImage[imageIndex];
            spriteCounter = 0;
        }
        spriteCounter++;
    }
}
