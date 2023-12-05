package GO.Players;

public class Black implements IPlayer{
    private int blackCaptures;
    private boolean hasPlayed;
    public Black() {
        blackCaptures = 0;
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

        this.blackCaptures += captures;
    }

    @Override
    public int getCaptures() {
        return this.blackCaptures;
    }
}
