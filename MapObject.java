import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class MapObject {
    private int value;
    private int tileSize = 25;

    private static BufferedImage rockImg, treeImg, mark7Img, mark8Img, mark9Img, mark10Img, mark11Img;

    

    static {
        try {
            rockImg = ImageIO.read(new File("images/rock.png"));
            treeImg = ImageIO.read(new File("images/tree.png"));
            mark7Img = ImageIO.read(new File("images/mark7.png")); //flight 93 national memorial
            mark8Img = ImageIO.read(new File("images/mark8.png")); //valley forge national historic park
            mark9Img = ImageIO.read(new File("images/mark9.png")); //Phipps Conservatory and Botanical Gardens
            mark10Img = ImageIO.read(new File("images/mark10.png")); // steamtown national historic site
            mark11Img = ImageIO.read(new File("images/mark11.png")); //falling water
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
            case 4 -> new Color(79,58,43);
            case 5 -> Color.green;
            case 6 -> Color.gray;

            default -> Color.red;
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
            case 7:
                if (mark7Img != null)
                    g.drawImage(mark7Img, x, y, tileSize, tileSize, null);
                break;
            case 8:
                if (mark8Img != null)
                    g.drawImage(mark8Img, x, y, tileSize, tileSize, null);
                break;
            case 9:
                if (mark9Img != null)
                    g.drawImage(mark9Img, x, y, tileSize, tileSize, null);
                break;
            case 10:
                if (mark10Img != null)
                    g.drawImage(mark10Img, x, y, tileSize, tileSize, null);
                break;
            case 11:
                if (mark11Img != null)
                    g.drawImage(mark11Img, x, y, tileSize, tileSize, null);
                break;
        }
    }

    public int value(){ return value; }
}