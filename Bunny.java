import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Bunny implements Runnable{
    private MyHashTable grid;
    private BufferedImage boatImg;
    private int tileSize = Screen.tileSize;
    int row,col;

    public Bunny(MyHashTable gr,int r, int c){
        grid=gr;
        row=r; col=c;

        try {
            boatImg = ImageIO.read(new File("images/bunny.png"));
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
                            toSquare = (row==0) ? null : grid.get(new Location(row-1, col));
                            break;
                        case 2: 
                            toSquare = (row==99) ? null : grid.get(new Location(row+1, col)); 
                            break;
                        case 3: 
                            toSquare = (col==0) ? null : grid.get(new Location(row, col-1)); 
                            break;
                        case 4: 
                            toSquare = (col==99) ? null : grid.get(new Location(row, col+1)); 
                            break;
                    }
                    if(toSquare!=null && toSquare.get(0).value()==0){
                        switch (dir) {
                            case 1:
                                row--;
                                break;
                            case 2:
                                row++;
                                break;
                            case 3:
                                col--;
                                break;
                            case 4:
                                col++;
                                break;
                            default:
                                break;
                        }
                        found=true;
                    }
                }

                try{
                    Thread.sleep(400);
                }catch(InterruptedException ex){
                    Thread.currentThread().interrupt();
                }
            }
    }
    public Point getPos(){
        return new Point(row,col);
    }
    public void draw(Graphics g, int cameraRow, int cameraCol){

        int screenRow = row - cameraRow;
        int screenCol = col - cameraCol;

        g.drawImage(boatImg, screenCol*tileSize, screenRow*tileSize, tileSize,tileSize,null);
    }
}
