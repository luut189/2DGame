package gfx;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

public class GUI extends JFrame {

    private KeyHandler keyHandler;
    private Renderer render;

    private GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

    public GUI(int width, int height, int FPS) {
        keyHandler = new KeyHandler(this);
        render = new Renderer(keyHandler, width, height, FPS);

        this.setTitle("2D Game");
        this.add(render);
        this.pack();
        this.addKeyListener(keyHandler);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }

    public void setFullscreen() {
        if(gd.getFullScreenWindow() == null) {
            gd.setFullScreenWindow(this);
        } else {
            gd.setFullScreenWindow(null);
        }
        render.setFullscreenAttribute(getWidth(), getHeight());
    }
    
}
