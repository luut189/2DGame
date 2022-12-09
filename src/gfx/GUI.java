package gfx;

import javax.swing.JFrame;

public class GUI extends JFrame {
    
    public GUI(int width, int height, int FPS) {
        KeyHandler keyHandler = new KeyHandler();
        Renderer render = new Renderer(keyHandler, width, height, FPS);

        this.setTitle("2D Game");
        this.add(render);
        this.pack();
        this.addKeyListener(keyHandler);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }
    
}