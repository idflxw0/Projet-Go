package GO.Board;

public interface IPlayer {
    void addCaptures(int captures);
    int getCaptures();
    void resetCaptures();
    boolean getTurn();
    void setTurn(boolean turn);
    Stone getStone();
    String getColor();
}
