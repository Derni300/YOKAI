public class Cell {
    private YokaiCard yokaiCard;
    private YokaiClue clueCard;
    private Position position;

    public Cell(YokaiCard yokaiCard, YokaiClue clueCard, Position position) {
        this.yokaiCard = yokaiCard;
        this.clueCard = clueCard;
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
    public void setPosition(Position position) {this.position = position;}

    public YokaiCard getYokaiCard() {
        return yokaiCard;
    }

    public YokaiClue getClueCard() {
        return clueCard;
    }
}
