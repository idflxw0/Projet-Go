package Test.BoardTest;

import GO.Board.Board;
import GO.Board.IBoard;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    IBoard goban = new Board(5);

    @org.junit.jupiter.api.Test
    void isPlaceable() {

        // Test empty position
        assertTrue(goban.isPlaceable(0, 0), "Position (0, 0) should be placeable");

        goban.play("black", "A1");
        assertFalse(goban.isPlaceable(0, 0), "Position (0, 0) should not be placeable after placing a stone");

        // Test position outside the board boundaries
        assertFalse(goban.isPlaceable(-1, 0), "Position (-1, 0) should not be placeable");
        assertFalse(goban.isPlaceable(0, -1), "Position (0, -1) should not be placeable");
        assertFalse(goban.isPlaceable(5, 0), "Position (5, 0) should not be placeable");
        assertFalse(goban.isPlaceable(0, 5), "Position (0, 5) should not be placeable");
    }

    @org.junit.jupiter.api.Test
    void showBoard() {
        goban.clearBoard();
        assertEquals(getExpectedBoardState0(),goban.toString());
    }

    @org.junit.jupiter.api.Test
    public void play() {
        goban.clearBoard();
        // Simulate a series of plays and assert the board state
        assertEquals("SUCCESS", goban.play("black", "A2"));
        assertEquals("SUCCESS", goban.play("white", "A1"));
        assertEquals("SUCCESS", goban.play("black", "B1"));

        assertEquals(getExpectedBoardState1(), goban.toString());

        assertEquals("SUCCESS", goban.play("white", "B3"));
        assertEquals("SUCCESS", goban.play("black", "A3"));
        assertEquals("SUCCESS", goban.play("white", "B2"));
        assertEquals("SUCCESS", goban.play("black", "C1"));

        assertEquals(getExpectedBoardState2(), goban.toString());

        assertEquals("SUCCESS", goban.play("white", "C3"));
        assertEquals("SUCCESS", goban.play("black", "B4"));

        assertEquals("ILLEGAL_MOVE", goban.play("white", "B3"));

        assertEquals(getExpectedBoardState3(), goban.toString());
        assertEquals("SUCCESS", goban.play("white", "C2"));

        assertEquals("SUCCESS", goban.play("black", "D2"));
        assertEquals(getExpectedBoardState4(), goban.toString());

    }

    @org.junit.jupiter.api.Test
    void testGetNbLiberties() {

        goban.clearBoard();

        // Scenario 1: Test liberties of a single stone with no adjacent stones
        goban.play("BLACK", "C3"); // Place a black stone at C3
        assertEquals(4, goban.getNbLiberties(2, 2));

        // Scenario 2: Test liberties when adjacent to the board edge
        goban.clearBoard();
        goban.play("WHITE", "A1");
        assertEquals(2, goban.getNbLiberties(0, 0));

        // Scenario 3: Test liberties with adjacent stones
        goban.clearBoard(); // Clear the board for a new scenario
        goban.play("BLACK", "C3");
        goban.play("WHITE", "A5");
        goban.play("BLACK", "B3");
        goban.play("WHITE", "A4");
        goban.play("BLACK", "D3");
        goban.showBoard();
        assertEquals(8, goban.getNbLiberties(2, 2));
    }

    private String getExpectedBoardState0() {
        return """
                   A B C D E\s
                \s5 . . . . .  5
                \s4 . . . . .  4
                \s3 . . . . .  3
                \s2 . . . . .  2     WHITE (O) has captured 0 stones
                \s1 . . . . .  1     BLACK (X) has captured 0 stones
                   A B C D E\s
                    """;
    }

    private String getExpectedBoardState1() {
        return """
                   A B C D E\s
                \s5 . . . . .  5
                \s4 . . . . .  4
                \s3 . . . . .  3
                \s2 x . . . .  2     WHITE (O) has captured 0 stones
                \s1 . x . . .  1     BLACK (X) has captured 1 stones
                   A B C D E\s
                    """;
    }
    private String getExpectedBoardState2() {
        return """
                   A B C D E\s
                \s5 . . . . .  5
                \s4 . . . . .  4
                \s3 x o . . .  3
                \s2 x o . . .  2     WHITE (O) has captured 0 stones
                \s1 . x x . .  1     BLACK (X) has captured 1 stones
                   A B C D E\s
                    """;
    }

    private String getExpectedBoardState3() {
        return """
                   A B C D E\s
                \s5 . . . . .  5
                \s4 . x . . .  4
                \s3 x o o . .  3
                \s2 x o . . .  2     WHITE (O) has captured 0 stones
                \s1 . x x . .  1     BLACK (X) has captured 1 stones
                   A B C D E\s
                    """;
    }

    private String getExpectedBoardState4() {
        return """
                   A B C D E\s
                \s5 . . . . .  5
                \s4 . x . . .  4
                \s3 x o o . .  3
                \s2 x o o x .  2     WHITE (O) has captured 0 stones
                \s1 . x x . .  1     BLACK (X) has captured 1 stones
                   A B C D E\s
                    """;
    }

}