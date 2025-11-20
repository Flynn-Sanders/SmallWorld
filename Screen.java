import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import javax.swing.JPanel;

public class Screen extends JPanel implements KeyListener {
    private MyHashTable<Location,MapObject> map;


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
        return new Dimension(1920,1080);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        DLList<Location> keys = map.keySet().toLinkedList();
        for (int i = 0; i < keys.size(); i++) {
            Location loc = keys.get(i);
            DLList<MapObject> objs = map.get(loc);
            if (objs != null) {
                for (int j = 0; j < objs.size(); j++) {
                    MapObject mo = objs.get(j);
                    mo.draw(g, loc.getCol(), loc.getRow());
                }
            }
        }   
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
    
}
