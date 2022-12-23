package gfx;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import entity.Direction;
import entity.EntityState;

public class KeyHandler extends KeyAdapter {

    private Direction playerDirection = Direction.NONE;

    private EntityState playerState = EntityState.STANDING;
    private Direction previousPlayerDirection = Direction.NONE;

    private boolean hasMinimap = false;
    
    private boolean isZooming = false;
    private int zoomDist;

    private GUI window;

    public KeyHandler(GUI window) {
        this.window = window;
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
        if(key == KeyEvent.VK_Q || key == KeyEvent.VK_E) {
            isZooming = true;
        }
        if(key == KeyEvent.VK_W || key == KeyEvent.VK_S || key == KeyEvent.VK_D || key == KeyEvent.VK_A) {
            playerState = EntityState.WALKING;
        }

        switch(key) {
            case KeyEvent.VK_W:
                playerDirection = Direction.UP;
                break;
            case KeyEvent.VK_S:
                playerDirection = Direction.DOWN;
                break;
            case KeyEvent.VK_D:
                playerDirection = Direction.RIGHT;
                break;
            case KeyEvent.VK_A:
                playerDirection = Direction.LEFT;
                break;
            case KeyEvent.VK_M:
                hasMinimap = !hasMinimap;
                break;
            case KeyEvent.VK_E:
                if(zoomDist > 30) break;
                zoomDist++;
                break;
            case KeyEvent.VK_Q:
                if(zoomDist == 5) break;
                zoomDist--;
                break;
            // case KeyEvent.VK_F:
            //     window.setFullscreen();
            //     break;
            default:
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_Q || key == KeyEvent.VK_E) {
            isZooming = false;
        }
        if(key == KeyEvent.VK_W || key == KeyEvent.VK_S || key == KeyEvent.VK_D || key == KeyEvent.VK_A) {
            playerState = EntityState.STANDING;
            previousPlayerDirection = playerDirection;
            playerDirection = Direction.NONE;
        }
    }
}