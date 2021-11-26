import java.util.Random;

public class Game {
    private Player[] players;
    public Cell[][] board = new Cell[6][6];

    public void play() {
        initialiseBoard();
        printBoard();
    }

    private void createPlayers() {
        //Scanner scanner = new Scanner(System.in);
        //System.out.println("Veuillez entrer le nom des joueurs");
        players = new Player[2];
        players[0] = new Player("Bob");
        players[1] = new Player("Fanta");
    }

    private void shuffle(char t[]) {
        Random random = new Random();
        for (int i = 0; i < t.length; i++) {
            int r = (int) random.nextInt(t.length);
            char tmp = t[i];
            t[i] = t[r];
            t[r] = tmp;
        }
    }

    private void initialiseBoard() {
        char[] cardList = {'R', 'R', 'R', 'R', 'B', 'B', 'B', 'B', 'P', 'P', 'P', 'P', 'G', 'G', 'G', 'G'};
        shuffle(cardList);
        //System.out.println(cardList);
        int count = 0;
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                Position positionCard = new Position(i, j);
                Card card = new Card(cardList[count], positionCard);
                board[i][j] = new Cell(card, positionCard);
                count += 1;
            }
        }

    }

    private void printBoard() {
        System.out.println("+  -  -  -  -  -  -  +");
        for (int i = 0; i < 6; i++) {
            System.out.print("|  ");
            for (int j = 0; j < 6; j++) {
                if (board[i][j] == null) {
                    System.out.print(".  ");
                } else {
                    Card cardName = board[i][j].getCard();
                    System.out.print(cardName.getName() + "  ");
                }
            }
            System.out.print("|");
            System.out.println();
        }
        System.out.println("+  -  -  -  -  -  -  +");
    }
}
