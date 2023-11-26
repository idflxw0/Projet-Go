/**
 * Represents the game board for Go. This class is responsible for maintaining
 * the state of the game, including the positions of stones and the board logic.
 * It provides methods to place stones, check for captures, and determine the score.
 * @Class : Board
 * @since 19/11/2023
 */
package Board;

public class Board implements IBoard{
    private Stone[][] board; //board of the game
    private final int size; //size of the board
    private int whiteCaptures;
    private int blackCaptures;

    /**
     * Create a board of size x size
     * @param size size of the board
     * @throws  IllegalArgumentException if the size in incorrect
     */
    public Board(int size) throws IllegalMoveException {
        if (size < 1 || size > 26) throw new IllegalMoveException("La taille du plateau doit etre comprise entre 1 et 26");
        board = new Stone[size][size]; //by default the size of the board is 19x19
        this.size = size;
        whiteCaptures = 0;
        blackCaptures = 0;
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
        int msgL1 = size >= 10 ? 1 + (size - 10) : 1;
        int msgL2 =  msgL1 - 1;

        //HEADER
        System.out.print("   ");
        for (int i = 0; i < size; i++) {
            System.out.printf("%c ",'A' + i);
        }
        System.out.println();

        //BODY
        for (int i = size-1; i >= 0; i--) {
            System.out.printf("%2d ", i + 1);
            for (int j = 0; j < size; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.printf("%2d", i + 1);


            if (i == msgL1) {
                System.out.printf("     WHITE (O) has captured %d stones", whiteCaptures);
            }
            if (i == msgL2) {
                System.out.printf("     BLACK (X) has captured %d stones", blackCaptures);
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
    public boolean play(String color,String pos)  {
        if (!color.equals("black") && !color.equals("white")) {
            System.out.println("Couleur incorrecte : "  + color);
            return false;
        }
        if (pos.length() < 2) {
            System.out.println("position incorrecte : " + pos);
            return false;
        }

        char column = pos.toUpperCase().charAt(0);
        String rowString = pos.substring(1);

        if (!Character.isLetter(column)) {
            System.out.println("Colonne incorrecte : " + column);
            return false;
        }
        if (!rowString.matches("\\d+")) {
            System.out.println("Ligne incorrecte : " + rowString);
            return false;
        }
        int row;
        try {
            row = Integer.parseInt(rowString);
        } catch (NumberFormatException e) {
            System.out.println("Ligne incorrecte : " + rowString);
            return false;
        }
        if (row < 1 || row > size) {
            System.out.println("Ligne incorrecte : " + row);
            return false;
        }
        Stone type = null;
        if (color.equals("black")) type = Stone.BLACK;
        else type = Stone.WHITE;

        return placeStones(row,column,type);
    }

    private boolean placeStones(int row, char column, Stone color) {
        int columnIndex = column - 'A';
        int rowIndex = row - 1;
        if(isplaceable(rowIndex, columnIndex) && !isSuicide(rowIndex, columnIndex,color)) {
            if (color == Stone.BLACK) {
                this.board[rowIndex][columnIndex] = Stone.BLACK;
            } else {
                this.board[rowIndex][columnIndex] = Stone.WHITE;
            }
            return true;
        }
        return false;
    }

    public boolean isplaceable(int row, int column) {
        return this.board[row][column] == Stone.EMPTY;
    }

    private boolean isSuicide(int row, int column, Stone color_w_place) {
        if (row < 1 || row > size || column < 1 || column > size) return false;
//        if(board[row-1][column] != color_w_place)
        return false;
    }
}
