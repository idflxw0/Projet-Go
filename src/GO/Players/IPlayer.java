package GO.Players;

public interface IPlayer {
    boolean isPlaying(boolean situation);
    boolean getSituation();
    void addCaptures();
    int getCaptures();
}
