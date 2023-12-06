package GO.Players;

public class Player implements IPlayer{
    protected int captures;
    public Player() {
        captures = 0;
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
}
