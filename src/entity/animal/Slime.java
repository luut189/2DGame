package entity.animal;

import entity.EntityState;
import gfx.Renderer;
import utils.AssetManager;
import world.tile.TileManager;

public class Slime extends Animal {

    public Slime(Renderer render, int x, int y, int speed) {
        super(render, x, y, speed);
    }

    @Override
    public void initTexture() {
        animalImage = AssetManager.slimeImage[imageIndex];
    }

    @Override
    public void update(TileManager tileManager) {
        super.update(tileManager);
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
