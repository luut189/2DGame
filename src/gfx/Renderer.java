package gfx;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import entity.Direction;
import entity.Player;
import entity.EntityState;
import world.tile.TileManager;

public class Renderer extends JPanel implements Runnable {

    private KeyHandler keyHandler;

    private int width, height;

    private int originalUnitSize = 16;
    private int scale = 3;

    private int unitSize = originalUnitSize * scale;

    private int FPS;

    private TileManager tileManager;

    private int camX, camY;
    private int sceneX, sceneY;
    private Player player;

    private Thread gameThread;

    public Renderer(KeyHandler keyHandler, int width, int height, int FPS) {
        this.keyHandler = keyHandler;

        this.width = width/unitSize*unitSize;
        this.height = height/unitSize*unitSize;

        camX = width/2-unitSize/2;
        camY = height/2-unitSize/2;

        sceneX = 0;
        sceneY = 0;

        player = new Player(this, camX, camY);

        this.FPS = FPS;

        this.setPreferredSize(new Dimension(this.width, this.height));
        this.setDoubleBuffered(true);

        tileManager = new TileManager(this);
        gameThread = new Thread(this);
        gameThread.start();
    }

    public int getUnitScale() {
        return scale;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getUnitSize() {
        return unitSize;
    }

    public int getSceneX() {
        return sceneX;
    }

    public int getSceneY() {
        return sceneY;
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
        g.translate(-sceneX, -sceneY);
        player.draw(g);
    }

    public void update() {
        Direction dir = keyHandler.getDirection();
        EntityState state = keyHandler.getState();
        if(dir == Direction.NONE) {
            player.setCurrentPlayerImage(state, keyHandler.getPreviousDirection());
            return;
        }
        player.setCurrentPlayerImage(state, dir);
        switch(dir) {
            case UP:
                if(-sceneY+camY < 0 || player.collide(tileManager.getWorldTiles(), dir)) break;
                sceneY += player.getSpeed();
                break;
            case DOWN:
                if(-sceneY+camY >= tileManager.getMaxCol()*unitSize-unitSize || player.collide(tileManager.getWorldTiles(), dir)) break;
                sceneY -= player.getSpeed();
                break;
            case RIGHT:
                if(-sceneX+camX >= tileManager.getMaxRow()*unitSize-unitSize || player.collide(tileManager.getWorldTiles(), dir)) break;
                sceneX -= player.getSpeed();
                break;
            case LEFT:
                if(-sceneX+camX <= 0 || player.collide(tileManager.getWorldTiles(), dir)) break;
                sceneX += player.getSpeed();
                break;
            default:
                break;
        }
    }

    @Override
    public void run() {
        double interval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        long timer = 0;
        long drawCount = 0;

        while(gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime)/interval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            if(delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            // print the fps
            // if(timer >= 1000000000) {
            //     System.out.println("FPS: " + drawCount);
            //     drawCount = 0;
            //     timer = 0;
            // }
        }
        
    }
    
}