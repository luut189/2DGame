package dev.kyzel.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import dev.kyzel.App;

public class TextureLoader {

    public static BufferedImage loadImage(String path) {
        InputStream stream = App.class.getResourceAsStream(path);
        if(stream == null) return null;
        try {
            return ImageIO.read(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
