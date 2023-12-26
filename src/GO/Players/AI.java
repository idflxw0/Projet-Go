package GO.Players;


import GO.Board.Stone;

import java.awt.Point;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class AI extends Player{
    Random random;
    public AI(Stone stone,Boolean turn) {
        super(stone,turn);
        random = new Random();
    }

    public Point placeStonesRandomly(Stone[][] board) {
        int size = board.length;
        List<Point> emptySpots = new ArrayList<>();

        // Collect all empty spots
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (board[x][y] == Stone.EMPTY) {
                    emptySpots.add(new Point(x, y));
                }
            }
        }

        // If there are no empty spots, return null or handle appropriately
        if (emptySpots.isEmpty()) {
            return null; // or handle this case as per your game's rules
        }

        // Select a random empty spot
        int index = random.nextInt(emptySpots.size());
        return emptySpots.get(index);
    }

}
