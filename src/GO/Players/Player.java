package GO.Players;

import GO.Board.IPlayer;
import GO.Board.Stone;

public class Player implements IPlayer {
    private Stone stone;
    protected int captures;
    private boolean Turn;
    public Player(Stone stone,boolean turn) {
        this.stone = stone;
        captures = 0;
        this.Turn = turn;
    }

    @Override
    public void addCaptures(int captures) {
        if (captures <= 0) return;
        this.captures += captures;
    }

    @Override
    public int getCaptures() {
        return this.captures;
    }
    @Override
    public void resetCaptures() {
        this.captures = 0;
    }

    @Override
    public boolean getTurn() {
        return this.Turn;
    }

    @Override
    public void setTurn(boolean turn) {
        this.Turn = turn;
    }

    @Override
    public Stone getStone() {
        return this.stone;
    }

    @Override
    public String getColor() {
        return this.getStone() == Stone.WHITE ? "white" : "black";
    }
}
