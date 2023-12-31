package GO.Players;


import GO.Board.IBoard;
import GO.Board.Stone;

import java.awt.Point;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class AI extends Player{
    Random random;
    public AI(Stone stone,Boolean turn) {
        super(stone,turn);
        random = new Random();
    }

   /* public Point placeStoneRandomly(IBoard board, Stone color) {
        Random random = new Random();
        int size = board.getSize();
        int maxAttempts = size * size; // Limit the number of attempts to prevent infinite loop
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            int x = random.nextInt(size); // Random row
            int y = random.nextInt(size); // Random column

            if (board.isPlaceable(x, y) && board.placeStones(x + 1, (char) ('A' + y), color)) {
                return new Point(x, y);
            }
        }
        return null; // No legal move found after maxAttempts
    }*/

    public Point placeStoneRandomly(IBoard board, Stone color) {
        int size = board.getSize();
        List<Point> emptyPositions = new ArrayList<>();

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (board.isPlaceable(x, y)) {
                    emptyPositions.add(new Point(x, y));
                }
            }
        }
        Collections.shuffle(emptyPositions, new Random());
        for (Point pos : emptyPositions) {
            if (board.placeStones(pos.x + 1, (char) ('A' + pos.y), color)) {
                return pos;
            }
        }
        return null;
    }

}
