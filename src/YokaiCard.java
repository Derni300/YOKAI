public class YokaiCard {
    private Position position;
    private char color;

    public YokaiCard(char color, Position position) {
        this.color = color;
        this.position = position;
    }

    public char getCardName() {return color;}

    public Position getPosition() {return position;}
    public void setPosition(Position position) {this.position = position;}
}
