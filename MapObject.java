import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class MapObject {
    private int value;
    private int tileSize = 4;

    private static BufferedImage rockImg, treeImg;

    static {
        try {
            rockImg = ImageIO.read(new File("images/rock.png"));
            treeImg = ImageIO.read(new File("images/tree.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public MapObject(int v) {
        value = v;
    }   

    public void draw(Graphics g, int row, int col) {
        int x = col * tileSize;
        int y = row * tileSize;
        
        Color bgColor = switch(value) {
            case 0 -> Color.green;
            case 1 -> Color.yellow;
            case 2 -> new Color(79,58,43);
            case 3 -> Color.blue;
            case 4 -> Color.green;
            case 5 -> Color.green;

            default -> null;
        };
        if(bgColor != null){
            g.setColor(bgColor);
            g.fillRect(x, y, tileSize, tileSize);
        }
        

        switch(value) {
            case 4:
                if (rockImg != null)
                    g.drawImage(rockImg, x, y, tileSize, tileSize, null);
                break;
            case 5:
                if (treeImg != null)
                    g.drawImage(treeImg, x, y, tileSize, tileSize, null);
                break;
        }
    }

    public int value(){ return value; }
}