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
        this.size = size;
        String[] moves = play.split(" ");
        for (String move : moves) {
            String[] parts = move.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            Stone color = parts[2].equals("B") ? Stone.BLACK : Stone.WHITE;
            board[x][y] = color;
        }
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

    /**
     * Get the board
     * @return : the board
     */
    public Stone[][] getBoard() {
        return board;
    }

    /**
     * Get the size of the board
     * @return : the size of the board
     */
    @Override
    public int getSize() {
        return size;
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
        if (!color.equalsIgnoreCase("black") && !color.equalsIgnoreCase("white")) return "INCORRECT_PLAY";

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

        boolean moveSuccessful = placeStones(row, column, type);
        if (moveSuccessful) {
            currentPlayer.setTurn(false);
            oppositePlayer.setTurn(true);

            // Let the AI play its turn if the opponent is AI.
            if (oppositePlayer instanceof AI) {
                playBot(oppositePlayer.getColor());
            }

            // Check for game over after both players have had the chance to play
            if (isGameOver()) {
                return "GAME OVER";
            }

            return "SUCCESS";
        } else {
            return "ILLEGAL_MOVE";
        }
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

        // Check if the placed stone is in atari (has no liberties).
        if (countLiberties(getGroup(rowIndex, columnIndex, color)) == 0) {
            manageCaptures(rowIndex, columnIndex);
            board[rowIndex][columnIndex] = Stone.EMPTY; // Remove the placed stone
            return false;
        }
//        System.out.println("nbLiberties for " + board[rowIndex][columnIndex] + ": " + getNbLiberties(rowIndex, columnIndex));
        return true;
    }

    public boolean placeStone(int x, int y, Stone color) {
        int columnIndex = y - 1;
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

        // Check if the placed stone is in atari (has no liberties).
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
    public int getNbLiberties(int x, int y) {
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
     * Let the bot play
     * @return : SUCCESS if the bot played, ILLEGAL_PLAY otherwise
     */
    @Override
    public String playBot(String color) {
        if (color.equalsIgnoreCase("black") && !(this.black instanceof AI)) {
            this.black = new AI(Stone.BLACK, false);
            this.white.setTurn(true);
            return "SUCCESS";
        } else if (color.equalsIgnoreCase("white") && !(this.white instanceof AI)) {
            this.white = new AI(Stone.WHITE, false);
            return "SUCCESS";
        }
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
        if (isGameOver()) return "GAME OVER";
        return "ILLEGAL_PLAY";
    }


    /**
     * Checks if the game is over.
     * The game is over when neither player can place any more stones on the board.
     * @return true if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        AI aiHelper = new AI(Stone.BLACK, false);
        List<Point> emptyPositions = aiHelper.getAllEmptyPositions(this);

        boolean whiteCanPlay = canPlayerPlay(Stone.WHITE, emptyPositions);
        boolean blackCanPlay = canPlayerPlay(Stone.BLACK, emptyPositions);

        return !whiteCanPlay && !blackCanPlay;
    }

    /**
     * Checks if a player can make a move by attempting to place a stone in any empty position.
     * @param stone the color of the stone to be placed.
     * @param emptyPositions a list of empty positions on the board.
     * @return true if the player can play, false otherwise.
     */
    private boolean canPlayerPlay(Stone stone, List<Point> emptyPositions) {
        for (Point pos : emptyPositions) {
            if (isPlaceable(pos.x, pos.y) && isValidMove(stone, pos.x, pos.y)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if placing a stone at the given position is a valid move.
     * Implement specific Go rules here to determine if a move is valid.
     * For simplicity, this example just checks if the position is empty.
     * @param stone the color of the stone to be placed.
     * @param x the x-coordinate of the position.
     * @param y the y-coordinate of the position.
     * @return true if it's a valid move, false otherwise.
     */
    private boolean isValidMove(Stone stone, int x, int y) {
        return board[x][y] == Stone.EMPTY;
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int msgL1 = size >= THRESHOLD ? 1 + (size - THRESHOLD) : 1;
        int msgL2 = msgL1 - 1;

        // HEADER
        sb.append("   ");
        for (int i = 0; i < size; i++) {
            sb.append(String.format("%c ", 'A' + i));
        }
        sb.append("\n");

        // BODY
        for (int i = size - 1; i >= 0; i--) {
            sb.append(String.format("%2d ", i + 1));
            for (int j = 0; j < size; j++) {
                sb.append(board[i][j]).append(" ");
            }
            sb.append(String.format("%2d", i + 1));
            if (i == msgL1) {
                sb.append(String.format("     WHITE (O) has captured %d stones", white.getCaptures()));
            }
            if (i == msgL2) {
                sb.append(String.format("     BLACK (X) has captured %d stones", black.getCaptures()));
            }
            sb.append("\n");
        }

        // FOOTER
        sb.append("   ");
        for (int i = 0; i < size; i++) {
            sb.append(String.format("%c ", 'A' + i));
        }
        sb.append("\n");

        return sb.toString();
    }
}