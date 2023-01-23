package dev.kyzel.game.entity.animal;

import java.awt.Rectangle;
import java.util.ArrayList;

import dev.kyzel.game.Game;
import dev.kyzel.game.entity.Entity;
import dev.kyzel.game.entity.Player;
import dev.kyzel.gfx.Renderer;
import dev.kyzel.utils.AssetManager;

public class Ghost extends Animal {

    public Ghost(Renderer render, Game game, int x, int y, int speed) {
        super(render, game, x, y, speed);
        maxHealthValue = 2;
        currentHealthValue = maxHealthValue;
        attackValue = 0;
        isCursed = true;
        
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
        ArrayList<Entity> entityList = game.getEntityList();
        for(Entity entity : entityList) {
            if(entity instanceof Ghost) continue;
            int entityX = entity.getX();
            int entityY = entity.getY();

            if(entity instanceof Player) {
                entityX -= game.getSceneX();
                entityY -= game.getSceneY();
            }

            if(Math.abs(entityX - x) <= render.getUnitSize()/2 &&
                    Math.abs(entityY - y) <= render.getUnitSize()/2) {
                entity.cursed();
            }
        }
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
    public int getPoint() {
        return 0;
    }
}
