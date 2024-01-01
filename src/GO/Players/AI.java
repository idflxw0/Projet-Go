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


    /**
     * Place a stone randomly on the board
     * @param board the board to place the stone on
     * @param color the color of the stone to place
     * @return the position of the stone placed or null if no stone was placed
     */
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
