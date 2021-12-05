public class YokaiClue {
    private Position position;
    private String color;

    public YokaiClue(String color, Position position) {
        this.color = color;
        this.position = position;
    }

    public String getClueName() {
        return color;
    }
}
