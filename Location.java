public class Location {
    private int row,col;

    public Location(int r, int c){
        row=r; col=c;
    }
    public int hashCode(){
        return row * 100 + col;
    }
    @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Location)) return false;
            Location other = (Location) o;
            return row == other.row && col == other.col;
        }
    public int getRow(){return row; }
    public int getCol(){ return col; }
}
