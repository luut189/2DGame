package dev.kyzel.gfx;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.util.ArrayList;

import javax.swing.JPanel;

import dev.kyzel.game.entity.Entity;
import dev.kyzel.game.entity.Player;
import dev.kyzel.utils.AssetManager;
import dev.kyzel.utils.EntityLoader;
import dev.kyzel.utils.TextureLoader;
import dev.kyzel.game.world.tile.Map;
import dev.kyzel.game.world.tile.TileManager;

public class Renderer extends JPanel implements Runnable {

    private KeyHandler keyHandler;

    private int width, height;
    
    private int screenWidth, screenHeight;

    private int originalUnitSize = 16;
    private int scale = 3;

    private int unitSize = originalUnitSize * scale;

    private int FPS;

    private TileManager tileManager;

    private Map map;

    private int camX, camY;
    private int sceneX, sceneY;

    private int playerSceneX, playerSceneY;

    private Player player;
    private ArrayList<Entity> entityList;

    private Thread gameThread;

    private BufferedImage gameImage;
    private Graphics gameImageGraphics;

    public Renderer(KeyHandler keyHandler, int width, int height, int FPS) {
        AssetManager.loadAllRes(new TextureLoader(), unitSize);
        this.keyHandler = keyHandler;

        this.width = width/unitSize*unitSize;
        this.height = height/unitSize*unitSize;
        
        screenWidth = this.width;
        screenHeight = this.height;

        camX = this.width/2-unitSize/2;
        camY = this.height/2-unitSize/2;

        sceneX = 0;
        sceneY = 0;

        playerSceneX = sceneX;
        playerSceneY = sceneY;

        this.FPS = FPS;

        this.setPreferredSize(new Dimension(this.width, this.height));
        this.setDoubleBuffered(true);

        gameImage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);

        tileManager = new TileManager(this);
        
        map = new Map(this, tileManager, 10);
        keyHandler.setZoomDist(map.getMapSize());

        player = new Player(this, camX, camY, 4);
        entityList = new ArrayList<>();
        entityList.add(player);
        EntityLoader.loadEntity(entityList, this, tileManager);

        gameThread = new Thread(this);
        gameThread.start();
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

    public KeyHandler getKeyHandler() {
        return keyHandler;
    }

    public int getUnitScale() {
        return scale;
    }

    public int getUnitSize() {
        return unitSize;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
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

    public void drawToTempScreen() {
        gameImageGraphics = gameImage.getGraphics();
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
                    entity.getX() >= -playerSceneX-unitSize &&
                    entity.getY() >= -playerSceneY-unitSize &&
                    entity.getX() < width - playerSceneX &&
                    entity.getY() < height - playerSceneY
                ) {
                    entity.draw(gameImageGraphics);
                    if(keyHandler.hasHUD()) entity.drawHealthBar(gameImageGraphics);
                }
            }
        }
        gameImageGraphics.translate(-playerSceneX, -playerSceneY);

        if(keyHandler.hasHUD()) player.drawHealthBar(gameImageGraphics);

        if(keyHandler.hasMinimap()) map.drawMinimap(gameImageGraphics);
        gameImageGraphics.dispose();
    }

    public void drawToScreen() {
        Graphics g = getGraphics();
        if(g == null) {
            System.err.println("No Graphics found, retrying...");
            return;
        }
        g.drawImage(gameImage, 0, 0, screenWidth, screenHeight, null);
        g.dispose();
    }

    public void setFullscreenAttribute(int width, int height) {
        this.width = width;
        this.height = height;

        screenWidth = this.width;
        screenHeight = this.height;

        gameImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        camX = this.width/2-unitSize/2;
        camY = this.height/2-unitSize/2;

        sceneX += camX - player.getX();
        sceneY += camY - player.getY();
        if(sceneX > 0) playerSceneX = 0;
        if(sceneY > 0) playerSceneY = 0;
        
        player.setX(camX);
        player.setY(camY);
    }

    public void update() {
        map.update(keyHandler);
        for(Entity entity : entityList) {
            if(entity instanceof Player) entity.update();
            else if(
                entity.getX() >= -playerSceneX-unitSize &&
                entity.getY() >= -playerSceneY-unitSize &&
                entity.getX() < width - playerSceneX &&
                entity.getY() < height - playerSceneY
            ) {
                entity.update();
            }
        }
    }

    @Override
    public void run() {
        double interval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime)/interval;
            lastTime = currentTime;
            if(delta >= 1) {
                update();
                drawToTempScreen();
                drawToScreen();
                delta--;
            }
        }
    }
}