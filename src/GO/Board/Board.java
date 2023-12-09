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

import java.util.*;

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
        this.board = new Stone[size][size];
        this.size = size;
        this.white = new White();
        this.black = new Black();
        initBoard();
    }

    private class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
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

    private boolean placeStones(int x, char y, Stone color) {
        int columnIndex = y - 'A';
        int rowIndex = x - 1;
        if (!isPlaceable(rowIndex,columnIndex)) return false;

        board[rowIndex][columnIndex] = color;

        List<Set<Point>> friendGroups = new ArrayList<>();
        List<Set<Point>> enemyGroups = new ArrayList<>();
        Stone enemyColor = (color == Stone.BLACK) ? Stone.WHITE : Stone.BLACK;

        if (rowIndex > 0) checkAdjacent(new Point(rowIndex - 1,columnIndex),color,friendGroups,enemyGroups);
        if (columnIndex > 0) checkAdjacent(new Point(rowIndex, columnIndex-1), color, friendGroups, enemyGroups);
        if (rowIndex < size - 1) checkAdjacent(new Point(rowIndex+1, columnIndex), color, friendGroups, enemyGroups);
        if (columnIndex < size - 1) checkAdjacent(new Point(rowIndex, columnIndex+1), color, friendGroups, enemyGroups);

        // Check for captures
        for (Set<Point> group : enemyGroups) {
            if (countLiberties(group) == 0) {
                removeGroup(group);
            }
        }

        // Check if the placed stone is in atari (has no liberties)
        if (countLiberties(getGroup(new Point(rowIndex, columnIndex), color)) == 0) {

            board[rowIndex][columnIndex] = Stone.EMPTY; // Remove the placed stone
            return false;
        }
        showBoard();
        return true;
    }

    public boolean isPlaceable(int row, int column) {
        return this.board[row][column] == Stone.EMPTY;
    }

    private void checkAdjacent(Point p,Stone color, List<Set<Point>> friendGroups, List<Set<Point>> enemyGroups) {
        if (board[p.x][p.y] == color) {
            friendGroups.add(getGroup(p, color));
        } else if (board[p.x][p.y] != Stone.EMPTY) {
            enemyGroups.add(getGroup(p, board[p.x][p.y]));
        }
    }
    private Set<Point> getGroup(Point p, Stone color) {
        Set<Point> group = new HashSet<>();
        exploreGroup(p, color, group);
        return group;
    }

    private void exploreGroup(Point p, Stone color, Set<Point> group) {
        if (p.x < 0 || p.y < 0 || p.x >= size || p.y >= size || board[p.x][p.y] != color || group.contains(p)) {
            return;
        }

        group.add(p);

        exploreGroup(new Point(p.x-1, p.y), color, group);
        exploreGroup(new Point(p.x+1, p.y), color, group);
        exploreGroup(new Point(p.x, p.y-1), color, group);
        exploreGroup(new Point(p.x, p.y+1), color, group);
    }

    private int countLiberties(Set<Point> group) {
        Set<Point> liberties = new HashSet<>();
        for (Point p : group) {
            addLiberty(liberties, new Point(p.x-1, p.y));
            addLiberty(liberties, new Point(p.x+1, p.y));
            addLiberty(liberties, new Point(p.x, p.y-1));
            addLiberty(liberties, new Point(p.x, p.y+1));
        }
        return liberties.size();
    }

    private void addLiberty(Set<Point> liberties, Point p) {
        if (p.x >= 0 && p.y >= 0 && p.x < size && p.y < size && board[p.x][p.y] == Stone.EMPTY) {
            liberties.add(p);
        }
    }

    private void removeGroup(Set<Point> group) {
        for (Point p : group) {
            if (board[p.x][p.y] == Stone.BLACK) white.addCaptures(1);
            else white.addCaptures(1);
            board[p.x][p.y] = Stone.EMPTY;
        }
    }

}
