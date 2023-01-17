package dev.kyzel.game.entity.animal;

import java.awt.Rectangle;

import dev.kyzel.game.Game;
import dev.kyzel.gfx.Renderer;
import dev.kyzel.utils.AssetManager;

public class Ghost extends Animal {

    public Ghost(Renderer render, Game game, int x, int y, int speed) {
        super(render, game, x, y, speed);
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
    public void setCurrentAnimalImage() {
        if(spriteCounter > 12) {
            imageIndex = increaseImageIndex(imageIndex, 2);
            animalImage = AssetManager.ghostImage[imageIndex];
            spriteCounter = 0;
        }
        spriteCounter++;
    }

    @Override
    public int getScore() {
        return 0;
    }
}
