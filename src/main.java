import Board.Board;
import Board.IBoard;

import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Bienvenue dans le jeu de Go");

        int size = 0;
        IBoard board = null;

        System.out.println("Veuillez choisir la taille du plateau : ");
        String input = sc.nextLine();

        while (!input.equals("exit")) {
            try {
                if (input.contains("boardsize")) {
                    size = Integer.parseInt(input.split(" ")[1]);
                    board = new Board(size);
                    System.out.println("Plateau de taille " + size + " créé");
                } else if (input.contains("showboard")) {
                    if (board != null) {
                        board.showBoard();
                    } else {
                        System.out.println("Le plateau n'a pas été initialisé avec une taille.");
                    }
                } else {
                    System.out.println("Commande inconnue");
                }
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide pour la taille du plateau.");
            }

            System.out.println("Entrez une commande : ");
            input = sc.nextLine();
        }

        sc.close();
    }
}
