package Utils;

import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.ImageIcon;

public class XImage {
    public static Image getAppIcon() { // Trả về kiểu Image
        URL url = XImage.class.getResource("/icons/20.png");
        if (url != null) {
            return new ImageIcon(url).getImage();
        } else {
            System.err.println("Couldn't find file: /icons/20.png");
            return null;
        }
    }

    public static boolean save(File src) {
        File dst = new File("src/images/Logos", src.getName());
        if (!dst.getParentFile().exists()) {
            dst.getParentFile().mkdirs(); // Tạo thư mục //dst: Destination
        }
        try {
            Path from = Paths.get(src.getAbsolutePath());
            Path to = Paths.get(dst.getAbsolutePath());
            Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ImageIcon read(String filename) {
        File path = new File("src/images/Logos", filename);
        if (path.exists()) {
            return new ImageIcon(path.getAbsolutePath());
        } else {
            System.err.println("Couldn't find file: src/images/Logos/" + filename);
            return null;
        }
    }
}
