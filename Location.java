public class Location {
    private int row,col;

    public Location(int r, int c){
        row=r; col=c;
    }
    public int hashCode(){
        return row * 125 + col;
    }
    public boolean equals(Object o){
        int x = ((Location)o).getRow();
        int y = ((Location)o).getCol();
        return row==x && col==y;
    }
    public int getRow(){return row; }
    public int getCol(){ return col; }
}
