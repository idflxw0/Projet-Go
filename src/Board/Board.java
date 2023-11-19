/**
 * Represents the game board for Go. This class is responsible for maintaining
 * the state of the game, including the positions of stones and the board logic.
 * It provides methods to place stones, check for captures, and determine the score.
 * @Class : Board
 * @since 19/11/2023
 */
package Board;

public class Board implements IBoard{

    final int DEFAULT_SIZE = 19; //defaut size of the board
    private Stone[][] board; //board of the game
    private final int size; //size of the board

    /**
     * Create a board of size 19x19
     */
    public Board() {
        this.board = new Stone[DEFAULT_SIZE][DEFAULT_SIZE]; //by default the size of the board is 19x19
        this.size = DEFAULT_SIZE;
        initBoard();
    }

    /**
     * Create a board of size x size
     * @param size size of the board
     * @throws  IllegalArgumentException if the size in incorrect
     */
    public Board(int size) {
        if (size < 1 || size > 26) throw new IllegalArgumentException("La taille du plateau doit etre comprise entre 1 et 26");
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
     * Initialise the board with the size
     * @param size size of the board
     */
    @Override
    public void boardSize(int size) {
        new Board(size);
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
}
