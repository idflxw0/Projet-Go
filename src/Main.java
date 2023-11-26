import Board.Board;
import Board.IBoard;

import java.util.Scanner;
import Board.IllegalMoveException;

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
                String[] inputArray = input.split(" ");
                if (inputArray.length > 1) {
                    try { //TODO pas une bonne idée d'utiliser try and catch. faut revoir
                        size = Integer.parseInt(input.split(" ")[1]);
                        board = new Board(size);
                        System.out.println("=" + nbCommands);
                        nbCommands++;
                    } catch (NumberFormatException | IllegalMoveException e) {
                        System.out.println("?" + nbCommands);
                    }
                } else {
                    System.out.println("?" + nbCommands);
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
                    if (board.play(color,command)) {
                        System.out.println("=" + nbCommands +" " + command.toUpperCase());
                    }
                    else {
                        System.out.println("?" + nbCommands+ " illegal move");
                    }
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