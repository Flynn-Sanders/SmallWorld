import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Frog implements Runnable{
    private MyHashTable grid;
    private BufferedImage frogImg;
    private int tileSize = Screen.tileSize;
    int row,col;

    public Frog(MyHashTable gr,int r, int c){
        grid=gr;
        row=r; col=c;

        try {
            frogImg = ImageIO.read(new File("images/frog.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        @SuppressWarnings("unchecked")
        public void run(){
            while (true) {
                boolean found=false;
                while(found==false){
                    int dir = (int)(Math.random()*4)+1;
                    DLList<MapObject> toSquare = null;
                    switch(dir){
                        case 1:
                            if (row > 1) toSquare = grid.get(new Location(row - 2, col));
                            break;
                        case 2:
                            if (row < 98) toSquare = grid.get(new Location(row + 2, col));
                            break;
                        case 3:
                            if (col > 1) toSquare = grid.get(new Location(row, col - 2));
                            break;
                        case 4:
                            if (col < 98) toSquare = grid.get(new Location(row, col + 2));
                            break;
                    }
                    if(toSquare!=null && toSquare.get(0).value()<3){
                        switch (dir) {
                            case 1:
                                row-=2;
                                break;
                            case 2:
                                row+=2;
                                break;
                            case 3:
                                col-=2;
                                break;
                            case 4:
                                col+=2;
                                break;
                            default:
                                break;
                        }
                        found=true;
                    }
                }

                try{
                    Thread.sleep(1500);
                }catch(InterruptedException ex){
                    Thread.currentThread().interrupt();
                }
            }
    }
    public synchronized Point getPos(){
        return new Point(row,col);
    }
    public void draw(Graphics g, int cameraRow, int cameraCol){

        int screenRow = row - cameraRow;
        int screenCol = col - cameraCol;

        g.drawImage(frogImg, screenCol*tileSize, screenRow*tileSize, tileSize,tileSize,null);
    }
}
