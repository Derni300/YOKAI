import java.util.*;

public class YokaiGame {
    private Player[] players;
    int n = 6;
    public Cell[][] cardBoard = new Cell[n][n];
    public Cell[][] clueBoard = new Cell[n][n];

    public void play() {
        createPlayers();
        initialiseBoard();
        Stack<String> clueStack = setCardBoard(players.length);
        printCardBoard();
        printClueBoard();
        showTwoCards();
        moveOneCard();
        printCardBoard();
        drawClueCard(clueStack);
    }

    private void createPlayers() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Veuillez entrer le nombre de joueurs");
        int playerNumber = scanner.nextInt();
        players = new Player[playerNumber];
        for (int i = 0; i < playerNumber; i++) {
            players[i] = new Player(scanner.nextLine());
            System.out.println("Nom du joueur " + (i + 1));
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

    private void initialiseBoard() {
        char[] cardList = {'R', 'R', 'R', 'R', 'B', 'B', 'B', 'B', 'P', 'P', 'P', 'P', 'G', 'G', 'G', 'G'};
        shuffle(cardList);

        int count = 0;
        for (int i = (n / 2 - 2); i < (n / 2 + 2); i++) {
            for (int j = (n / 2 - 2); j < (n / 2 + 2); j++) {
                Position positionCard = new Position(i, j);
                YokaiCard card = new YokaiCard(cardList[count], positionCard);
                cardBoard[i][j] = new Cell(card, null, positionCard);
                count += 1;
            }
        }
    }

    private Stack<String> setCardBoard(int playerNumber) {

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

        Collections.shuffle(colorList);
        Collections.shuffle(duoColorList);
        Collections.shuffle(triColorList);

        List<String> clueList = new ArrayList<>();
        Stack<String> clueStack = new Stack<>();

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

        for (String s : clueList) {
            clueStack.push(s);
        }

        return (clueStack);
    }

    private void printCardBoard() {
        System.out.println();
        System.out.println(" ".repeat(3 * (n / 2) - 4) + "[Card Board]" + " ".repeat(3 * (n / 2) - 4));
        System.out.println("╭ " + " ─ ".repeat(n) + " ╮");
        for (int i = 0; i < n; i++) {
            System.out.print("|  ");
            for (int j = 0; j < n; j++) {
                if (cardBoard[i][j] == null) {
                    System.out.print("·  ");
                } else {
                    YokaiCard card = cardBoard[i][j].getYokaiCard();
                    System.out.print(card.getCardName() + "  ");
                }
            }
            System.out.print("|");
            System.out.println();
        }
        System.out.println("╰ " + " ─ ".repeat(n) + " ╯");
        System.out.println();
    }

    private void printClueBoard() {
        System.out.println(" ".repeat(3 * (n / 2) - 4) + "[Clue Board]" + " ".repeat(3 * (n / 2) - 4));
        System.out.println("╭ " + " ─ ".repeat(n) + " ╮");
        for (int i = 0; i < n; i++) {
            System.out.print("|  ");
            for (int j = 0; j < n; j++) {
                if (clueBoard[i][j] == null) {
                    System.out.print("·  ");
                } else {
                    YokaiClue clue = clueBoard[i][j].getClueCard();
                    System.out.print(clue.getClueName() + "  ");
                }
            }
            System.out.print("|");
            System.out.println();
        }
        System.out.println("╰ " + " ─ ".repeat(n) + " ╯");
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
            } while (((x >= n) || (y >= n)) || cardBoard[x][y] == null);

            System.out.println("Carte " + i + " : " + cardBoard[x][y].getYokaiCard().getCardName());
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
        } while (((x >= n) || (y >= n)) || cardBoard[x][y] == null);
        System.out.println();

        int i, j;

        do {
            System.out.println("Veuillez sélectionner des coordonnées valides où déplacer la carte : ");
            System.out.println("Coordonnées en x de la carte :");
            i = scanner.nextInt();
            System.out.println("Coordonnées en y de la carte :");
            j = scanner.nextInt();
        } while (((i >= n) || (j >= n)) || !(cardBoard[i][j] == null) || !isValidMove(cardBoard[x][y].getYokaiCard()));

        Cell temp = cardBoard[x][y];
        cardBoard[x][y] = null;
        cardBoard[i][j] = temp;
    }

    private boolean isValidMove(YokaiCard card) {
        Position cardPosition = card.getPosition();
        boolean gauche, droite, haut, bas;
        if (cardPosition.getRow() >= 0) {
            gauche = cardBoard[cardPosition.getRow()][cardPosition.getColumn() - 1] != null;
            haut = cardBoard[cardPosition.getRow() - 1][cardPosition.getColumn()] != null;
        } else {
            gauche = false;
            haut = false;
        }
        if (cardPosition.getRow() <= n) {
            droite = cardBoard[cardPosition.getRow()][cardPosition.getColumn() + 1] != null;
            bas = cardBoard[cardPosition.getRow() + 1][cardPosition.getColumn()] != null;
        } else {
            droite = false;
            bas = false;
        }
        return (gauche || droite || haut || bas);
    }

    private void drawClueCard(Stack<String> clueStack) {
        Scanner scanner = new Scanner(System.in);
        String clueCard = clueStack.pop();
        System.out.println("Indice pioché : " + clueCard);
        System.out.println("Choisissez si vous voulez mettre l'indice de côter (tap 0) ou le jouer (tap 1)");
        int choice = scanner.nextInt();
        switch (choice) {
            case 0:
                System.out.println("L'indice est mis sur le côté");
            case 1:
                System.out.println("Choisissez ou poser l'indice : ");
        }
    }
}