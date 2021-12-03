public class Position {
    private int row;
    private int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public String toString() {
        return this.row + "" + this.column;
    }

    public int getRow() {return row;}
    public int getColumn() {return column;}
}
