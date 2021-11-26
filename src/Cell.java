public class Cell {
    private Card card;
    private Position position;

    public Cell(Card card, Position position) {
        this.card = card;
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public Card getCard(){
        return card;
    }
}
