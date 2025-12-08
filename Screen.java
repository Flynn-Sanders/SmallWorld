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


    static {
        try {
            playerImg = ImageIO.read(new File("images/player.png"));
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
