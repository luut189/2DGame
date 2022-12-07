package gfx;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import entity.PlayerState;
import utils.Direction;

public class KeyHandler extends KeyAdapter {

    private Direction dir = Direction.NONE;

    private PlayerState state = PlayerState.STANDING;
    private Direction previousDirection = Direction.NONE;

    public Direction getDirection() {
        return dir;
    }
    
    public void setDirection(Direction dir) {
        this.dir = dir;
    }
    
    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public Direction getPreviousDirection() {
        return previousDirection;
    }

    public void setPreviousDirection(Direction previousDirection) {
        this.previousDirection = previousDirection;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        state = PlayerState.WALKING;
        int key = e.getKeyCode();

        switch(key) {
            case KeyEvent.VK_W:
                dir = Direction.UP;
                break;
            case KeyEvent.VK_S:
                dir = Direction.DOWN;
                break;
            case KeyEvent.VK_D:
                dir = Direction.RIGHT;
                break;
            case KeyEvent.VK_A:
                dir = Direction.LEFT;
                break;
            default:
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        state = PlayerState.STANDING;
        previousDirection = dir;
        dir = Direction.NONE;
    }
}