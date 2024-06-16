package Common;

import javax.swing.*;
import java.io.File;

public class ImageLoader {
    public static ImageIcon loadImage(String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.err.println("Зображення не знайдено: " + file.getAbsolutePath());
            return null;
        }
        return new ImageIcon(file.getAbsolutePath());
    }
}
