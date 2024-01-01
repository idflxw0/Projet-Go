package GO.Board;
import org.junit.jupiter.api.Test;
import java.awt.Point;


class BoardTest {

    @Test
    void testPlaceStonesRandomly() {
        Board board = new Board(2); // Initialize a 4x4 board

        /*for (int i = 0; i < 8; i++) { // Try to place 8 stones (4 white, 4 black)
            Stone stoneColor = (i % 2 == 0) ? Stone.WHITE : Stone.BLACK;
            Point placedPoint = board.placeStoneRandomly(stoneColor);

            if (placedPoint != null) {
                // Convert the point to a human-readable format (e.g., D2)
                char column = (char) ('A' + placedPoint.y);
                int row = placedPoint.x  + 1;
                System.out.println("Player " + (stoneColor == Stone.WHITE ? "white" : "black") + " placed on " + column + row);
            } else {
                System.out.println("No legal move found for " + (stoneColor == Stone.WHITE ? "white" : "black"));
            }

            board.showBoard(); // Show the board after each placement
        }*/
    }

}