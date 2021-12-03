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

        String[] colorSet = {"R", "B", "P", "G"};
        List<String> colorList = new ArrayList<>(Arrays.asList(colorSet));

        List<String> duoColorList = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            for (int j = 0; j <= 3; j++) {
                if (i != j) {
                    duoColorList.add(colorSet[i] + colorSet[j]);
                }
            }
        }

        //System.out.println(duoColorList);

        List<String> triColorList = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            for (int j = 0; j <= 3; j++) {
                for (int k = 0; k <= 3; k++) {
                    if (i != j && j != k && i != k) {
                        triColorList.add(colorSet[i] + colorSet[j] + colorSet[k]);
                    }
                }
            }
        }

        //System.out.println(triColorList);

        Collections.shuffle(colorList);
        Collections.shuffle(duoColorList);
        Collections.shuffle(triColorList);

        //System.out.println(colorList);
        //System.out.println(duoColorList);
        //System.out.println(triColorList);

        List<String> clueList = new ArrayList<>();
        Stack<String> clueStack = new Stack<String>();

        switch (playerNumber) {
            case 2:
                for (int i = 0; i <= 6; i++) {
                    if (i <= 1) {
                        clueList.add(colorList.get(i));
                    }
                    else if (i <= 4) {
                        clueList.add(duoColorList.get(i));
                    }
                    else {
                        clueList.add(triColorList.get(i));
                    }
                }
                Collections.shuffle(clueList);
        }

        //System.out.println(clueList);

        for (int i = 0; i < clueList.size(); i++) {
            clueStack.push(clueList.get(i));
        }

        System.out.println(clueStack);
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
