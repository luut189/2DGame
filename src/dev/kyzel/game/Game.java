package dev.kyzel.game;

import java.awt.Graphics;
import java.util.ArrayList;

import dev.kyzel.game.entity.Entity;
import dev.kyzel.game.entity.Player;
import dev.kyzel.game.menu.OverMenu;
import dev.kyzel.game.menu.PauseMenu;
import dev.kyzel.game.menu.StatMenu;
import dev.kyzel.game.world.tile.Minimap;
import dev.kyzel.game.world.tile.TileManager;
import dev.kyzel.gfx.Renderer;
import dev.kyzel.gfx.Window;
import dev.kyzel.utils.EntityLoader;
import dev.kyzel.utils.Keyboard;

/**
 * The main game.
 */
public class Game implements Runnable {

    /**
     * The current {@link GameState}.
     */
    private GameState gameState;

    /**
     * The {@link Renderer} where the game will be drawn on.
     */
    private Renderer render;

    /**
     * The {@link TileManager} of the game.
     */
    private TileManager tileManager;

    /**
     * The {@link Minimap} of the game.
     */
    private Minimap minimap;

    /**
     * A variable to see if the game is paused.
     */
    private boolean isPausing;

    /**
     * A variable to see if the game has HUD.
     */
    private boolean hasHUD;

    /**
     * A variable to see if the game has minimap.
     */
    private boolean hasMinimap;

    /**
     * The x coordinate of the player on the screen.
     */
    private int camX;

    /**
     * The y coordinate of the player on the screen.
     */
    private int camY;

    /**
     * The x offset of the camera.
     */
    private int sceneX;
    
    /**
     * The y offset of the camera.
     */
    private int sceneY;

    /**
     * A modified x offset of the camera to prevent showing the void.
     */
    private int playerSceneX;
    
    /**
     * A modified y offset of the camera to prevent showing the void.
     */
    private int playerSceneY;

    /**
     * The {@link Player} of the game.
     */
    private Player player;

    /**
     * The {@link Entity} list of the game.
     */
    private ArrayList<Entity> entityList;

    /**
     * The main {@link Thread} for the game.
     */
    private Thread gameThread;

    /**
     * Creates a new game.
     * 
     * @param render the {@link Renderer} where the game will be drawn on
     */
    public Game(Renderer render) {
        gameState = GameState.PLAYING;

        this.render = render;

        isPausing = false;
        hasHUD = true;
        hasMinimap = false;

        camX = render.getWidth()/2-render.getUnitSize()/2;
        camY = render.getHeight()/2-render.getUnitSize()/2;

        tileManager = new TileManager(render, this);
        minimap = new Minimap(render, this, 10);

        setPlayerInitialPosition();
        player = new Player(render, this, camX, camY, 4);
        
        entityList = new ArrayList<>();
        entityList.add(player);
        EntityLoader.loadEntity(entityList, render, this, tileManager);

        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Set the initial position for the player and camera.
     */
    public void setPlayerInitialPosition() {
        sceneX = - (int) (Math.random() * tileManager.getMaxRow());
        sceneY = - (int) (Math.random() * tileManager.getMaxCol());

        while(tileManager.getTile(-sceneX, -sceneY).isSolid()) {
            sceneX = - (int) (Math.random() * tileManager.getMaxRow());
            sceneY = - (int) (Math.random() * tileManager.getMaxCol());
        }
        sceneX *= render.getUnitSize();
        sceneY *= render.getUnitSize();

        playerSceneX = sceneX;
        playerSceneY = sceneY;
    }

    /**
     * Set the attributes of the game to match with the new width and height.
     *
     * @param width the new width
     * @param height the new height
     */
    public void setAttributes(int width, int height) {
        camX = width/2-render.getUnitSize()/2;
        camY = height/2-render.getUnitSize()/2;

        sceneX += camX - player.getX();
        sceneY += camY - player.getY();
        if(sceneX > 0) playerSceneX = 0;
        if(sceneY > 0) playerSceneY = 0;

        player.setX(camX);
        player.setY(camY);
    }

    /**
     * Gets the {@link Player} of the game.
     * 
     * @return the {@link Player} of the game
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the {@link Entity} list of the game.
     * 
     * @return the {@link Entity} list of the game
     */
    public ArrayList<Entity> getEntityList() {
        return entityList;
    }

    /**
     * Gets the {@link TileManager} of the game.
     * 
     * @return the {@link TileManager} of the game
     */
    public TileManager getTileManager() {
        return tileManager;
    }

    /**
     * Gets the modified x offset of the camera.
     * 
     * @return the modified x offset of the camera
     */
    public int getPlayerSceneX() {
        return playerSceneX;
    }

    /**
     * Set the modified x offset of the camera to the given value.
     * 
     * @param playerSceneX the given value
     */
    public void setPlayerSceneX(int playerSceneX) {
        this.playerSceneX = playerSceneX;
    }

    /**
     * Gets the modified y offset of the camera.
     * 
     * @return the modified y offset of the camera
     */
    public int getPlayerSceneY() {
        return playerSceneY;
    }

    /**
     * Set the modified y offset of the camera to the given value.
     * 
     * @param playerSceneY the given value
     */
    public void setPlayerSceneY(int playerSceneY) {
        this.playerSceneY = playerSceneY;
    }

    /**
     * Gets the x offset of the camera.
     * 
     * @return the x offset of the camera
     */
    public int getSceneX() {
        return sceneX;
    }

    /**
     * Set the x offset of the camera to the given value.
     * 
     * @param sceneX the given value
     */
    public void setSceneX(int sceneX) {
        this.sceneX = sceneX;
    }

    /**
     * Gets the y offset of the camera.
     * 
     * @return the y offset of the camera
     */
    public int getSceneY() {
        return sceneY;
    }

    /**
     * Set the y offset of the camera to the given value.
     * 
     * @param sceneY the given value
     */
    public void setSceneY(int sceneY) {
        this.sceneY = sceneY;
    }

    /**
     * Gets the x coordinate of the player on the screen.
     * 
     * @return the x coordinate of the player on the screen
     */
    public int getCamX() {
        return camX;
    }

    /**
     * Gets the y coordinate of the player on the screen.
     * 
     * @return the y coordinate of the player on the screen
     */
    public int getCamY() {
        return camY;
    }

    /**
     * Draws the game.
     * 
     * @param gameImageGraphics the {@link Graphics} which is used to draw
     */
    public void draw(Graphics gameImageGraphics) {
        ArrayList<Entity> drawingList = new ArrayList<>(entityList);
        drawingList.sort((e1, e2) -> {
            int y1 = e1.getY();
            int y2 = e2.getY();
            if(e1.equals(player)) y1 -= sceneY;
            if(e2.equals(player)) y2 -= sceneY;
            return Integer.compare(y1, y2);
        });
        tileManager.drawAllTexture(gameImageGraphics);
        for(Entity entity : drawingList) {
            if(entity instanceof Player) {
                gameImageGraphics.translate(-sceneX, -sceneY);
                entity.draw(gameImageGraphics);
                gameImageGraphics.translate(sceneX, sceneY);
            } else {
                if(
                    entity.getX() >= -playerSceneX-render.getUnitSize() &&
                    entity.getY() >= -playerSceneY-render.getUnitSize() &&
                    entity.getX() < render.getWidth() - playerSceneX &&
                    entity.getY() < render.getHeight() - playerSceneY
                ) {
                    entity.draw(gameImageGraphics);
                    if(hasHUD) entity.drawHealthBar(gameImageGraphics);
                }
            }
        }
        gameImageGraphics.translate(-playerSceneX, -playerSceneY);

        if(hasHUD) {
            player.drawHealthBar(gameImageGraphics);
            player.drawScoreBar(gameImageGraphics);
            player.drawHitCooldownBar(gameImageGraphics);
        }
        if(hasMinimap) minimap.draw(gameImageGraphics);
    
        switch(gameState) {
            case OVER -> {
                new OverMenu(render, this).draw(gameImageGraphics);
                gameImageGraphics.dispose();
                return;
            }
            case PAUSE -> {
                new PauseMenu(render).draw(gameImageGraphics);
                gameImageGraphics.dispose();
                return;
            }
            default -> {}
        }

        if(ControlHandler.SHOW_STAT.down()) new StatMenu(render, this).draw(gameImageGraphics);

        gameImageGraphics.dispose();
    }

    /**
     * Update the game.
     */
    public void update() {
        Keyboard.update();
        if(ControlHandler.PAUSE.pressed()) isPausing = !isPausing;
        if(ControlHandler.TOGGLE_FULLSCREEN.pressed()) Window.window.setFullscreen();
        if(isPausing) return;
        
        if(ControlHandler.TOGGLE_MINIMAP.pressed()) hasMinimap = !hasMinimap;
        if(ControlHandler.TOGGLE_HUD.pressed()) hasHUD = !hasHUD;

        minimap.update();

        for(Entity entity : entityList) {
            if(entity == null) continue;
            if(entity instanceof Player) entity.update();
            else if(
                    entity.getX() >= -playerSceneX-render.getUnitSize() &&
                    entity.getY() >= -playerSceneY-render.getUnitSize() &&
                    entity.getX() < render.getWidth() - playerSceneX &&
                    entity.getY() < render.getHeight() - playerSceneY
            ) {
                entity.update();
            }
        }
        while(entityList.contains(null)) {
            entityList.remove(null);
        }
        if(player.isDead()) {
            gameState = GameState.OVER;
        }
    }

    /**
     * Updates and redraws the game every 60 ticks.
     */
    @Override
    public void run() {
        double interval = 1000000000.0 / render.getFPS();
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime)/interval;
            lastTime = currentTime;
            if(delta >= 1) {
                gameState = isPausing && gameState != GameState.OVER ? GameState.PAUSE : GameState.PLAYING;
                update();
                draw(render.getGameImageGraphics());
                render.drawToScreen();
                delta--;
            }
        }
    }
}