import Board.Board;
import Board.IBoard;

import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        if(args.length != 2 || !args[0].equals("boardsize")) {
            System.out.println("Usage: GameGo <board size> <size>");
            System.exit(1);
        }
        int size = 0;
        try {
            size = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Taille du plateau invalide: " + args[1]);
            System.exit(1);
        }
        IBoard board = new Board(size);
        System.out.println("Plateau de taille " + size + " créé");

        Scanner sc = new Scanner(System.in);
        System.out.println("Entrez une commande : ");
        String input = sc.nextLine();

        while (!input.equals("exit")) {
            if (input.contains("showboard")) {
                board.showBoard();
            } else {
                System.out.println("Commande inconnue");
            }
            System.out.println("Entrez une commande : ");
            input = sc.nextLine();
        }
        sc.close();
    }
}
