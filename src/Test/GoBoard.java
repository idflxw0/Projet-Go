//package Test;
//
//import java.util.*;
//
//public class GoBoard {
//    private static final int EMPTY = 0;
//    private static final int BLACK = 1;
//    private static final int WHITE = 2;
//    private int[][] board;
//    private int size;
//
//    public GoBoard(int size) {
//        this.size = size;
//        board = new int[size][size];
//    }
//
//    public boolean addStone(int x, int y, int color) {
//        if (board[x][y] != EMPTY) {
//            return false; // Cannot place a stone on an occupied point
//        }
//
//        // Place the stone temporarily
//        board[x][y] = color;
//
//        List<Set<Point>> friendGroups = new ArrayList<>();
//        List<Set<Point>> enemyGroups = new ArrayList<>();
//        int enemyColor = (color == BLACK) ? WHITE : BLACK;
//
//        // Check adjacent points
//        if (x > 0) checkAdjacent(new Point(x-1, y), color, friendGroups, enemyGroups);
//        if (y > 0) checkAdjacent(new Point(x, y-1), color, friendGroups, enemyGroups);
//        if (x < size - 1) checkAdjacent(new Point(x+1, y), color, friendGroups, enemyGroups);
//        if (y < size - 1) checkAdjacent(new Point(x, y+1), color, friendGroups, enemyGroups);
//
//        // Check for captures
//        for (Set<Point> group : enemyGroups) {
//            if (countLiberties(group) == 0) {
//                removeGroup(group);
//            }
//        }
//
//        // Check if the placed stone is in atari (has no liberties)
//        if (countLiberties(getGroup(new Point(x, y), color)) == 0) {
//            board[x][y] = EMPTY; // Remove the placed stone
//            return false;
//        }
//
//        return true;
//    }
//
//    private void checkAdjacent(Point p, int color, List<Set<Point>> friendGroups, List<Set<Point>> enemyGroups) {
//        if (board[p.x][p.y] == color) {
//            friendGroups.add(getGroup(p, color));
//        } else if (board[p.x][p.y] != EMPTY) {
//            enemyGroups.add(getGroup(p, board[p.x][p.y]));
//        }
//    }
//
//    private Set<Point> getGroup(Point p, int color) {
//        Set<Point> group = new HashSet<>();
//        exploreGroup(p, color, group);
//        return group;
//    }
//
//    private void exploreGroup(Point p, int color, Set<Point> group) {
//        if (p.x < 0 || p.y < 0 || p.x >= size || p.y >= size || board[p.x][p.y] != color || group.contains(p)) {
//            return;
//        }
//
//        group.add(p);
//
//        exploreGroup(new Point(p.x-1, p.y), color, group);
//        exploreGroup(new Point(p.x+1, p.y), color, group);
//        exploreGroup(new Point(p.x, p.y-1), color, group);
//        exploreGroup(new Point(p.x, p.y+1), color, group);
//    }
//
//    private int countLiberties(Set<Point> group) {
//        Set<Point> liberties = new HashSet<>();
//        for (Point p : group) {
//            addLiberty(liberties, new Point(p.x-1, p.y));
//            addLiberty(liberties, new Point(p.x+1, p.y));
//            addLiberty(liberties, new Point(p.x, p.y-1));
//            addLiberty(liberties, new Point(p.x, p.y+1));
//        }
//        return liberties.size();
//    }
//
//    private void addLiberty(Set<Point> liberties, Point p) {
//        if (p.x >= 0 && p.y >= 0 && p.x < size && p.y < size && board[p.x][p.y] == EMPTY) {
//            liberties.add(p);
//        }
//    }
//
//    private void removeGroup(Set<Point> group) {
//        for (Point p : group) {
//            board[p.x][p.y] = EMPTY;
//        }
//    }
//
//    // Point class to represent coordinates on the board
//    private static class Point {
//        int x, y;
//
//        Point(int x, int y) {
//            this.x = x;
//            this.y = y;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//            Point point = (Point) o;
//            return x == point.x && y == point.y;
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(x, y);
//        }
//    }
//
//    public void printBoard() {
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                System.out.print(board[i][j] + " ");
//            }
//            System.out.println();
//        }
//    }
//
//    public static void main(String[] args) {
//        GoBoard board = new GoBoard(9);
//        board.addStone(4, 4, BLACK);
//        board.addStone(3, 4, WHITE);
//        board.addStone(5, 4, WHITE);
//        board.addStone(4, 3, WHITE);
//        board.addStone(4, 5, WHITE);
//        // More moves can be added for testing
//        board.printBoard();
//    }
//}
//
