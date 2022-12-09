package gfx;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import entity.Direction;
import entity.EntityState;

public class KeyHandler extends KeyAdapter {

    private Direction playerDirection = Direction.NONE;

    private EntityState playerState = EntityState.STANDING;
    private Direction previousPlayerDirection = Direction.NONE;

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
            default:
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        playerState = EntityState.STANDING;
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_W || key == KeyEvent.VK_S || key == KeyEvent.VK_D || key == KeyEvent.VK_A) {
            previousPlayerDirection = playerDirection;
            playerDirection = Direction.NONE;
        }
    }
}