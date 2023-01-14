package dev.kyzel.game.entity.animal;

import java.awt.Rectangle;

import dev.kyzel.game.Game;
import dev.kyzel.game.entity.EntityState;
import dev.kyzel.gfx.Renderer;
import dev.kyzel.utils.AssetManager;

public class Slime extends Animal {

    public Slime(Renderer render, Game game, int x, int y, int speed) {
        super(render, game, x, y, speed);
        hostileProb = 0.01;
        maxHealthValue = 3;
        currentHealthValue = maxHealthValue;
        attackValue = 1;

        int offsetX = 0;
        int offsetY = render.getUnitSize()/3;

        int rectWidth = render.getUnitSize();
        int rectHeight = render.getUnitSize() - render.getUnitSize()/3;
        
        this.solidArea = new Rectangle(offsetX, offsetY, rectWidth, rectHeight);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    @Override
    public void initTexture() {
        animalImage = AssetManager.slimeImage[imageIndex];
    }

    @Override
    public void update() {
        super.update();
        setCurrentSlimeImage();
    }

    public void setCurrentSlimeImage() {
        if(state == EntityState.STANDING) {
            animalImage = AssetManager.slimeImage[0];
            return;
        }
        if(spriteCounter > 12) {
            imageIndex = increaseImageIndex(imageIndex, 2);
            animalImage = AssetManager.slimeImage[imageIndex];
            spriteCounter = 0;
        }
        spriteCounter++;
    }
    
}
