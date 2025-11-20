import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class MapObject {
    private int bg;
    private int object;
    /*
    first digit
    0 water
    1 sand
    2 dirt
    3 grass
    second digit
    0 nothing
    1 rock
    2 tree
    3-7 landmarks 1-5
     */
    private int tileSize = 50;

    private static BufferedImage rockImg, treeImg;

    public MapObject(int v) {
        object = v%10;
        bg = Math.floorDiv(v,10);
        try {
            rockImg = ImageIO.read(new File("images/rock.png"));
            treeImg = ImageIO.read(new File("images/tree.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g, int row, int col) {
        int x = col * tileSize;
        int y = row * tileSize;
        
        Color bgColor = switch(bg) {
            case 0 -> Color.BLUE;
            case 1 -> Color.yellow;
            case 2 -> new Color(79,58,43);
            case 3 -> Color.green;

            default -> null;
        };
        if(bgColor != null){
            g.setColor(bgColor);
            g.fillRect(x, y, tileSize, tileSize);
        }
        

        switch(object) {
            case 1:
                if (rockImg != null)
                    g.drawImage(rockImg, x, y, tileSize, tileSize, null);
                break;
            case 2:
                if (treeImg != null)
                    g.drawImage(treeImg, x, y, tileSize, tileSize, null);
                break;
        }
    }

    public int getBg(){
        return bg;
    }
    public int getObject(){
        return object;
    }
}