/**
 * Represents the game board for Go. This class is responsible for maintaining
 * the state of the game, including the positions of stones and the board logic.
 * It provides methods to place stones, check for captures, and determine the score.
 * @Class : Board
 * @since 19/11/2023
 */
package GO.Board;

import GO.Players.AI;
import GO.Players.Player;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Board implements IBoard {
    private static final int THRESHOLD = 10;
    private static final int BOARD_MIN_SIZE = 2;
    private static final int BOARD_MAX_SIZE = 26;
    private static final int BOARD_DEFAULT_SIZE = 19;
    private Stone[][] board; //board of the game
    private int size; //size of the board
    private IPlayer white; //white player
    private IPlayer black; //black player


    /**
     * Create a board of default size
     */
    public Board() {
        this.board = new Stone[BOARD_DEFAULT_SIZE][BOARD_DEFAULT_SIZE];
        this.size = BOARD_DEFAULT_SIZE;
        this.white = new Player(Stone.WHITE,false);
        this.black = new Player(Stone.BLACK, true);
        initBoard();
    }
    public Board(int size, String play) {
        this.board = new Stone[size][size];
        this.white = new Player(Stone.WHITE,false);
        this.black = new Player(Stone.BLACK, true);
        initBoard();
    }

    /**
     * Create a board of size x size
     * @param size size of the board
     */
    public Board(int size) {
        if (size < BOARD_MIN_SIZE || size > BOARD_MAX_SIZE) throw new IllegalArgumentException ("Size must be between 2 and 26");
        this.board = new Stone[size][size];
        this.size = size;
        this.white = new Player(Stone.WHITE,false);
        this.black = new Player(Stone.BLACK,true);
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
     * Clear the board
     */
    @Override
    public void clearBoard() {
        this.initBoard();
        this.white.resetCaptures();
        this.black.resetCaptures();
    }

    @Override
    public String playBot(String color) {
        // Set AI for the specified color if not already set
        if (color.equalsIgnoreCase("black") && !(this.black instanceof AI)) {
            this.black = new AI(Stone.BLACK, false);
            this.white.setTurn(true);
            return "SUCCESS";
        } else if (color.equalsIgnoreCase("white") && !(this.white instanceof AI)) {
            this.white = new AI(Stone.WHITE, false);
            return "SUCCESS";
        }
        // Determine the AI player based on color
        AI botPlayer = (AI) (color.equalsIgnoreCase("black") ? black : white);
        IPlayer humanPlayer = color.equalsIgnoreCase("black") ? white : black;

        Point pos = botPlayer.placeStoneRandomly(this, botPlayer.getStone());
        if (pos != null) {
            char column = (char) ('A' + pos.y);
            int row = pos.x + 1;
            System.out.println("bot " + color + " played on " + column + row);
            showBoard();
            botPlayer.setTurn(false);
            humanPlayer.setTurn(true);
            return "SUCCESS";
        }
        return "ILLEGAL_PLAY";
    }



    /**
     * Play a stone on the board
     * @param color : color of the stone
     * @param pos : positions of the stone including the column and the row
     * @return : SUCCESS if the stone is placed,
     *           INCORRECT_PLAY if the position is incorrect,
     *           ILLEGAL_MOVE if the move is illegal
     */
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
        if (row < 1 || row > size || column < 'A' || column > 'A' + size - 1) {
            return "INCORRECT_PLAY";
        }
        Stone type = color.equalsIgnoreCase("black") ? Stone.BLACK : Stone.WHITE;
        IPlayer currentPlayer = (type == Stone.BLACK) ? black : white;
        IPlayer oppositePlayer = (type == Stone.BLACK) ? white : black;

        if (!currentPlayer.getTurn()) {
            return "NOT_YOUR_TURN";
        }

        String output = placeStones(row, column, type) ? "SUCCESS" : "ILLEGAL_MOVE";

        if (output.equals("SUCCESS")) {
            currentPlayer.setTurn(false);
            oppositePlayer.setTurn(true);

            if (oppositePlayer instanceof AI) {
                playBot(oppositePlayer.getColor());
            }
        }
        return output;
    }

    /**
     * Place a stone on the board
     * @param x : row of the stone
     * @param y : column of the stone
     * @param color : color of the stone
     * @return : true if the stone is placed, false otherwise
     */
    public boolean placeStones(int x, char y, Stone color) {
        int columnIndex = y - 'A';
        int rowIndex = x - 1;
        if (!isPlaceable(rowIndex, columnIndex)){
            return false;
        }


        board[rowIndex][columnIndex] = color;

        List<Set<String>> friendGroups = new ArrayList<>(); // List of friend groups
        List<Set<String>> enemyGroups = new ArrayList<>(); // List of enemy groups
        // Check adjacent points using row and column indices
        if (rowIndex > 0) checkAdjacent(rowIndex - 1, columnIndex, color, friendGroups, enemyGroups); // Check the point above
        if (columnIndex > 0) checkAdjacent(rowIndex, columnIndex - 1, color, friendGroups, enemyGroups); // Check the point to the left
        if (rowIndex < size - 1) checkAdjacent(rowIndex + 1, columnIndex, color, friendGroups, enemyGroups); // Check the point below
        if (columnIndex < size - 1) checkAdjacent(rowIndex, columnIndex + 1, color, friendGroups, enemyGroups); // Check the point to the right

        // Check for captures
        for (Set<String> group : enemyGroups) {
            if (countLiberties(group) == 0) {
                removeGroup(group);
            }
        }

        // Check if the placed stone is in atari (has no liberties)
        if (countLiberties(getGroup(rowIndex, columnIndex, color)) == 0) {
            manageCaptures(rowIndex, columnIndex);
            board[rowIndex][columnIndex] = Stone.EMPTY; // Remove the placed stone
            return false;
        }
//        System.out.println("nbLiberties for " + board[rowIndex][columnIndex] + ": " + getNbLiberties(rowIndex, columnIndex));
        return true;
    }

    /**
     * Check if a stone can be placed on the board
     * @param row : row of the stone
     * @param column : column of the stone
     * @return : true if the stone can be placed, false otherwise
     */
    public boolean isPlaceable(int row, int column) {
        if (row < 0 || column < 0 || row >= size || column >= size) return false;
        return this.board[row][column] == Stone.EMPTY;
    }

    /**
     * Check adjacent points
     * @param color : color of the stone
     * @param friendGroups : list of friend groups
     * @param enemyGroups : list of enemy groups
     */
    private void checkAdjacent(int x, int y, Stone color, List<Set<String>> friendGroups, List<Set<String>> enemyGroups) {
        if (board[x][y] == color) {
            friendGroups.add(getGroup(x, y, color));
        } else if (board[x][y] != Stone.EMPTY) {
            enemyGroups.add(getGroup(x, y, board[x][y]));
        }
    }

    /**
     * Get the group of stones
     * @param x : row of the stone
     * @param y : column of the stone
     * @param color : color of the stone
     * @return : the group of stones
     */
    private Set<String> getGroup(int x, int y, Stone color) {
        Set<String> group = new HashSet<>();
        exploreGroup(x, y, color, group);
        return group;
    }

    /**
     * Explore the group of stones
     * @param x : row of the stone
     * @param y : column of the stone
     * @param color : color of the stone
     * @param group : group of stones
     */
    private void exploreGroup(int x, int y, Stone color, Set<String> group) {
        String pos = x + "," + y;
        if (x < 0 || y < 0 || x >= size || y >= size || board[x][y] != color || group.contains(pos)) {
            return;
        }

        group.add(pos);
        exploreGroup(x - 1, y, color, group);
        exploreGroup(x + 1, y, color, group);
        exploreGroup(x, y - 1, color, group);
        exploreGroup(x, y + 1, color, group);
    }
    private int getNbLiberties(int x, int y) {
        return countLiberties(getGroup(x, y, board[x][y]));
    }

    /**
     * Count the liberties of a group of stones
     * @param group : group of stones
     * @return : the number of liberties
     */
    private int countLiberties(Set<String> group) {
        Set<String> liberties = new HashSet<>();
        for (String pos : group) {
            String[] parts = pos.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            addLiberty(liberties, x - 1, y);
            addLiberty(liberties, x + 1, y);
            addLiberty(liberties, x, y - 1);
            addLiberty(liberties, x, y + 1);
        }
        return liberties.size();
    }

    /**
     * Add a liberty to a group of stones
     * @param liberties : liberties of the group
     * @param x : row of the stone
     * @param y : column of the stone
     */
    private void addLiberty(Set<String> liberties, int x, int y) {
        String pos = x + "," + y;
        if (x >= 0 && y >= 0 && x < size && y < size && board[x][y] == Stone.EMPTY) {
            liberties.add(pos);
        }
    }

    /**
     * Remove a group of stones
     * @param group : group of stones
     */
    private void removeGroup(Set<String> group) {
        for (String pos : group) {
            String[] parts = pos.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            manageCaptures(x, y);
            board[x][y] = Stone.EMPTY;
        }
    }

    /**
     * Manage the captures
     * @param x : row of the stone
     * @param y : column of the stone
     */
    private void manageCaptures(int x, int y) {
        if (board[x][y] == Stone.BLACK) white.addCaptures(1);
        else black.addCaptures(1);
    }
    /**
     * Show the board
     */
    @Override
    public void showBoard() {
        int msgL1 = size >= THRESHOLD ? 1 + (size - THRESHOLD) : 1;
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
    public Stone[][] getBoard() {
        return board;
    }

    @Override
    public int getSize() {
        return size;
    }
}