import java.awt.Point;

public class Location {
    private int row,col;

    public Location(int r, int c){
        row=r; col=c;
    }
    public int hashCode(){
        return row*10+col;
    }
    public boolean equals(Object o){
        int x = ((Location)o).getLocation().x;
        int y = ((Location)o).getLocation().y;
        return row==x && col==y;
    }
    public Point getLocation(){
        return new Point(row,col);
    } 
}
