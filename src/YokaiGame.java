import java.util.*;

public class YokaiGame {
    private Player[] players;
    int n = 6;
    public Cell[][] board = new Cell[n][n];

    public void play() {
        createPlayers();
        initialiseBoard(players.length);
        printBoard();
        showTwoCards();
        moveOneCard();
        printBoard();
    }

    private void createPlayers() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Veuillez entrer le nombre de joueurs");
        int playerNumber = scanner.nextInt();
        players = new Player[playerNumber];
        for (int i = 0; i < playerNumber; i++) {
            players[i] = new Player(scanner.nextLine());
            System.out.println("Nom du joueur " + i);
        }
        players[playerNumber - 1] = new Player(scanner.nextLine());
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

    private void initialiseBoard(int playerNumber) {
        Random random = new Random();
        char[] cardList = {'R', 'R', 'R', 'R', 'B', 'B', 'B', 'B', 'P', 'P', 'P', 'P', 'G', 'G', 'G', 'G'};
        shuffle(cardList);

        //System.out.println(cardList);

        int count = 0;
        for (int i = (n/2 - 2); i < (n/2 + 2); i++) {
            for (int j = (n/2 - 2); j < (n/2 + 2); j++) {
                Position positionCard = new Position(i, j);
                YokaiCard card = new YokaiCard(cardList[count], positionCard);
                board[i][j] = new Cell(card, positionCard);
                count += 1;
            }
        }

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
                    } else if (i <= 4) {
                        clueList.add(duoColorList.get(i));
                    } else {
                        clueList.add(triColorList.get(i));
                    }
                }
                Collections.shuffle(clueList);
            case 3:
                for (int i = 0; i <= 8; i++) {
                    if (i <= 1) {
                        clueList.add(colorList.get(i));
                    } else if (i <= 5) {
                        clueList.add(duoColorList.get(i));
                    } else {
                        clueList.add(triColorList.get(i));
                    }
                }
                Collections.shuffle(clueList);
            case 4:
                for (int i = 0; i <= 9; i++) {
                    if (i <= 2) {
                        clueList.add(colorList.get(i));
                    } else if (i <= 6) {
                        clueList.add(duoColorList.get(i));
                    } else {
                        clueList.add(triColorList.get(i));
                    }
                }
                Collections.shuffle(clueList);
        }

        //System.out.println(clueList);

        for (int i = 0; i < clueList.size(); i++) {
            clueStack.push(clueList.get(i));
        }

        //System.out.println(clueStack);
    }

    private void printBoard() {
        System.out.println();
        System.out.println("+ " + " - ".repeat(n) + " +");
        for (int i = 0; i < n; i++) {
            System.out.print("|  ");
            for (int j = 0; j < n; j++) {
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
        System.out.println("+ " + " - ".repeat(n) + " +");
        System.out.println();
    }

    private void showTwoCards() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Entrer les coordonnées des deux cartes à regarder :");
        System.out.println();

        for (int i = 1; i <= 2; i++) {
            int x, y;
            do {
                System.out.println("Veuillez sélectionner des coordonnées valides pour la carte " + i);
                System.out.println("Coordonnées en x de la carte " + i + " :");
                x = scanner.nextInt();
                System.out.println("Coordonnées en y de la carte " + i + " :");
                y = scanner.nextInt();
            } while (((x >= n) || (y >= n)) || board[x][y] == null);

            System.out.println("Carte " + i + " : " + board[x][y].getCard().getName());
            System.out.println();
        }
    }

    private void moveOneCard() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choisissez les coordonnées de la carte à déplacer :");
        System.out.println();
        int x, y;

        do {
            System.out.println("Veuillez sélectionner des coordonnées valides pour la carte à déplacer : ");
            System.out.println("Coordonnées en x de la carte :");
            x = scanner.nextInt();
            System.out.println("Coordonnées en y de la carte :");
            y = scanner.nextInt();
        } while (((x >= n) || (y >= n)) || board[x][y] == null);
        System.out.println();

        int i, j;

        do {
            System.out.println("Veuillez sélectionner des coordonnées valides où déplacer la carte : ");
            System.out.println("Coordonnées en x de la carte :");
            i = scanner.nextInt();
            System.out.println("Coordonnées en y de la carte :");
            j = scanner.nextInt();
        } while (((i >= n) || (j >= n)) || !(board[i][j] == null) || !isValidMove(board[x][y].getCard()));

        Cell temp = board[x][y];
        board[x][y] = null;
        board[i][j] = temp;
    }

    private boolean isValidMove(YokaiCard card) {
        Position cardPosition = card.getPosition();
        boolean gauche,droite,haut,bas;
        if (cardPosition.getRow() >= 0) {
            gauche = board[cardPosition.getRow()][cardPosition.getColumn() - 1] != null;
        } else {
            gauche = false;
        }
        if (cardPosition.getRow() >= n) {
            droite = board[cardPosition.getRow()][cardPosition.getColumn() + 1] != null;
        } else {
            droite = false;
        }
        if (cardPosition.getRow() >= 0) {
            haut = board[cardPosition.getRow() - 1][cardPosition.getColumn()] != null;
        } else {
            haut = false;
        }
        if (cardPosition.getRow() >= n) {
            bas = board[cardPosition.getRow() + 1][cardPosition.getColumn()] != null;
        } else {
            bas = false;
        }
        return (!gauche || !droite || !haut || !bas);
    }
}