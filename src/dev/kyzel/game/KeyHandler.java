package dev.kyzel.game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import dev.kyzel.game.entity.Direction;
import dev.kyzel.game.entity.EntityState;
import dev.kyzel.gfx.Window;

public class KeyHandler extends KeyAdapter {

    private GameState gameState = GameState.PLAYING;

    private Direction playerDirection = Direction.NONE;

    private EntityState playerState = EntityState.STANDING;
    private Direction previousPlayerDirection = Direction.NONE;

    private boolean hasHUD = true;

    private boolean hasMinimap = false;
    
    private boolean isZooming = false;
    private int zoomDist;

    private int keyPressing = 0;

    private final Window window;

    public KeyHandler(Window window) {
        this.window = window;
    }

    public boolean hasHUD() {
        return hasHUD;
    }

    public boolean hasMinimap() {
        return hasMinimap;
    }

    public boolean isZooming() {
        return isZooming;
    }

    public int getZoomDist() {
        return zoomDist;
    }

    public void setZoomDist(int zoomDist) {
        this.zoomDist = zoomDist;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Direction getPlayerDirection() {
        return playerDirection;
    }
    
    public void setPlayerDirection(Direction dir) {
        this.playerDirection = dir;
    }
    
    public EntityState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(EntityState state) {
        this.playerState = state;
    }

    public Direction getPreviousPlayerDirection() {
        return previousPlayerDirection;
    }

    public void setPreviousPlayerDirection(Direction previousDirection) {
        this.previousPlayerDirection = previousDirection;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if(gameState == GameState.PAUSE && (key != KeyEvent.VK_ESCAPE && key != KeyEvent.VK_F)) return;
        
        if(key == KeyEvent.VK_Q || key == KeyEvent.VK_E) {
            isZooming = true;
        }
        if(key == KeyEvent.VK_W || key == KeyEvent.VK_S || key == KeyEvent.VK_D || key == KeyEvent.VK_A) {
            playerState = EntityState.WALKING;
        }

        switch(key) {
            case KeyEvent.VK_W -> {
                keyPressing = key;
                playerDirection = Direction.UP;
            }
            case KeyEvent.VK_S -> {
                keyPressing = key;
                playerDirection = Direction.DOWN;
            }
            case KeyEvent.VK_D -> {
                keyPressing = key;
                playerDirection = Direction.RIGHT;
            }
            case KeyEvent.VK_A -> {
                keyPressing = key;
                playerDirection = Direction.LEFT;
            }
            case KeyEvent.VK_M -> hasMinimap = !hasMinimap;
            case KeyEvent.VK_E -> {
                if(zoomDist > 30) break;
                zoomDist++;
            }
            case KeyEvent.VK_Q -> {
                if(zoomDist == 5) break;
                zoomDist--;
            }
            case KeyEvent.VK_H -> hasHUD = !hasHUD;
            case KeyEvent.VK_F -> window.setFullscreen();
            case KeyEvent.VK_ESCAPE -> {
                if(gameState == GameState.PLAYING) gameState = GameState.PAUSE;
                else if(gameState == GameState.PAUSE) gameState = GameState.PLAYING;
            }
            default -> {}
        }

        if(key == KeyEvent.VK_SPACE) {
            playerState = EntityState.ATTACKING;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_Q || key == KeyEvent.VK_E) {
            isZooming = false;
        }
        if(key == KeyEvent.VK_SPACE) {
            playerState = EntityState.STANDING;
        }
        if(key == keyPressing) {
            playerState = EntityState.STANDING;
            previousPlayerDirection = playerDirection;
            playerDirection = Direction.NONE;
        }
    }
}