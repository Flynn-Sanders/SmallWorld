import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Screen extends JPanel implements KeyListener {
    private MyHashTable<Location, MapObject> map;
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

    private int cameraRow = playerRow - viewRows / 2;
    private int cameraCol = playerCol - viewCols / 2;

    public static int tileSize = 25;

    private static BufferedImage mark1,mark2,mark3,mark4,mark5;

    private Font titleFont = new Font("Arial", Font.BOLD, 17);
    private Font subscript = new Font("Arial", Font.BOLD, 11);

    static {
        try {
            playerImg = ImageIO.read(new File("images/player.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
                mark1 = ImageIO.read(new File("images/mark7.png")); //flight 93 national memorial
                mark2 = ImageIO.read(new File("images/mark8.png")); //valley forge national historic park
                mark3 = ImageIO.read(new File("images/mark9.png")); //Phipps Conservatory and Botanical Gardens
                mark4 = ImageIO.read(new File("images/mark10.png")); // steamtown national historic site
                mark5 = ImageIO.read(new File("images/mark11.png")); //falling water
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Screen() {
        setLayout(null);
        setFocusable(true);
        addKeyListener(this);

        map = new MyHashTable<>();

        isNearLandmark = false;
        markValue = 0;

        try {
            Scanner scan = new Scanner(new FileReader("PA_map.txt"));
            int row = 0;
            while (scan.hasNextLine() && row < 100) {
                String line = scan.nextLine();
                String[] numberArray = line.split(" ");
                for (int col = 0; col < numberArray.length && col < 100; col++) {
                    Location key = new Location(row, col);
                    int value = Integer.parseInt(numberArray[col]);
                    map.put(key, new MapObject(value));
                }
                row++;
            }
            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        loadGame("saveFile.txt");

        new Thread(boat1).start();
        new Thread(boat2).start();

        new Thread(deer1).start();
        new Thread(deer2).start();

        new Thread(bunny1).start();
        new Thread(bunny2).start();

        new Thread(frog1).start();
        new Thread(frog2).start();

        Animate a = new Animate(this);
        new Thread(a).start();

        System.out.println("P to save...");
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(viewCols * tileSize, viewRows * tileSize);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        updateCamera();

        int startR = cameraRow;
        int endR = cameraRow + viewRows - 1;
        int startC = cameraCol;
        int endC = cameraCol + viewCols - 1;

        for (int r = startR; r <= endR; r++) {
            for (int c = startC; c <= endC; c++) {
                Location loc = new Location(r, c);
                DLList<MapObject> objs = map.get(loc);
                int screenRow = r - cameraRow;
                int screenCol = c - cameraCol;
                if (objs != null && !objs.isEmpty()) {
                    for (int j = 0; j < objs.size(); j++) {
                        MapObject mo = objs.get(j);
                        mo.draw(g, screenRow, screenCol);
                    }
                } else {
                    int x = screenCol * tileSize;
                    int y = screenRow * tileSize;
                    g.setColor(new Color(0, 0, 0));
                    g.fillRect(x, y, tileSize, tileSize);
                }
            }
        }

        boat1.draw(g, cameraRow, cameraCol);
        boat2.draw(g, cameraRow, cameraCol);

        deer1.draw(g, cameraRow, cameraCol);
        deer2.draw(g, cameraRow, cameraCol);

        bunny1.draw(g, cameraRow, cameraCol);
        bunny2.draw(g, cameraRow, cameraCol);

        frog1.draw(g, cameraRow, cameraCol);
        frog2.draw(g, cameraRow, cameraCol);

        int playerScreenRow = playerRow - cameraRow;
        int playerScreenCol = playerCol - cameraCol;
        g.drawImage(playerImg, playerScreenCol * tileSize, playerScreenRow * tileSize, tileSize, tileSize, null);

        if(isNearLandmark){
            
            BufferedImage img = null;
            String title ="", desc= "";
            switch (markValue) {
                case 7: // flight 93
                    img=mark1;
                    title = "Flight 93 National Memorial";
                    desc="A memorial honoring the\npassengers and crew of Flight 93 who\ncourageously resisted the\nhijackers on September 11, 2001.";
                    break;
                case 8: //valley frge national historic park
                    img=mark2;
                    title = "Valley Forge National\nHistoric Park";
                    desc="The site of the Continental Army's\n1777-1778 winter encampment, where \nWashington's troops endured\nhardship and emerged stronger.";
                    break;
                case 9: //Phipps Conservatory and Botanical Gardens
                    img=mark3;
                    title = "Phipps Conservatory and\nBotanical Gardens";
                    desc="A vast Victorian glasshouse filled\nwith exotic plants, seasonal flower shows,\nand beautifully designed garden spaces.";
                    break;
                case 10: //steamtown national historic site
                    img=mark4;
                    title = "Steamtown National Historic Site";
                    desc="A museum and rail yard\ndedicated to the history of\nsteam locomotives,featuring \nrestored trains,tours,\nand working engines.";
                    break;
                case 11: //fallingwater
                    img=mark5;
                    title = "Fallingwater";
                    desc="Frank Lloyd Wright's iconic house\nbuilt over a waterfall, celebrated for its\norganic architecture and\nharmony with nature.";
                    break;
            
                default:
                    break;
            }
            drawInfo(img, title, desc, g);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        int targetR = playerRow;
        int targetC = playerCol;

        if (key == KeyEvent.VK_W) targetR--;
        if (key == KeyEvent.VK_S) targetR++;
        if (key == KeyEvent.VK_A) targetC--;
        if (key == KeyEvent.VK_D) targetC++;

        if (inBounds(targetR, targetC) && canWalkTo(targetR, targetC)) {
            playerRow = targetR;
            playerCol = targetC;
        }

        if (key == KeyEvent.VK_P){
            System.out.println("Saving...");
            saveGame("saveFile.txt");
            System.out.println("Game Saved");
        } 

        isNearLandmark = false;
        markValue = 0;
        outer:
        for (int r = -1; r <= 1; r++) {
            for (int c = -1; c <= 1; c++) {
                if (r == 0 && c == 0) continue;
                int rr = playerRow + r;
                int cc = playerCol + c;
                if (!inBounds(rr, cc)) continue;
                DLList<MapObject> list = map.get(new Location(rr, cc));
                if (list == null || list.isEmpty()) continue;
                int value = list.get(0).value();
                if (value >= 7 && value <= 11) {
                    isNearLandmark = true;
                    markValue = value;
                    break outer;
                }
            }
        }

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    private boolean canWalkTo(int r, int c) {
        DLList<MapObject> list = map.get(new Location(r, c));
        if (list == null || list.isEmpty()) return false;
        int value = list.get(0).value();
        return value <= 2;
    }

    private void drawInfo(BufferedImage img, String title, String desc, Graphics g){
        g.setColor(Color.white);
        int x = 440; int y = 150; int w = 300; int h = 450;
        g.fillRect(x, y, w, h);

        
        g.drawImage(img, x+25, y+50,250,250,null);

        g.setColor(Color.black);
        g.setFont(titleFont);
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(title);
        int cx = x + (w - textWidth) / 2;
        String[] titleLines = title.split("\n");
        int titleLineHeight = fm.getHeight();
        if(title.contains("\n")){
            for (int i = 0; i < titleLines.length; i++) {
                textWidth = fm.stringWidth(titleLines[i]);
                int drawX = x + (w - textWidth) / 2; 
                g.drawString(titleLines[i], drawX, y+20 + i*(titleLineHeight+5));
            }
        }
        else g.drawString(title, cx , y+35);
        
    
        g.setFont(subscript);
        fm=g.getFontMetrics();
        String[] lines = desc.split("\n");
        int lineHeight = fm.getHeight();

        for (int i = 0; i < lines.length; i++) {
            textWidth = fm.stringWidth(lines[i]);
            int drawX = x + (w - textWidth) / 2; 
            g.drawString(lines[i], drawX, y+350 + i*(lineHeight+5));
        }
    }

    private boolean inBounds(int r, int c) {
        return r >= 0 && r < 100 && c >= 0 && c < 100;
    }

    private void updateCamera() {
        cameraRow = playerRow - viewRows / 2;
        cameraCol = playerCol - viewCols / 2;
        cameraRow = clamp(cameraRow, 0, 100 - viewRows);
        cameraCol = clamp(cameraCol, 0, 100 - viewCols);
    }
    public void saveGame(String filename) {
        try (PrintWriter out = new PrintWriter(filename)) {
            out.println("PLAYER " + playerRow + " " + playerCol);

            out.println("BOAT " + boat1.row + " " + boat1.col);
            out.println("BOAT " + boat2.row + " " + boat2.col);

            out.println("DEER " + deer1.row + " " + deer1.col);
            out.println("DEER " + deer2.row + " " + deer2.col);

            out.println("BUNNY " + bunny1.row + " " + bunny1.col);
            out.println("BUNNY " + bunny2.row + " " + bunny2.col);

            out.println("FROG " + frog1.row + " " + frog1.col);
            out.println("FROG " + frog2.row + " " + frog2.col);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGame(String filename) {
    try (Scanner scan = new Scanner(new File(filename))) {
        while(scan.hasNextLine()) {
            String[] parts = scan.nextLine().split(" ");
            String type = parts[0];
            int r = Integer.parseInt(parts[1]);
            int c = Integer.parseInt(parts[2]);

            switch(type) {
                case "PLAYER":
                    playerRow = r;
                    playerCol = c;
                    break;
                case "BOAT":
                    if(boat1 == null) boat1 = new Boat(map,r,c);
                    else if(boat2 == null) boat2 = new Boat(map,r,c);
                    break;
                case "DEER":
                    if(deer1 == null) deer1 = new Deer(map,r,c);
                    else if(deer2 == null) deer2 = new Deer(map,r,c);
                    break;
                case "BUNNY":
                    if(bunny1 == null) bunny1 = new Bunny(map,r,c);
                    else if(bunny2 == null) bunny2 = new Bunny(map,r,c);
                    break;
                case "FROG":
                    if(frog1 == null) frog1 = new Frog(map,r,c);
                    else if(frog2 == null) frog2 = new Frog(map,r,c);
                    break;
            }
        }
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
}

    private int clamp(int v, int min, int max) {
        if (v < min) return min;
        if (v > max) return max;
        return v;
    }
}
