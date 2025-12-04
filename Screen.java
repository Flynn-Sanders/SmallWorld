import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Screen extends JPanel implements KeyListener {
    private MyHashTable<Location,MapObject> map;
    private static BufferedImage playerImg;
    
    private Boat boat1;
    private Boat boat2;

    private Deer deer1;
    private Deer deer2;

    private Bunny bunny1;
    private Bunny bunny2;

    private Frog frog1;
    private Frog frog2;

    private int playerRow = 34;
    private int playerCol = 39;

    private boolean isNearLandmark;
    private int markValue;
     
    private int viewRows = 30;
    private int viewCols = 30; 

    private int cameraRow = playerRow-viewRows/2;
    private int cameraCol = playerCol-viewCols/2;;  

    public static int tileSize = 25;


    static {
        try {
            playerImg = ImageIO.read(new File("images/player.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }  
    public Screen(){
        this.setLayout(null);
        this.setFocusable(true);
        addKeyListener(this);

        map = new MyHashTable<>();

        isNearLandmark=false;
        markValue=0;


        try {
            Scanner scan = new Scanner(new FileReader("PA_map.txt"));             
            int row=0; 
            while (scan.hasNextLine()){
                String line = scan.nextLine();

                
                String[] numberArray = line.split(" ");  
                for(int col=0;col<numberArray .length;col++){  
                    Location key = new Location(row,col);
                    int value = Integer.parseInt(numberArray[col]);
                    map.put(key, new MapObject(value));
                }
                row++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        boat1 = new Boat(map,49,91);
        boat2 = new Boat(map,52,48);
        Thread boatThread1 = new Thread(boat1);
        Thread boatThread2 = new Thread(boat2);
        boatThread1.start();
        boatThread2.start();

        deer1 = new Deer(map,57,36);
        deer2 = new Deer(map,52,75);
        Thread deerThread1 = new Thread(deer1);
        Thread deerThread2 = new Thread(deer2);
        deerThread1.start();
        deerThread2.start();

        bunny1 = new Bunny(map,77,44);
        bunny2 = new Bunny(map,29,70);
        Thread bunnyThread1 = new Thread(bunny1);
        Thread bunnyThread2 = new Thread(bunny2);
        bunnyThread1.start();
        bunnyThread2.start();

        frog1 = new Frog(map,48,45);
        frog2 = new Frog(map,54,16);
        Thread frogThread1 = new Thread(frog1);
        Thread frogThread2 = new Thread(frog2);
        frogThread1.start();
        frogThread2.start();

        Animate a = new Animate(this);
        Thread animThread = new Thread(a);
        animThread.start();

    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(viewCols * tileSize, viewRows * tileSize);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        DLList<Location> keys = map.keySet().toLinkedList();
        for (int i = 0; i < keys.size(); i++) {
            Location loc = keys.get(i);
            
            int r = loc.getRow();
            int c = loc.getCol();
            
            if (r < cameraRow || r >= cameraRow + viewRows) continue;
            if (c < cameraCol || c >= cameraCol + viewCols) continue;
            
            int screenRow = r - cameraRow;
            int screenCol = c - cameraCol;
            
            DLList<MapObject> objs = map.get(loc);
            if (objs != null) {
                for (int j = 0; j < objs.size(); j++) {
                    MapObject mo = objs.get(j);
                    mo.draw(g, screenRow, screenCol);
                }
            }
        }

        boat1.draw(g,cameraRow,cameraCol);
        boat2.draw(g,cameraRow,cameraCol);

        deer1.draw(g,cameraRow,cameraCol);
        deer2.draw(g,cameraRow,cameraCol);

        bunny1.draw(g,cameraRow,cameraCol);
        bunny2.draw(g,cameraRow,cameraCol);

        frog1.draw(g,cameraRow,cameraCol);
        frog2.draw(g,cameraRow,cameraCol);


        int playerScreenRow = playerRow - cameraRow;
        int playerScreenCol = playerCol - cameraCol;
        g.setColor(Color.red);
        g.drawImage(playerImg, playerScreenCol * tileSize, playerScreenRow * tileSize, tileSize, tileSize, null);

    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W && map.get(new Location(playerRow-1, playerCol)).get(0).value() <= 2) playerRow--;
        if (key == KeyEvent.VK_S && map.get(new Location(playerRow+1, playerCol)).get(0).value() <= 2) playerRow++;
        if (key == KeyEvent.VK_A && map.get(new Location(playerRow, playerCol-1)).get(0).value() <= 2) playerCol--;
        if (key == KeyEvent.VK_D&& map.get(new Location(playerRow, playerCol+1)).get(0).value() <= 2) playerCol++;
        if (key == KeyEvent.VK_P) System.out.println("Row: "+playerRow+" - Col: "+playerCol+"\nNear Landmark: "+isNearLandmark+" - markValue: "+markValue);

        isNearLandmark = false;
        for(int r=-1; r<=1; r++){
            for(int c=-1; c<=1; c++){
                if(r==0 && c==0) continue;
                int value = map.get(new Location(playerRow+r, playerCol+c)).get(0).value();
                if(value > 6){
                    isNearLandmark = true;
                    markValue = value;
                    break;
                }
                
            }
        }

        playerRow = Math.clamp(playerRow, 0, 99);
        playerCol = Math.clamp(playerCol,0,99);

        cameraRow = playerRow - viewRows / 2;
        cameraCol = playerCol - viewCols / 2;

        cameraRow = Math.clamp(cameraRow, 0, 100-viewRows);
        cameraCol = Math.clamp(cameraCol,0,100-viewCols);

        repaint();
    }
    @Override
    public void keyReleased(KeyEvent e) {}
    
}
