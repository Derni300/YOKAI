public class YokaiCard {
    private Position position;
    private char color;

    public YokaiCard(char color, Position position) {
        this.color = color;
        this.position = position;
    }

    public char getName() {
        return color;
    }
}
