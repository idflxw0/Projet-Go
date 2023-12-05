package GO.Players;

public class White implements IPlayer{
    private int whiteCaptures;
    private boolean hasPlayed;

    public White() {
        whiteCaptures = 0;
        hasPlayed = false;
    }


    @Override
    public boolean isPlaying(boolean situation) {
        return hasPlayed;
    }

    @Override
    public boolean getSituation() {
        return this.hasPlayed;
    }

    @Override
    public void addCaptures(int captures) {
        this.whiteCaptures += captures;
    }

    @Override
    public int getCaptures() {
        return this.whiteCaptures;
    }
}
