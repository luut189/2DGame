package dev.kyzel.game;

import java.awt.Graphics;
import java.util.ArrayList;

import dev.kyzel.game.entity.Entity;
import dev.kyzel.game.entity.Player;
import dev.kyzel.game.menu.OverMenu;
import dev.kyzel.game.menu.PauseMenu;
import dev.kyzel.game.world.tile.Minimap;
import dev.kyzel.game.world.tile.TileManager;
import dev.kyzel.gfx.Renderer;
import dev.kyzel.utils.EntityLoader;
import dev.kyzel.utils.Keyboard;

public class Game implements Runnable {

    private GameState gameState;
    private Renderer render;
    private TileManager tileManager;
    private Minimap minimap;

    private boolean isPausing;
    private boolean hasHUD, hasMinimap;

    private int camX, camY;
    private int sceneX, sceneY;

    private int playerSceneX, playerSceneY;

    private Player player;
    private ArrayList<Entity> entityList;

    private Thread gameThread;

    public Game(Renderer render) {
        gameState = GameState.PLAYING;

        this.render = render;

        isPausing = false;
        hasHUD = true;
        hasMinimap = false;

        camX = render.getWidth()/2-render.getUnitSize()/2;
        camY = render.getHeight()/2-render.getUnitSize()/2;

        sceneX = 0;
        sceneY = 0;

        playerSceneX = sceneX;
        playerSceneY = sceneY;

        tileManager = new TileManager(render, this);

        minimap = new Minimap(render, this, tileManager, 10);

        player = new Player(render, this, camX, camY, 4);
        entityList = new ArrayList<>();
        entityList.add(player);
        EntityLoader.loadEntity(entityList, render, this, tileManager);

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void setFullscreenAttribute(int width, int height) {
        camX = width/2-render.getUnitSize()/2;
        camY = height/2-render.getUnitSize()/2;

        sceneX += camX - player.getX();
        sceneY += camY - player.getY();
        if(sceneX > 0) playerSceneX = 0;
        if(sceneY > 0) playerSceneY = 0;

        player.setX(camX);
        player.setY(camY);
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Entity> getEntityList() {
        return entityList;
    }

    public TileManager getTileManager() {
        return tileManager;
    }

    public int getPlayerSceneX() {
        return playerSceneX;
    }

    public void setPlayerSceneX(int playerSceneX) {
        this.playerSceneX = playerSceneX;
    }

    public int getPlayerSceneY() {
        return playerSceneY;
    }

    public void setPlayerSceneY(int playerSceneY) {
        this.playerSceneY = playerSceneY;
    }

    public int getSceneX() {
        return sceneX;
    }

    public void setSceneX(int sceneX) {
        this.sceneX = sceneX;
    }

    public int getSceneY() {
        return sceneY;
    }

    public void setSceneY(int sceneY) {
        this.sceneY = sceneY;
    }

    public int getCamX() {
        return camX;
    }

    public int getCamY() {
        return camY;
    }

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
            case PAUSE -> new PauseMenu(render).draw(gameImageGraphics);
            case OVER -> new OverMenu(render, this).draw(gameImageGraphics);
            default -> {}
        }

        gameImageGraphics.dispose();
    }

    public void update() {
        Keyboard.update();
        if(ControlHandler.PAUSE.pressed()) isPausing = !isPausing;
        if(isPausing) return;
        
        if(ControlHandler.TOGGLE_MINIMAP.pressed()) hasMinimap = !hasMinimap;
        if(ControlHandler.TOGGLE_HUD.pressed()) hasHUD = !hasHUD;

        minimap.update();
        for(Entity entity : entityList) {
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
        if(player.isDead()) {
            gameState = GameState.OVER;
            gameThread = null;
        }
    }

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
                gameState = isPausing ? GameState.PAUSE : GameState.PLAYING;
                update();
                draw(render.getGameImageGraphics());
                render.drawToScreen();
                delta--;
            }
        }
    }
}