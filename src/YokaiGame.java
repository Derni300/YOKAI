import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

import java.util.*;

public class YokaiGame {
    int n = 6;
    List<Player> players = new ArrayList<>();
    int turn = 0;
    Cell[][] cardBoard = new Cell[n][n];
    Cell[][] clueBoard = new Cell[n][n];
    Stack<String> clueStack = new Stack<>();
    List<String> drawClueList = new ArrayList<>();

    public void play() {
        playSound();
        createPlayers();
        initialiseBoard();
        setCardBoard(players.size());
        do {
            nextTurn();
            printBoard(n, n);
            showTwoCards();
            moveOneCard();
            drawClueCard(clueStack);
        } while (yokaiAreAppeased());
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    private void createPlayers() {
        players = new ArrayList<>();
        Scanner scannerInt = new Scanner(System.in);
        Scanner scannerLine = new Scanner(System.in);
        System.out.println("Veuillez entrer le nombre de joueurs");
        int playerNumber = scannerInt.nextInt();
        for (int i = 0; i < playerNumber; i++) {
            System.out.println("Nom du joueur " + (i + 1));
            players.add(new Player(scannerLine.nextLine()));
        }
        System.out.println();
    }

    private void nextTurn() {
        System.out.println("Au tour de : " + players.get(turn).getName() + "\n");
        if (turn < players.size() - 1) {
            turn += 1;
        } else {
            turn = 0;
        }
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

    private void setCardBoard(int playerNumber) {

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
    }

    private void printBoard(int x, int y) {
        System.out.print("     ");
        for (int h = 0; h < n; h++) {
            System.out.print(h + "    ");
        }
        System.out.println();
        System.out.println("  ╭" + " ─".repeat(n * 3 - n / 2) + "╮");
        for (int i = 0; i < n; i++) {
            System.out.print(i + " │");
            for (int j = 0; j < n; j++) {
                if (cardBoard[i][j] == null) {
                    System.out.print("  ·  ");
                } else if (i == x && j == y) {
                    System.out.print(ANSI_GREEN + "  " + cardBoard[i][j].getYokaiCard().getCardName() + "  " + ANSI_RESET);
                } else if (clueBoard[i][j] != null) {
                    if (clueBoard[i][j].getClueCard().getClueName().length() == 1) {
                        System.out.print("  " + ANSI_RED + clueBoard[i][j].getClueCard().getClueName() + ANSI_RESET + "  ");
                    } else if (clueBoard[i][j].getClueCard().getClueName().length() == 2) {
                        System.out.print("  " + ANSI_RED + clueBoard[i][j].getClueCard().getClueName() + ANSI_RESET + " ");
                    } else {
                        System.out.print(" " + ANSI_RED + clueBoard[i][j].getClueCard().getClueName() + ANSI_RESET + " ");
                    }
                } else {
                    System.out.print(ANSI_BLUE + "  ■  " + ANSI_RESET);
                }
            }
            System.out.print("│");
            if (i < n - 1) {
                System.out.println("\n  │" + " ".repeat(n * 5) + "│");
            }
        }
        System.out.println("\n  ╰" + " ─".repeat(n * 3 - n / 2) + "╯");
        System.out.println();
    }

    private void showTwoCards() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Entrer les coordonnées des deux cartes à regarder :");
        System.out.println();

        for (int i = 1; i <= 2; i++) {
            int x;
            int y;
            do {
                System.out.println("Veuillez sélectionner des coordonnées valides pour la carte " + i + "\n");
                System.out.println("Coordonnées en x ∈ [0;" + (n - 1) + "] de la carte " + i + " :");
                x = scanner.nextInt();
                System.out.println("Coordonnées en y ∈ [0;" + (n - 1) + "] de la carte " + i + " :");
                y = scanner.nextInt();
            } while ((x >= n || y >= n) || (x < 0 || y < 0) || cardBoard[x][y] == null);
            printBoard(x, y);
            System.out.println("Carte " + i + " : " + cardBoard[x][y].getYokaiCard().getCardName());
            System.out.println();
        }
    }

    private void moveOneCard() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choisissez les coordonnées de la carte à déplacer :");
        int x, y;

        do {
            System.out.println("\nVeuillez sélectionner des coordonnées valides pour la carte à déplacer :");
            System.out.println("Coordonnées en x ∈ [0;" + (n - 1) + "] de la carte :");
            x = scanner.nextInt();
            System.out.println("Coordonnées en y ∈ [0;" + (n - 1) + "] de la carte :");
            y = scanner.nextInt();
        } while ((x >= n || y >= n) || (x < 0 || y < 0) || cardBoard[x][y] == null || clueBoard[x][y] != null);

        int i, j;
        boolean test;

        Cell temp = cardBoard[x][y];
        cardBoard[x][y] = null;

        printBoard(n, n);

        do {
            System.out.println("Veuillez sélectionner des coordonnées valides où déplacer la carte :\n");
            System.out.println("Coordonnées en x ∈ [0;" + (n - 1) + "] de la carte :");
            i = scanner.nextInt();
            System.out.println("Coordonnées en y ∈ [0;" + (n - 1) + "] de la carte :");
            j = scanner.nextInt();
            if (i < n && j < n) {
                if (cardBoard[i][j] == null) {
                    cardBoard[i][j] = temp;
                    cardBoard[i][j].setPosition(new Position(i, j));
                    cardBoard[i][j].getYokaiCard().setPosition(new Position(i, j));

                    test = (i >= n || j >= n) || isValidMove(cardBoard[i][j]);
                    if (test) {
                        cardBoard[i][j] = null;
                    }
                } else {
                    test = true;
                }
            } else {
                test = true;
            }
        } while (test);
        printBoard(n, n);
    }

    private boolean isValidMove(Cell cell) {
        Position cellPosition = cell.getPosition();
        boolean gauche, droite, haut, bas;
        if (cellPosition.getRow() > 0) {
            haut = cardBoard[cellPosition.getRow() - 1][cellPosition.getColumn()] != null;
        } else {
            haut = false;
        }
        if (cellPosition.getColumn() > 0) {
            gauche = cardBoard[cellPosition.getRow()][cellPosition.getColumn() - 1] != null;
        } else {
            gauche = false;
        }
        if (cellPosition.getRow() < n - 1) {
            bas = cardBoard[cellPosition.getRow() + 1][cellPosition.getColumn()] != null;
        } else {
            bas = false;
        }
        if (cellPosition.getColumn() < n - 1) {
            droite = cardBoard[cellPosition.getRow()][cellPosition.getColumn() + 1] != null;
        } else {
            droite = false;
        }

        //System.out.println("gauche : " + gauche + " ,droite : " + droite + " ,haut : " + haut + " ,bas : " + bas);
        return (!(gauche || droite || haut || bas));
    }

    private void drawClueCard(Stack<String> clueStack) {
        Scanner scannerInt = new Scanner(System.in);
        String clueCard = clueStack.pop();
        System.out.println("Indice pioché : " + clueCard + "\n");
        System.out.println("Choisissez si vous voulez jouer l'indice '1', le mettre sur le côte '2' " +
                "ou jouer un indice de la réserve '3'\n");
        int choice;
        do {
            System.out.println("Sélectionner un nombre entre 1 et 3");
            choice = scannerInt.nextInt();
        } while (choice < 1 || choice > 3);
        System.out.println();

        int x, y;
        if (choice == 1) {
            System.out.println("Choisissez ou poser l'indice :\n");

            do {
                System.out.println("Veuillez sélectionner des coordonnées valides pour l'indice sélectionné à placer :\n");
                System.out.println("Coordonnées en x ∈ [0;" + (n - 1) + "] de la carte :");
                x = scannerInt.nextInt();
                System.out.println("Coordonnées en y ∈ [0;" + (n - 1) + "] de la carte :");
                y = scannerInt.nextInt();
            } while (((x >= n) || (y >= n)) || clueBoard[x][y] != null);

            Position positionClue = new Position(x, y);
            YokaiClue card = new YokaiClue(clueCard, positionClue);
            clueBoard[x][y] = new Cell(null, card, positionClue);

        } else if (choice == 2) {
            System.out.println("L'indice est mis sur le côté\n");
            drawClueList.add(clueCard);
            System.out.println("Liste des indices :\n");
            System.out.println(drawClueList);
            System.out.println();

        } else {
            drawClueList.add(clueCard);
            System.out.println("Liste des indices :\n");
            System.out.println(drawClueList + "\n");
            do {
                System.out.println("Sélectionner l'index de l'indice que vous voulez jouer :");
                choice = scannerInt.nextInt();
            } while (choice < 0 || choice >= drawClueList.size());
            String clueChoice = drawClueList.get(choice);
            System.out.println("\nIndice sélectionné : " + clueChoice);
            System.out.println("\nChoisissez ou poser l'indice sélectionné :\n");

            do {
                System.out.println("Veuillez sélectionner des coordonnées valides pour l'indice à placer :\n");
                System.out.println("Coordonnées en x ∈ [0;" + (n - 1) + "] de la carte :");
                x = scannerInt.nextInt();
                System.out.println("Coordonnées en y ∈ [0;" + (n - 1) + "] de la carte :");
                y = scannerInt.nextInt();
            } while (((x >= n) || (y >= n)) || clueBoard[x][y] != null);

            Position positionClue = new Position(x, y);
            YokaiClue card = new YokaiClue(clueChoice, positionClue);
            clueBoard[x][y] = new Cell(null, card, positionClue);
        }
    }

    private boolean yokaiAreAppeased() {
        Scanner scannerLine = new Scanner(System.in);
        printBoard(n,n);
        System.out.println("Voulez vous déclarer que les Yokais sont apaisés ? (Y/N)");
        String choice = scannerLine.nextLine();
        if (choice.equals("Y") || clueStack.size() == 0) {
            if (verification()) {
                System.out.println(score());
            }
            return false;
        } else {
            return true;
        }
    }

    private boolean verification() {
        System.out.println();
        boolean win = true;
        boolean gauche, droite, haut, bas;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (cardBoard[i][j] != null) {

                    if (i > 0) {
                        if (cardBoard[i - 1][j] != null) {
                            haut = cardBoard[i - 1][j].getYokaiCard().getCardName()
                                    == cardBoard[i][j].getYokaiCard().getCardName();
                        } else {
                            haut = false;
                        }
                    } else {
                        haut = false;
                    }

                    if (j > 0) {
                        if (cardBoard[i][j - 1] != null) {
                            gauche = cardBoard[i][j - 1].getYokaiCard().getCardName()
                                    == cardBoard[i][j].getYokaiCard().getCardName();
                        } else {
                            gauche = false;
                        }
                    } else {
                        gauche = false;
                    }

                    if (i < n - 1) {
                        if (cardBoard[i + 1][j] != null) {
                            bas = cardBoard[i + 1][j].getYokaiCard().getCardName()
                                    == cardBoard[i][j].getYokaiCard().getCardName();
                        } else {
                            bas = false;
                        }
                    } else {
                        bas = false;
                    }

                    if (j < n - 1) {
                        if (cardBoard[i][j + 1] != null) {
                            droite = cardBoard[i][j + 1].getYokaiCard().getCardName()
                                    == cardBoard[i][j].getYokaiCard().getCardName();
                        } else {
                            droite = false;
                        }
                    } else {
                        droite = false;
                    }

                    if (!(gauche || droite || haut || bas)) {
                        win = false;
                    }
                    //System.out.println("(" + i + "," + j + ") " + "gauche : " + gauche + " ,droite : " + droite + " ,haut : " + haut + " ,bas : " + bas);
                }
            }
        }
        if (win) {
            System.out.println("Victoire");
        } else {
            System.out.println("Défaite");
        }
        System.out.println();
        printCardBoard();
        return win;
    }

    private void printCardBoard() {
        System.out.print(" ".repeat(5));
        for (int h = 0; h < n; h++) {
            System.out.print(h + "    ");
        }
        System.out.println();
        System.out.println("  ╭" + " ─".repeat(n * 3 - n / 2) + "╮");
        for (int i = 0; i < n; i++) {
            System.out.print(i + " │");
            for (int j = 0; j < n; j++) {
                if (cardBoard[i][j] == null) {
                    System.out.print("  ·  ");
                } else {
                    System.out.print(ANSI_YELLOW + "  " + cardBoard[i][j].getYokaiCard().getCardName() + "  " + ANSI_RESET);
                }
            }
            System.out.print("│");
            if (i < n - 1) {
                System.out.println("\n  │" + " ".repeat(n * 5) + "│");
            }
        }
        System.out.println("\n  ╰" + " ─".repeat(n * 3 - n / 2) + "╯");
        System.out.println();
    }

    public void playSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("8-bit.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    public int score() {
        int finalScore = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (cardBoard[i][j] != null && clueBoard[i][j] != null) {
                    int count = 0;
                    for (int k = 0; k < clueBoard[i][j].getClueCard().getClueName().length(); k++) {
                        if (cardBoard[i][j].getYokaiCard().getCardName() == clueBoard[i][j].getClueCard().getClueName().charAt(k)) {
                            finalScore += 1;
                        } else {
                            count += 1;
                        }
                    }
                    if (count == clueBoard[i][j].getClueCard().getClueName().length()) {
                        finalScore -= 1;
                    }
                }
            }
        }

        finalScore += 2 * drawClueList.size();
        finalScore += 5 * clueStack.size();

        System.out.println("Vôtre score : " + finalScore);

        System.out.println("\n Victoire : ");

        switch (players.size()) {
            case 2:
                if (finalScore <= 7) {
                    System.out.print("Honorable");
                } else if (finalScore <= 11) {
                    System.out.print("Glorieuse");
                } else {
                    System.out.print("Totale");
                }
            case 3:
                if (finalScore <= 9) {
                    System.out.print("Honorable");
                } else if (finalScore <= 13) {
                    System.out.print("Glorieuse");
                } else {
                    System.out.print("Totale");
                }
            case 4:
                if (finalScore <= 10) {
                    System.out.print("Honorable");
                } else if (finalScore <= 14) {
                    System.out.print("Glorieuse");
                } else {
                    System.out.print("Totale");
                }
        }
        return finalScore;
    }
}