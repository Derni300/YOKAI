public class Card {
    private Position position;
    private char color;

    public Card(char color, Position position) {
        this.color = color;
        this.position = position;
    }

    public char getName() {
        return color;
    }
}
