package gfx;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import entity.Player;
import entity.animal.Slime;

import utils.AssetManager;
import utils.TextureLoader;

import world.tile.TileManager;
import world.tile.Map;

public class Renderer extends JPanel implements Runnable {

    private KeyHandler keyHandler;

    private int width, height;

    private int originalUnitSize = 16;
    private int scale = 3;

    private int unitSize = originalUnitSize * scale;

    private int FPS;

    private TileManager tileManager;

    private Map map;

    private int camX, camY;
    private int sceneX, sceneY;

    private Player player;
    private Slime[] slimes;

    private Thread gameThread;

    public Renderer(KeyHandler keyHandler, int width, int height, int FPS) {
        AssetManager.loadAllRes(new TextureLoader(), unitSize);
        this.keyHandler = keyHandler;

        this.width = width/unitSize*unitSize;
        this.height = height/unitSize*unitSize;

        camX = width/2-unitSize/2;
        camY = height/2-unitSize/2;

        sceneX = 0;
        sceneY = 0;

        this.FPS = FPS;

        this.setPreferredSize(new Dimension(this.width, this.height));
        this.setDoubleBuffered(true);

        tileManager = new TileManager(this);
        
        map = new Map(this, tileManager, 10);
        keyHandler.setZoomDist(map.getMapSize());

        player = new Player(this, camX, camY, 4);
        slimes = new Slime[1000];
        for(int i = 0; i < slimes.length; i++) {
            int x = (int) (Math.random()*this.width*unitSize)-unitSize;
            int y = (int) (Math.random()*this.height*unitSize)-unitSize;
            while(tileManager.getWorldTiles()[(x+unitSize/2)/unitSize][(y+unitSize)/unitSize].getTileName().equals("water") ||
                  tileManager.getWorldTiles()[(x+unitSize/2)/unitSize][(y+unitSize)/unitSize].getTileName().equals("tree")) {
                x = (int) (Math.random()*this.width*unitSize)-unitSize;
                y = (int) (Math.random()*this.height*unitSize)-unitSize;
            }
            slimes[i] = new Slime(this, x, y, 1);
        }

        gameThread = new Thread(this);
        gameThread.start();
    }

    public Player getPlayer() {
        return player;
    }

    public TileManager getTileManager() {
        return tileManager;
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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        tileManager.drawAllTexture(g);
        for(int i = 0; i < slimes.length; i++) {
            if(slimes[i] != null) {
                slimes[i].draw(g);
            }
        }
        g.translate(-sceneX, -sceneY);
        player.draw(g);
        if(keyHandler.hasMinimap()) map.drawMinimap(g);
    }

    public void update() {
        map.update(keyHandler);
        player.update(this, keyHandler, tileManager);
        for(int i = 0; i < slimes.length; i++) {
            if(slimes[i] != null) {
                slimes[i].update(tileManager);
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
                repaint();
                delta--;
            }
        }
    }
}