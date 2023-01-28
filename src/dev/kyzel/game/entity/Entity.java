package dev.kyzel.game.entity;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import dev.kyzel.game.Game;
import dev.kyzel.game.entity.animal.Animal;
import dev.kyzel.game.entity.animal.Ghost;
import dev.kyzel.game.world.tile.Tile;
import dev.kyzel.game.world.tile.TileManager;
import dev.kyzel.gfx.Renderer;

/**
 * A class to handle Entity in the game.
 */
public abstract class Entity implements IAttackable {

    /**
     * Constant for the max value of healing counter.
     */
    public static final int maxHealingCounter = 300;

    /**
     * Constant for the max value of hit cooldown.
     */
    public static final int maxHitTick = 20;

    /**
     * Constant for the max value of knockback counter.
     */
    public static final int maxKnockbackCounter = 20;
    
    /**
     * Constant for the max value of invincible counter.
     */
    public static final int maxInvincibleCounter = 20;

    /**
     * The {@link Renderer} where the entity will be drawn on.
     */
    protected Renderer render;

    /**
     * The {@link Game} where the entity interacts.
     */
    protected Game game;

    /**
     * The {@link Direction} the entity moves in.
     */
    protected Direction direction;

    /**
     * The last {@link Direction} of the entity.
     */
    protected Direction previousDirection = Direction.DOWN;

    /**
     * The {@link EntityState} of the entity.
     */
    protected EntityState state;

    /**
     * The x coordinate of the entity.
     */
    protected int x;

    /**
     * The y coordinate of the entity.
     */
    protected int y;

    /**
     * The max health value of the entity.
     */
    protected int maxHealthValue;

    /**
     * The current health value of the entity.
     */
    protected int currentHealthValue;
    
    /**
     * The attack damage value of the entity.
     */
    protected int attackValue;
    
    /**
     * The speed of the entity.
     */
    protected int speed;

    /**
     * The based speed of the entity.
     */
    protected int basedSpeed;

    /**
     * A variable to see if the entity is cursed.
     */
    protected boolean isCursed = false;

    /**
     * The hitbox of the entity.
     */
    protected Rectangle solidArea;
    
    /**
     * The default x coordinate of the hitbox.
     */
    protected int solidAreaDefaultX;

    /**
     * The default y coordinate of the hitbox.
     */
    protected int solidAreaDefaultY;

    /**
     * The sprite counter, which is used for the animation
     */
    protected int spriteCounter = 0;

    /**
     * The default healing counter.
     */
    protected int healingCounter = 0;

    /**
     * The default hit cooldown.
     */
    protected int hitTick = maxHitTick;

    /**
     * The default knockback counter;
     */
    protected int knockbackCounter = maxKnockbackCounter;

    /**
     * The default invincible counter.
     */
    protected int invincibleCounter = maxInvincibleCounter;

    /**
     * Creates a new Entity.
     * Construction of a new, plain Entity is not allowed.
     * Construction has to be explicitly typed (e.g. Player, Slime,...).
     * 
     * @param render the {@link Renderer} where the entity will be drawn on
     * @param game the {@link Game} where the entity interacts
     * @param x the x coordinate
     * @param y the y coordinate
     * @param speed the speed of the entity
     */
    public Entity(Renderer render, Game game, int x, int y, int speed) {
        this.render = render;
        this.game = game;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.basedSpeed = speed;

        solidArea = new Rectangle(0, 0, render.getUnitSize(), render.getUnitSize());
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    /**
     * Checks if the entity is inside the map.
     * 
     * @param tileMap the map
     * @param x the x coordinate
     * @param y the y coordinate
     * @return if the entity is inside the map
     */
    public boolean isInRange(Tile[][] tileMap, int x, int y) {
        return (
            x >= 0 && x < tileMap.length &&
            y >= 0 && y < tileMap[x].length
        );
    }

    /**
     * Updates the entity.
     */
    public void update() {
        if(hitTick < maxHitTick) hitTick++;
        invincibleCounter++;
        knockbackCounter++;
        healing();
        knockback();
    }

    /**
     * Initializes the base texture.
     */
    public abstract void initTexture();

    @Override
    public void inflictDamage(Entity target) {
        if(hitTick < maxHitTick) return;
        if(target.getInvincibleCounter() < maxInvincibleCounter) return;
        target.setHealthValue(target.getHealthValue() - attackValue);

        target.setPreviousDirection(target.getDirection());
        target.setDirection(direction);
        target.setKnockbackCounter(0);

        target.setInvincibleCounter(0);
    }

    /**
     * Inflicts knockback on the entity.
     */
    public void knockback() {
        if(knockbackCounter < maxKnockbackCounter) {
            boolean collideWithTile = collideWithTile(game.getTileManager().getWorldTiles());
            int collidedEntity = collideWithEntity(game.getEntityList(), direction);
            boolean collideWithPlayer = !(this instanceof Player) && collideWithPlayer();

            if(collideWithTile || collidedEntity != -1 || collideWithPlayer) return;

            TileManager tileManager = game.getTileManager();
            int knockbackStrength = (int) (Math.random() * 10);
            if(this instanceof Player) {
                switch(direction) {
                    case UP -> {
                        if(y - game.getSceneY() <= 0) break;
                        game.setSceneY(game.getSceneY() + knockbackStrength);
                    }
                    case DOWN -> {
                        if(y - game.getSceneY() > tileManager.getMaxCol() * render.getUnitSize() - render.getUnitSize()) break;
                        game.setSceneY(game.getSceneY() - knockbackStrength);
                    }
                    case RIGHT -> {
                        if(x - game.getSceneX() > tileManager.getMaxRow() * render.getUnitSize() - render.getUnitSize()) break;
                        game.setSceneX(game.getSceneX() - knockbackStrength);
                    }
                    case LEFT -> {
                        if(x - game.getSceneX() <= 0) break;
                        game.setSceneX(game.getSceneX() + knockbackStrength);
                    }
                    default -> {}
                }
            } else {
                switch(direction) {
                    case UP -> {
                        if(y <= 0) break;
                        y -= knockbackStrength;
                    }
                    case DOWN -> {
                        if(y >= tileManager.getMaxCol() * render.getUnitSize() - render.getUnitSize()) break;
                        y += knockbackStrength;
                    }
                    case RIGHT -> {
                        if(x >= tileManager.getMaxRow() * render.getUnitSize() - render.getUnitSize()) break;
                        x += knockbackStrength;
                    }
                    case LEFT -> {
                        if(x <= 0) break;
                        x -= knockbackStrength;
                    }
                    default -> {}
                }
            }
        }
    }

    /**
     * Heals the entity if the requirements are met.
     */
    public void healing() {
        if(currentHealthValue >= maxHealthValue) return;
        if(healingCounter < maxHealingCounter) healingCounter++;
        if(healingCounter == maxHealingCounter) {
            isCursed = false;
            currentHealthValue++;
            healingCounter = 0;
        }
    }

    /**
     * Curses the entity.
     */
    public void cursed() {
        healingCounter = 0;
        if(currentHealthValue <= maxHealthValue/2) return;

        isCursed = true;
        currentHealthValue--;
    }

    /**
     * Checks if the entity is alive.
     * 
     * @return if the entity is alive
     */
    public boolean isAlive() {
        return currentHealthValue > 0;
    }

    /**
     * Checks if the entity is dead.
     * 
     * @return if the entity is dead
     */
    public boolean isDead() {
        return currentHealthValue <= 0;
    }

    /**
     * Checks if the entity collides with any solid tile.
     * 
     * @param tileMap the map
     * @return if the entity collides with any solid tile
     */
    public boolean collideWithTile(Tile[][] tileMap) {
        if(this instanceof Ghost) return false;
        
        int entityLeftX = x + solidArea.x;
        int entityRightX = x + solidArea.x + solidArea.width;

        int entityTopY = y + solidArea.y;
        int entityBottomY = y + solidArea.y + solidArea.height;

        if(this.equals(game.getPlayer())) {
            entityLeftX -= game.getSceneX();
            entityRightX -= game.getSceneX();

            entityTopY -= game.getSceneY();
            entityBottomY -= game.getSceneY();
        }

        int entityLeftCol = entityLeftX/render.getUnitSize();
        int entityRightCol = entityRightX/render.getUnitSize();
        int entityTopRow = entityTopY/render.getUnitSize();
        int entityBottomRow = entityBottomY/render.getUnitSize();

        Tile tile1;
        Tile tile2;
        switch(direction) {
            case UP -> {
                entityTopRow = (entityTopY - speed) / render.getUnitSize();
                if(isInRange(tileMap, entityLeftCol, entityTopRow) && isInRange(tileMap, entityRightCol, entityTopRow)) {
                    tile1 = tileMap[entityLeftCol][entityTopRow];
                    tile2 = tileMap[entityRightCol][entityTopRow];
                } else {
                    return false;
                }
            }
            case DOWN -> {
                entityBottomRow = (entityBottomY + speed) / render.getUnitSize();
                if(isInRange(tileMap, entityLeftCol, entityBottomRow) && isInRange(tileMap, entityRightCol, entityBottomRow)) {
                    tile1 = tileMap[entityLeftCol][entityBottomRow];
                    tile2 = tileMap[entityRightCol][entityBottomRow];
                } else {
                    return false;
                }
            }
            case RIGHT -> {
                entityRightCol = (entityRightX + speed) / render.getUnitSize();
                if(isInRange(tileMap, entityRightCol, entityTopRow) && isInRange(tileMap, entityRightCol, entityBottomRow)) {
                    tile1 = tileMap[entityRightCol][entityTopRow];
                    tile2 = tileMap[entityRightCol][entityBottomRow];
                } else {
                    return false;
                }
            }
            case LEFT -> {
                entityLeftCol = (entityLeftX - speed) / render.getUnitSize();
                if(isInRange(tileMap, entityLeftCol, entityTopRow) && isInRange(tileMap, entityLeftCol, entityBottomRow)) {
                    tile1 = tileMap[entityLeftCol][entityTopRow];
                    tile2 = tileMap[entityLeftCol][entityBottomRow];
                } else {
                    return false;
                }
            }
            default -> {
                return false;
            }
        }
        
        // if the entity is an animal, this method will return true
        if(this instanceof Animal) {
            if(tile1.getTileName().equals("water")) return true;
            if(tile2.getTileName().equals("water")) return true;
        }
        return tile1.isSolid() || tile2.isSolid();
    }

    /**
     * Checks if the entity collides with the player.
     * 
     * @return if the entity collides with the player.
     */
    public boolean collideWithPlayer() {
        if(game.getPlayer().isDead()) return false;
        solidArea.x += x;
        solidArea.y += y;
        game.getPlayer().getSolidArea().x += -game.getSceneX() + game.getPlayer().getX();
        game.getPlayer().getSolidArea().y += -game.getSceneY() + game.getPlayer().getY();

        switch(direction) {
            case UP -> solidArea.y -= speed;
            case DOWN -> solidArea.y += speed;
            case RIGHT -> solidArea.x += speed;
            case LEFT -> solidArea.x -= speed;
            default -> {}
        }
        if(solidArea.intersects(game.getPlayer().getSolidArea())) {
            solidArea.x = solidAreaDefaultX;
            solidArea.y = solidAreaDefaultY;
            game.getPlayer().getSolidArea().x = game.getPlayer().getSolidAreaDefaultX();
            game.getPlayer().getSolidArea().y = game.getPlayer().getSolidAreaDefaultY();
            return true;
        }
        solidArea.x = solidAreaDefaultX;
        solidArea.y = solidAreaDefaultY;
        game.getPlayer().getSolidArea().x = game.getPlayer().getSolidAreaDefaultX();
        game.getPlayer().getSolidArea().y = game.getPlayer().getSolidAreaDefaultY();
        return false;
    }

    /**
     * Checks if the entity collides with any other entity in the given list.
     * 
     * @param targetList the list of all the target entities
     * @param currentEntityDirection the current {@link Direction} of the entity
     * @return the index of the entity in the list if the entity collides to any other entity, -1 otherwise
     */
    public int collideWithEntity(ArrayList<Entity> targetList, Direction currentEntityDirection) {
        for(int i = 0; i < targetList.size(); i++) {
            Entity entity = targetList.get(i);
            if(entity != null && !entity.equals(this) && !entity.equals(game.getPlayer())) {
                if(this.equals(game.getPlayer())) {
                    solidArea.x += -game.getSceneX() + x;
                    solidArea.y += -game.getSceneY() + y;
                } else {
                    solidArea.x += x;
                    solidArea.y += y;
                }
                entity.getSolidArea().x += entity.getX();
                entity.getSolidArea().y += entity.getY();

                switch(currentEntityDirection) {
                    case UP -> solidArea.y -= speed;
                    case DOWN -> solidArea.y += speed;
                    case RIGHT -> solidArea.x += speed;
                    case LEFT -> solidArea.x -= speed;
                    default -> {}
                }
                if(solidArea.intersects(entity.getSolidArea())) {
                    solidArea.x = solidAreaDefaultX;
                    solidArea.y = solidAreaDefaultY;
                    entity.getSolidArea().x = entity.getSolidAreaDefaultX();
                    entity.getSolidArea().y = entity.getSolidAreaDefaultY();
                    return i;
                }
                solidArea.x = solidAreaDefaultX;
                solidArea.y = solidAreaDefaultY;
                entity.getSolidArea().x = entity.getSolidAreaDefaultX();
                entity.getSolidArea().y = entity.getSolidAreaDefaultY();
            }
        }
        return -1;
    }

    /**
     * Increases the image index so that it will always be in range.
     * 
     * @param currentIndex the current index
     * @param maxIndex the max index
     * @return the new index
     */
    public int increaseImageIndex(int currentIndex, int maxIndex) {
        int newIndex = currentIndex + 1;
        if(newIndex < maxIndex) {
            return newIndex;
        }
        return 0;
    }
    
    /**
     * Gets the {@link Renderer} where the entity will be drawn on.
     * 
     * @return the {@link Renderer} where the entity will be drawn on
     */
    public Renderer getRender() {
        return render;
    }

    /**
     * Set the {@link Renderer} where the entity will be drawn on.
     * 
     * @param render the {@link Renderer} where the entity will be drawn on
     */
    public void setRender(Renderer render) {
        this.render = render;
    }

    /**
     * Gets the {@link Game} where the entity interacts.
     * 
     * @return the {@link Game} where the entity interacts
     */
    public Game getGame() {
        return game;
    }

    /**
     * Gets the x coordinate of the entity.
     * 
     * @return the x coordinate of the entity
     */
    public int getX() {
        return x;
    }

    /**
     * Set the x coordinate of the entity to the given value.
     * 
     * @param x the given value
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the y coordinate of the entity.
     * 
     * @return the y coordinate of the entity
     */
    public int getY() {
        return y;
    }

    /**
     * Set the y coordinate of the entity to the given value.
     * 
     * @param y the given value
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets the hitbox of the entity.
     * 
     * @return the hitbox of the entity
     */
    public Rectangle getSolidArea() {
        return solidArea;
    }

    /**
     * Gets the default x coordinate of the hitbox.
     * 
     * @return the default x coordinate of the hitbox
     */
    public int getSolidAreaDefaultX() {
        return solidAreaDefaultX;
    }

    /**
     * Gets the default y coordinate of the hitbox.
     * 
     * @return the default y coordinate of the hitbox
     */
    public int getSolidAreaDefaultY() {
        return solidAreaDefaultY;
    }

    /**
     * Gets the current {@link Direction} of the entity.
     * 
     * @return the current {@link Direction} of the entity
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Set the current {@link Direction} of the entity to the given value.
     * 
     * @param dir the given value
     */
    public void setDirection(Direction dir) {
        direction = dir;
    }

    /**
     * Gets the previous direction of the entity.
     *
     * @return the previous direction of the entity
     */
    public Direction getPreviousDirection() {
        return previousDirection;
    }

    /**
     * Set the previous direction of the entity to the given value.
     *
     * @param previousDirection the given value
     */
    public void setPreviousDirection(Direction previousDirection) {
        this.previousDirection = previousDirection;
    }

    @Override
    public void setAttackValue(int value) {
        attackValue = value;
    }

    @Override
    public int getAttackValue() {
        return attackValue;
    }

    @Override
    public void setHealthValue(int value) {
        if(value > maxHealthValue) {
            System.err.println("Invalid value");
            currentHealthValue = maxHealthValue;
            return;
        }
        if(invincibleCounter < maxInvincibleCounter) {
            return;
        }

        currentHealthValue = value;
    }

    @Override
    public int getHealthValue() {
        return currentHealthValue;
    }

    @Override
    public void setMaxHealthValue(int value) {
        maxHealthValue = value;
    }

    @Override
    public int getMaxHealthValue() {
        return maxHealthValue;
    }

    /**
     * Gets the speed of the entity.
     * 
     * @return the speed of the entity
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Set the speed of the entity to the given value.
     * 
     * @param speed the given value
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Gets the speed of the entity when swimming.
     * 
     * @return the speed of the entity when swimming
     */
    public int getSwimmingSpeed() {
        return speed - speed/3;
    }

    /**
     * Checks if the entity is cursed.
     * 
     * @return if the entity is cursed.
     */
    public boolean isCursed() {
        return isCursed;
    }

    /**
     * Gets the current knockback counter of the entity.
     *
     * @return the current knockback counter of the entity
     */
    public int getKnockbackCounter() {
        return knockbackCounter;
    }

    /**
     * Set the current knockback counter to the given value.
     *
     * @param counter the given value
     */
    public void setKnockbackCounter(int counter) {
        this.knockbackCounter = counter;
    }

    /**
     * Gets the current invincible counter of the entity.
     * 
     * @return the current invincible counter
     */
    public int getInvincibleCounter() {
        return invincibleCounter;
    }

    /**
     * Set the current invincible counter of the entity to the given value.
     * 
     * @param counter the given value
     */
    public void setInvincibleCounter(int counter) {
        invincibleCounter = counter;
    }

    /**
     * Draws the health bar of the entity.
     * 
     * @param g the {@link Graphics} which is used to draw
     */
    public abstract void drawHealthBar(Graphics g);

    /**
     * Draws the entity.
     * 
     * @param g the {@link Graphics} which is used to draw
     */
    public abstract void draw(Graphics g);
}