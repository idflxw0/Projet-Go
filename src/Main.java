import Board.Board;
import Board.IBoard;

import java.util.Scanner;
import Board.IllegalMoveException;
import org.w3c.dom.ls.LSOutput;

public class Main {
    private static int nbCommands = 1;

    public static void main(String[] args) {
        IBoard board = null;
        Scanner sc = new Scanner(System.in);
        int size = 0;
        System.out.print(nbCommands + " ");
        String input = sc.nextLine();

        while (!input.equals("quit")) {
            input = input.trim().toLowerCase();
            if (input.contains("boardsize") && board == null) {
                try {
                    size = Integer.parseInt(input.split(" ")[1]);
                    board = new Board(size);
                    System.out.println("=" + nbCommands);
                    nbCommands++;
                } catch (IllegalMoveException e) {
                    System.out.println("Taille du plateau invalide: " + Integer.parseInt(input.split(" ")[1]));
                    System.exit(1);
                }
            }
            else if (board != null ) {
                if (input.contains("showboard")) {
                    board.showBoard();
                    System.out.println("=" + nbCommands);
                    nbCommands++;
                }
                else if (input.contains("clear_board")) {
                    board.clearBoard();
                    System.out.println("=" + nbCommands);
                    nbCommands++;
                }
                else if (input.contains("play")) {
                    String color = input.split(" ")[1];
                    String command = input.split(" ")[2];
                    board.play(color,command);
                    System.out.println("=" + nbCommands);
                    nbCommands++;
                }
                else {
                    System.out.println("?" + nbCommands);
                    nbCommands++;
                }
            }
            else {
                System.out.println("?" + nbCommands);
                nbCommands++;
            }
            System.out.print(nbCommands + " ");
            input = sc.nextLine();
        }
        sc.close();
        System.out.print("=" + nbCommands);
    }

}
