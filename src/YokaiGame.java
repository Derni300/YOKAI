import java.util.*;

public class YokaiGame {
    private Player[] players;
    public Cell[][] board = new Cell[6][6];

    public void play() {
        createPlayers();
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

    private void shuffle(char[] cardList) {
        Random random = new Random();
        for (int i = 0; i < cardList.length; i++) {
            int r = random.nextInt(cardList.length);
            char temp = cardList[i];
            cardList[i] = cardList[r];
            cardList[r] = temp;
        }
    }

    private void initialiseBoard() {
        Random random = new Random();
        char[] cardList = {'R', 'R', 'R', 'R', 'B', 'B', 'B', 'B', 'P', 'P', 'P', 'P', 'G', 'G', 'G', 'G'};
        shuffle(cardList);
        //System.out.println(cardList);
        int count = 0;
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                Position positionCard = new Position(i, j);
                YokaiCard card = new YokaiCard(cardList[count], positionCard);
                board[i][j] = new Cell(card, positionCard);
                count += 1;
            }
        }

        int playerNumber = 2;

        String[] colorList = {"R", "B", "P", "G"};

        List<String> duoColorList = new ArrayList<String>();
        for (int i = 0; i <= 3; i++) {
            for (int j = 0; j <= 3; j++) {
                if (i != j) {
                    duoColorList.add(colorList[i] + colorList[j]);
                }
            }
        }

        System.out.println(duoColorList);

        List<String> triColorList = new ArrayList<String>();
        for (int i = 0; i <= 3; i++) {
            for (int j = 0; j <= 3; j++) {
                for (int k = 0; k <= 3; k++) {
                    if (i != j && j != k) {
                        triColorList.add(colorList[i] + colorList[j] + colorList[k]);
                    }
                }
            }
        }

        System.out.println(triColorList);

        List<String> clueList = new ArrayList<String>();

        switch (playerNumber) {
            case 2:
                for (int i = 0; i <= 7; i++) {
                    if (i <= 2) {
                        int id = random.nextInt(colorList.length);
                        clueList.add(colorList[id]);
                    } else if (i <= 5) {
                        for (int j = 0; j <= 1; j++) {
                            int id = random.nextInt(colorList.length);
                        }

                        clueList.add("");
                    } else {
                        int id = random.nextInt(colorList.length);
                    }
                }
        }

        Stack<String> clueStack = new Stack<String>();
    }

    private void printBoard() {
        System.out.println();
        System.out.println("+  -  -  -  -  -  -  +");
        for (int i = 0; i < 6; i++) {
            System.out.print("|  ");
            for (int j = 0; j < 6; j++) {
                if (board[i][j] == null) {
                    System.out.print("¤  ");
                } else {
                    YokaiCard cardName = board[i][j].getCard();
                    System.out.print(cardName.getName() + "  ");
                }
            }
            System.out.print("|");
            System.out.println();
        }
        System.out.println("+  -  -  -  -  -  -  +");
    }
}
