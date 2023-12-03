/**
 * IBoard interface
 * @since 18/11/2023
 */
package GO.Board;

public interface IBoard {
    void showBoard();
    void clearBoard();
    String play(String color, String pos);
    boolean isPlaceable(int row, int column);
}