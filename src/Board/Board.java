/**
 * Represents the game board for Go. This class is responsible for maintaining
 * the state of the game, including the positions of stones and the board logic.
 * It provides methods to place stones, check for captures, and determine the score.
 * @Class : Board
 * @since 19/11/2023
 */
package Board;

import java.awt.*;

public class Board implements IBoard{
    private Stone[][] board; //board of the game
    private final int size; //size of the board

    /**
     * Create a board of size x size
     * @param size size of the board
     * @throws  IllegalArgumentException if the size in incorrect
     */
    public Board(int size) throws IllegalMoveException {
        if (size < 1 || size > 26) throw new IllegalMoveException("La taille du plateau doit etre comprise entre 1 et 26");
        board = new Stone[size][size]; //by default the size of the board is 19x19
        this.size = size;
        initBoard();
    }

    /**
     * Initialise the board
     */
    private void initBoard() {
        for (int i =0; i < size; i++) {
            for (int j = 0; j < size;j++) {
                board[i][j] = Stone.EMPTY;
            }
        }
    }

    /**
     * Show the board
     */
    @Override
    public void showBoard() {
       //HEADER
        System.out.print("   ");
        for (int i = 0; i < size; i++) {
//            System.out.printf((char)('A' + i) + " ");
            System.out.printf("%c ",'A' + i);
        }
        System.out.println();

        //BODY
        for (int i = 0; i < size; i++) {
            System.out.printf("%2d ", i + 1);
            for (int j = 0; j < size; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.printf("%2d ", i + 1);
            System.out.println();
        }

        //FOOTER
        System.out.print("   ");
        for (int i = 0; i < size; i++) {
            System.out.printf((char)('A' + i) + " ");
        }
        System.out.println();
    }

    /**
     * Clear the board
     */
    @Override
    public void clearBoard() {
        this.initBoard();
    }

    @Override
    public void play(String color,String pos)  {
        if (!color.equals("black") && !color.equals("white")) {
            System.out.println("Couleur incorrecte : "  + color);
            return;
        }
        if (pos.length() < 2) {
            System.out.println("position incorrecte : " + pos);
            return;
        }

        char column = pos.charAt(0);
        String rowString = pos.substring(1);

        if (!Character.isLetter(column)) {
            System.out.println("Colonne incorrecte : " + column);
            return;
        }
        if (!rowString.matches("\\d+")) { //matches if the string contains only digits
            System.out.println("Ligne incorrecte : " + rowString);
            return;
        }

        int row;
        try {
            row = Integer.parseInt(rowString);
        } catch (NumberFormatException e) {
            System.out.println("Ligne incorrecte : " + rowString);
            return;
        }

        if (row < 2 || row > size) {
            System.out.println("Ligne incorrecte : " + row);
            return;
        }


        //TODO
        int columnIndex = (column - 'A')/size;
        int rowIndex = row - 1;

        if (color.equals("black")) {
            this.board[rowIndex][columnIndex] = Stone.BLACK;
        } else {
            this.board[rowIndex][columnIndex] = Stone.WHITE;
        }
    }
}
