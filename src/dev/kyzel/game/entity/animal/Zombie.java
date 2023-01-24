package dev.kyzel.game.entity.animal;

import java.awt.Rectangle;

import dev.kyzel.game.Game;
import dev.kyzel.game.entity.Direction;
import dev.kyzel.game.entity.EntityState;
import dev.kyzel.gfx.Renderer;
import dev.kyzel.utils.AssetManager;

/**
 * The zombie class.
 */
public class Zombie extends Animal {

    /**
     * A variable to help with animation.
     */
    private boolean isLeftLeg = false;

    /**
     * Creates a new Zombie.
     * 
     * @param render the {@link Renderer} where the zombie will be drawn on
     * @param game the {@link Game} where the zombie interacts
     * @param x the x coordinate
     * @param y the y coordinate
     * @param speed the speed of the zombie
     */
    public Zombie(Renderer render, Game game, int x, int y, int speed) {
        super(render, game, x, y, speed);
        hostileProb = 1;
        maxHealthValue = 5;
        currentHealthValue = maxHealthValue;
        attackValue = 2;

        int offsetX = render.getUnitSize()/4;
        int offsetY = render.getUnitSize()/4;

        int rectWidth = render.getUnitSize()-offsetX*2;
        int rectHeight = render.getUnitSize()-offsetY;
        
        this.solidArea = new Rectangle(offsetX, offsetY, rectWidth, rectHeight);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    @Override
    public void initTexture() {
        animalImage = AssetManager.zombieDownImage[imageIndex];
    }

    @Override
    public void setCurrentAnimalImage() {
        spriteCounter++;
        if(state == EntityState.STANDING) {
            imageIndex = 0;
        } else if(state == EntityState.WALKING || state == EntityState.ATTACKING) {
            boolean isSideWay = direction == Direction.RIGHT || direction == Direction.LEFT;
            if(spriteCounter > (isSideWay ? 10 : 12)) {
                isLeftLeg = !isLeftLeg;
                if(isSideWay) {
                    imageIndex = increaseImageIndex(imageIndex, 4);
                }
                spriteCounter = 0;
            }
            if(!isSideWay) {
                imageIndex = isLeftLeg ? 1 : 2;
            }
        }
        switch(direction) {
            case UP -> animalImage = AssetManager.zombieUpImage[imageIndex];
            case DOWN -> animalImage = AssetManager.zombieDownImage[imageIndex];
            case RIGHT -> animalImage = AssetManager.zombieRightImage[imageIndex];
            case LEFT -> animalImage = AssetManager.zombieLeftImage[imageIndex];
            default -> {}
        }
    }

    @Override
    public int getPoint() {
        return 2;
    }
}
