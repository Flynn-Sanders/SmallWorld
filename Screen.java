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


        try {
            Scanner scan = new Scanner(new FileReader("MapExportFile.txt"));             
            int row=0; //Each line in your text file is a row
            while (scan.hasNextLine()){
                //Reads in one line at a time.
                String line = scan.nextLine();

                //if there's a space between each number in your text file
                String[] numberArray = line.split(" ");  
                for(int col=0;col<numberArray .length;col++){  //Each number in the line is a column
                    Location key = new Location(row,col);
                    String value = numberArray[col];
                    //Based upon the value, create your mapObject
                    //Put your Location key and mapObject in your Hash Table
                }
                row++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(800,600);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
    
}
