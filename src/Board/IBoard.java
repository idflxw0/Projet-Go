/**
 * IBoard interface
 * @since 18/11/2023
 */
package Board;

public interface IBoard {
    void showBoard();
    void clearBoard();
    boolean play(String color, String pos);
    boolean isplaceable(int row, int column);
}
