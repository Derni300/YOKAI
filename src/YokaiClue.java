public class YokaiClue {
    private Position position;
    private char[] color;

    public YokaiClue(char[] color, Position position) {
        this.color = color;
        this.position = position;
    }

    public char[] getClueName() {
        return color;
    }
}
