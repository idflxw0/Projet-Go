/**
 * Represents the game board for Go. This class is responsible for maintaining
 * the state of the game, including the positions of stones and the board logic.
 * It provides methods to place stones, check for captures, and determine the score.
 * @Class : Board
 * @since 19/11/2023
 */
package GO.Board;

import GO.Players.Black;
import GO.Players.IPlayer;
import GO.Players.White;
import IHM.Constants;

public class Board implements IBoard {
    private static final int SEUIL = 10;
    private Stone[][] board; //board of the game
    private final int size; //size of the board
    private IPlayer white;
    private IPlayer black;

    /**
     * Create a board of size x size
     *
     * @param size size of the board
     */
    public Board(int size) {
        board = new Stone[size][size];
        this.size = size;
        white = new White();
        black = new Black();

        initBoard();
    }

    /**
     * Initialise the board
     */
    private void initBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = Stone.EMPTY;
            }
        }
    }

    /**
     * Show the board
     */
    @Override
    public void showBoard() {
        int msgL1 = size >= SEUIL ? 1 + (size - SEUIL) : 1;
        int msgL2 = msgL1 - 1;

        //HEADER
        System.out.print("   ");
        for (int i = 0; i < size; i++) {
            System.out.printf("%c ", 'A' + i);
        }
        System.out.println();

        //BODY
        for (int i = size - 1; i >= 0; i--) {
            System.out.printf("%2d ", i + 1);
            for (int j = 0; j < size; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.printf("%2d", i + 1);


            if (i == msgL1) {
                System.out.printf("     WHITE (O) has captured %d stones", white.getCaptures());
            }
            if (i == msgL2) {
                System.out.printf("     BLACK (X) has captured %d stones", black.getCaptures());
            }
            System.out.println();
        }

        //FOOTER
        System.out.print("   ");
        for (int i = 0; i < size; i++) {
            System.out.printf("%c ", 'A' + i);
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
    public String play(String color, String pos) {
        if (!color.equals("black") && !color.equals("white")) return "INCORRECT_PLAY";

        if (pos.length() < 2) return "INCORRECT_PLAY";

        char column = pos.toUpperCase().charAt(0);
        String rowString = pos.substring(1);

        if (!Character.isLetter(column)) return "INCORRECT_PLAY";

        if (!rowString.matches("\\d+")) return "INCORRECT_PLAY";

        int row;
        try {
            row = Integer.parseInt(rowString);
        } catch (NumberFormatException e) {
            return "INCORRECT_PLAY";
        }

        if (row < 1 || row > size) {
            return "INCORRECT_PLAY";
        }

        Stone type = color.equals("black") ? Stone.BLACK : Stone.WHITE;
        return placeStones(row, column, type) ? "SUCCESS" : "ILLEGAL_MOVE";
    }

    private boolean placeStones(int row, char column, Stone color) {
        int columnIndex = column - 'A';
        int rowIndex = row - 1;
        if (isPlaceable(rowIndex, columnIndex)) {
            this.board[rowIndex][columnIndex] = color == Stone.WHITE ? Stone.WHITE : Stone.BLACK;
            return true;
        }
        return false;
    }

    public boolean isPlaceable(int row, int column) {
        return this.board[row][column] == Stone.EMPTY;
    }

    public void floodFill(int row, int column, String color) {

    }
    private void floodFill(int[][] matrix, int row, int col, int targetColor, int replacementColor) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8 || matrix[row][col] != targetColor) {
            return;
        }

        matrix[row][col] = replacementColor;

        // Perform flood fill in all eight directions
        floodFill(matrix, row - 1, col, targetColor, replacementColor); // Up
        floodFill(matrix, row + 1, col, targetColor, replacementColor); // Down
        floodFill(matrix, row, col - 1, targetColor, replacementColor); // Left
        floodFill(matrix, row, col + 1, targetColor, replacementColor); // Right
        floodFill(matrix, row - 1, col - 1, targetColor, replacementColor); // Up-Left
        floodFill(matrix, row - 1, col + 1, targetColor, replacementColor); // Up-Right
        floodFill(matrix, row + 1, col - 1, targetColor, replacementColor); // Down-Left
        floodFill(matrix, row + 1, col + 1, targetColor, replacementColor); // Down-Right
    }
}
