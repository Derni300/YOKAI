public class Cell {
    private YokaiCard card;
    private Position position;

    public Cell(YokaiCard card, Position position) {
        this.card = card;
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public YokaiCard getCard() {
        return card;
    }
}
