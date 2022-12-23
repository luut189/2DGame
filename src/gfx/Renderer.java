package gfx;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import entity.Entity;
import entity.Player;

import utils.AssetManager;
import utils.EntityLoader;
import utils.TextureLoader;

import world.tile.TileManager;
import world.tile.Map;

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
        Collections.sort(drawingList, new Comparator<Entity>() {

            @Override
            public int compare(Entity e1, Entity e2) {
                int y1 = e1.getY();
                int y2 = e2.getY();
                if(e1.equals(player)) y1 -= sceneY;
                if(e2.equals(player)) y2 -= sceneY;
                return Integer.compare(y1, y2);
            }
            
        });
        tileManager.drawAllTexture(gameImageGraphics);
        for(Entity entity : drawingList) {
            if(entity instanceof Player) {
                gameImageGraphics.translate(-sceneX, -sceneY);
                entity.draw(gameImageGraphics);
                gameImageGraphics.translate(sceneX, sceneY);
            } else {
                entity.draw(gameImageGraphics);
            }
        }
        gameImageGraphics.translate(-playerSceneX, -playerSceneY);
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

    boolean isLoadingFullscreen = false;
    public void setFullscreenAttribute(int width, int height) {
        isLoadingFullscreen = true;
        this.width = width;
        this.height = height;

        screenWidth = this.width;
        screenHeight = this.height;

        gameImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        tileManager = new TileManager(this);

        camX = this.width/2-unitSize/2;
        camY = this.height/2-unitSize/2;

        sceneX += camX - player.getX();
        sceneY += camY - player.getY();
        for(Entity entity : entityList) {
            entity.setX(entity.getX() + camX - player.getX());
            entity.setY(entity.getY() + camY - player.getY());
        }

        playerSceneX = sceneX;
        playerSceneY = sceneY;

        player.setX(camX);
        player.setY(camY);
        isLoadingFullscreen = false;
    }

    public void update() {
        if(isLoadingFullscreen) return;
        map.update(keyHandler);
        for(Entity entity : entityList) {
            entity.update();
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