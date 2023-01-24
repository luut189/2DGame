package dev.kyzel.game.entity.animal;

import java.awt.Rectangle;

import dev.kyzel.game.Game;
import dev.kyzel.game.entity.EntityState;
import dev.kyzel.gfx.Renderer;
import dev.kyzel.utils.AssetManager;

/**
 * The slime class.
 */
public class Slime extends Animal {

    /**
     * Creates a new Slime.
     * 
     * @param render the {@link Renderer} where the slime will be drawn on
     * @param game the {@link Game} where the slime interacts
     * @param x the x coordinate
     * @param y the y coordinate
     * @param speed the speed of the slime
     */
    public Slime(Renderer render, Game game, int x, int y, int speed) {
        super(render, game, x, y, speed);
        hostileProb = 0.4;
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
    public void setCurrentAnimalImage() {
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

    @Override
    public int getPoint() {
        return 1;
    }
    
}
