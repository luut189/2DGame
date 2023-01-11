package entity.animal;

import java.awt.Rectangle;

import gfx.Renderer;
import utils.AssetManager;

public class Ghost extends Animal {

    public Ghost(Renderer render, int x, int y, int speed) {
        super(render, x, y, speed);
        setMaxHealthValue(2);
        setHealthValue(2);
        setAttackValue(0);
        
        this.solidArea = new Rectangle(0, 0, 0, 0);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
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
