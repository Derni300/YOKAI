

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.*;

public class YokaiGame {
    private List<Player> players;
    int n = 6;
    public Cell[][] cardBoard = new Cell[n][n];
    public Cell[][] clueBoard = new Cell[n][n];

    public void play() {
        playSound();
        createPlayers();
        initialiseBoard();
        Stack<String> clueStack = setCardBoard(players.size());
        int turn = 0;
        do {
            System.out.println("Au tour de : " + players.get(turn).getName() + "\n");
            if (turn < players.size() - 1) {
                turn += 1;
            } else {
                turn = 0;
            }
            printCardBoard();
            printClueBoard();
            showTwoCards();
            moveOneCard();
            printCardBoard();
            drawClueCard(clueStack);
        } while (yokaiAreAppeased());
    }


    private void createPlayers() {
        players = new ArrayList<>();
        Scanner scannerInt = new Scanner(System.in);
        Scanner scannerLine = new Scanner(System.in);
        System.out.println("Veuillez entrer le nombre de joueurs");
        int playerNumber = scannerInt.nextInt();
        System.out.println();
        for (int i = 0; i < playerNumber; i++) {
            System.out.println("Nom du joueur " + (i + 1));
            players.add(new Player(scannerLine.nextLine()));
        }
        System.out.println();
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
        System.out.println(" ".repeat(3 * (n / 2) - 4) + "[Card Board]" + " ".repeat(3 * (n / 2) - 4));
        System.out.println("╭ " + " ─ ".repeat(n) + " ╮");
        for (int i = 0; i < n; i++) {
            System.out.print("│  ");
            for (int j = 0; j < n; j++) {
                if (cardBoard[i][j] == null) {
                    System.out.print("·  ");
                } else {
                    YokaiCard card = cardBoard[i][j].getYokaiCard();
                    System.out.print(card.getCardName() + "  ");
                }
            }
            System.out.print("│");
            System.out.println();
        }
        System.out.println("╰ " + " ─ ".repeat(n) + " ╯");
        System.out.println();
    }

    private void printClueBoard() {
        System.out.println(" ".repeat(3 * (n / 2) - 4) + "[Clue Board]" + " ".repeat(3 * (n / 2) - 4));
        System.out.println("╭ " + " ─ ".repeat(n) + " ╮");
        for (int i = 0; i < n; i++) {
            System.out.print("│  ");
            for (int j = 0; j < n; j++) {
                if (clueBoard[i][j] == null) {
                    System.out.print("·  ");
                } else {
                    YokaiClue clue = clueBoard[i][j].getClueCard();
                    System.out.print(clue.getClueName() + "  ");
                }
            }
            System.out.print("│");
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
            } while ((x >= n || y >= n) || cardBoard[x][y] == null);

            System.out.println("\nCarte " + i + " : " + cardBoard[x][y].getYokaiCard().getCardName());
            System.out.println();
        }
    }

    private void moveOneCard() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choisissez les coordonnées de la carte à déplacer :");
        System.out.println();
        int x, y;

        do {
            System.out.println("Veuillez sélectionner des coordonnées valides pour la carte à déplacer :");
            System.out.println("Coordonnées en x de la carte :");
            x = scanner.nextInt();
            System.out.println("Coordonnées en y de la carte :");
            y = scanner.nextInt();
        } while ((x >= n || y >= n) || cardBoard[x][y] == null);
        System.out.println();

        int i, j;
        boolean test;

        Cell temp = cardBoard[x][y];
        cardBoard[x][y] = null;
        printCardBoard();

        do {
            System.out.println("Veuillez sélectionner des coordonnées valides où déplacer la carte :\n");
            System.out.println("Coordonnées en x de la carte :");
            i = scanner.nextInt();
            System.out.println("Coordonnées en y de la carte :");
            j = scanner.nextInt();

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
        } while (test);
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

        System.out.println("gauche : " + gauche + " ,droite : " + droite + " ,haut : " + haut + " ,bas : " + bas);
        return (!(gauche || droite || haut || bas));
    }

    private void drawClueCard(Stack<String> clueStack) {
        Scanner scanner = new Scanner(System.in);
        List<String> drawClueList = new ArrayList<>();
        String clueCard = clueStack.pop();
        System.out.println("\nIndice pioché : " + clueCard + "\n");
        System.out.println("Choisissez si vous voulez mettre l'indice de côté (tap 0) ou le jouer (tap 1)");
        int choice = scanner.nextInt();
        System.out.println();

        if (choice == 1) {
            System.out.println("Choisissez ou poser l'indice :\n");
            int x, y;

            do {
                System.out.println("Veuillez sélectionner des coordonnées valides pour l'indice à placer :\n");
                System.out.println("Coordonnées en x de la carte :");
                x = scanner.nextInt();
                System.out.println("Coordonnées en y de la carte :");
                y = scanner.nextInt();
            } while (((x >= n) || (y >= n)) || clueBoard[x][y] != null);

            Position positionClue = new Position(x, y);
            YokaiClue card = new YokaiClue(clueCard, positionClue);
            clueBoard[x][y] = new Cell(null, card, positionClue);

        } else {
            System.out.println("L'indice est mis sur le côté\n");
            drawClueList.add(clueCard);
            System.out.println("Liste des indices :\n");
            System.out.println(drawClueList + "\n");
        }
    }

    private boolean yokaiAreAppeased() {
        Scanner scannerLine = new Scanner(System.in);
        System.out.println("Voulez vous déclarer que les Yokais sont apaisés ? (Y/N)\n");
        String choice = scannerLine.nextLine();
        if (choice.equals("Y")) {
            System.out.println(verification());
            return verification();
        } else {
            return true;
        }
    }

    private boolean verification() {
        boolean win = true;
        boolean gauche, droite, haut, bas;
        for (int i = 0; i < cardBoard.length; i++) {
            for (int j = 0; j < cardBoard.length; j++) {
                if (cardBoard[i][j] == null) {
                    break;
                } else {
                    if (i > 0) {
                        haut = cardBoard[i - 1][j].getYokaiCard().getCardName()
                                == cardBoard[i][j].getYokaiCard().getCardName();
                    } else {
                        haut = false;
                    }
                    if (j > 0) {
                        gauche = cardBoard[i][j - 1].getYokaiCard().getCardName()
                                == cardBoard[i][j].getYokaiCard().getCardName();
                    } else {
                        gauche = false;
                    }
                    if (i < n) {
                        bas = cardBoard[i + 1][j].getYokaiCard().getCardName()
                                == cardBoard[i][j].getYokaiCard().getCardName();
                    } else {
                        bas = false;
                    }
                    if (j < n) {
                        droite = cardBoard[i][j + 1].getYokaiCard().getCardName()
                                == cardBoard[i][j].getYokaiCard().getCardName();
                    } else {
                        droite = false;
                    }
                    if (!(gauche || droite || haut || bas)) {
                        win = false;
                    }
                }
            }
        }
        return win;
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
}