/**
 * IBoard interface
 * @since 18/11/2023
 */
package GO.Board;

public interface IBoard {
    void showBoard();
    void clearBoard();
    String play(String color, String pos);
    String playBot(String color);
    boolean isPlaceable(int row, int column);
    boolean placeStones(int row, char column, Stone color);
    public boolean placeStone(int x, int y, Stone color);
    Stone[][] getBoard();
    int getSize();
    int getNbLiberties(int x, int y);
}