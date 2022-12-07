package utils;

import java.awt.image.BufferedImage;

import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class TextureLoader {

    public BufferedImage loadImage(String path) {
        InputStream stream = getClass().getResourceAsStream(path);
        if(stream == null) return null;
        try {
            return ImageIO.read(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
