import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import javax.swing.JPanel;

public class Screen extends JPanel implements KeyListener {
    private MyHashTable<Location,MapObject> map;

    private int playerRow = 15;
    private int playerCol = 15;

    private int cameraRow = 0;
    private int cameraCol = 15;  
    private int viewCols = 15;  

    private int tileSize = 45;

    public Screen(){
        this.setLayout(null);
        this.setFocusable(true);
        addKeyListener(this);

        map = new MyHashTable<>();


        try {
            Scanner scan = new Scanner(new FileReader("PA_map.txt"));             
            int row=0; //Each line in your text file is a row
            while (scan.hasNextLine()){
                //Reads in one line at a time.
                String line = scan.nextLine();

                //if there's a space between each number in your text file
                String[] numberArray = line.split(" ");  
                for(int col=0;col<numberArray .length;col++){  //Each number in the line is a column
                    Location key = new Location(row,col);
                    int value = Integer.parseInt(numberArray[col]);
                    map.put(key, new MapObject(value));
                }
                row++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

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

        int playerScreenRow = playerRow - cameraRow;
        int playerScreenCol = playerCol - cameraCol;
        g.setColor(Color.red);
        g.fillOval(playerScreenCol * tileSize, playerScreenRow * tileSize, tileSize, tileSize);

    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) playerRow--;
        if (key == KeyEvent.VK_S) playerRow++;
        if (key == KeyEvent.VK_A) playerCol--;
        if (key == KeyEvent.VK_D) playerCol++;

        playerRow = Math.max(0, Math.min(playerRow, 99));
        playerCol = Math.max(0, Math.min(playerCol, 99));

        cameraRow = playerRow - viewRows / 2;
        cameraCol = playerCol - viewCols / 2;

        cameraRow = Math.max(0, Math.min(cameraRow, 100 - viewRows));
        cameraCol = Math.max(0, Math.min(cameraCol, 100 - viewCols));

        repaint();
    }
    @Override
    public void keyReleased(KeyEvent e) {}
    
}
