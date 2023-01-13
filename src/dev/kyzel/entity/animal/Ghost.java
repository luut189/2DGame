package dev.kyzel.entity.animal;

import java.awt.Rectangle;

import dev.kyzel.gfx.Renderer;
import dev.kyzel.utils.AssetManager;

public class Ghost extends Animal {

    public Ghost(Renderer render, int x, int y, int speed) {
        super(render, x, y, speed);
        maxHealthValue = 2;
        currentHealthValue = maxHealthValue;
        attackValue = 0;
        
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
