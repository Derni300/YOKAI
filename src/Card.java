public class Card {
    private Position position;
    private char color;

    public Card(char color, Position position) {
        this.color = color;
        this.position = position;
    }

    public char getName(){
        return color;
    }

    /*
    public static class Builder {
        private Position position;
        private char color;

        public Builder(String owner, int token, int isepCoins) {
            this.position = position;
            this.color = color;
        }

        public Card build() {
            return new Card(this);
        }
    }
    */
}
