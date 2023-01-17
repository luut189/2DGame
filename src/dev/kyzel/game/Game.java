package dev.kyzel.game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import dev.kyzel.game.entity.Entity;
import dev.kyzel.game.entity.Player;
import dev.kyzel.game.world.tile.Map;
import dev.kyzel.game.world.tile.TileManager;
import dev.kyzel.gfx.Renderer;
import dev.kyzel.utils.EntityLoader;

public class Game implements Runnable {

    private GameState gameState;

    private Renderer render;
    private KeyHandler keyHandler;

    private TileManager tileManager;

    private Map map;

    private int camX, camY;
    private int sceneX, sceneY;

    private int playerSceneX, playerSceneY;

    private Player player;
    private ArrayList<Entity> entityList;

    private Thread gameThread;

    public Game(Renderer render, KeyHandler keyHandler) {
        gameState = GameState.PLAYING;

        this.render = render;
        this.keyHandler = keyHandler;

        camX = render.getWidth()/2-render.getUnitSize()/2;
        camY = render.getHeight()/2-render.getUnitSize()/2;

        sceneX = 0;
        sceneY = 0;

        playerSceneX = sceneX;
        playerSceneY = sceneY;

        tileManager = new TileManager(render, this);

        map = new Map(render, this, tileManager, 10);
        this.keyHandler.setZoomDist(map.getMapSize());

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

    public KeyHandler getKeyHandler() {
        return keyHandler;
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
                    if(keyHandler.hasHUD()) entity.drawHealthBar(gameImageGraphics);
                }
            }
        }
        gameImageGraphics.translate(-playerSceneX, -playerSceneY);

        if(keyHandler.hasHUD()) {
            player.drawHealthBar(gameImageGraphics);
            player.drawScoreBar(gameImageGraphics);
            player.drawHitCooldownBar(gameImageGraphics);
        }
        if(keyHandler.hasMinimap()) map.drawMinimap(gameImageGraphics);
    
        if(gameState == GameState.PAUSE) {
            Graphics2D g2d = (Graphics2D) gameImageGraphics;

            String pauseText = "PAUSED";
            gameImageGraphics.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 50));
            int textWidth = (int) gameImageGraphics.getFontMetrics().getStringBounds(pauseText, gameImageGraphics).getWidth();
            int textHeight = (int) gameImageGraphics.getFontMetrics().getStringBounds(pauseText, gameImageGraphics).getHeight();

            int boxWidth = textWidth * 2;
            int boxHeight = 100;

            int boxX = render.getWidth()/2 - boxWidth/2;
            int boxY = render.getHeight()/2 - boxHeight/2;
            
            g2d.setColor(Color.white);
            g2d.setStroke(new BasicStroke(5));
            g2d.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 10, 10);
            g2d.setColor(new Color(0, 0, 0, 200));
            g2d.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 10, 10);
            
            gameImageGraphics.setColor(Color.white);
            gameImageGraphics.drawString(pauseText, render.getWidth()/2 - textWidth/2, render.getHeight()/2 + textHeight/4);
        }

        gameImageGraphics.dispose();
    }

    public void update() {
        map.update(keyHandler);
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
    }

    @Override
    public void run() {
        double interval = 1000000000/ render.getFPS();
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime)/interval;
            lastTime = currentTime;
            if(delta >= 1) {
                gameState = keyHandler.getGameState();
                if(gameState != GameState.TITLE && gameState != GameState.PAUSE) update();
                draw(render.getGameImageGraphics());
                render.drawToScreen();
                delta--;
            }
        }
    }
}