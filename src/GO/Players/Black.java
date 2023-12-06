package GO.Players;

public class Black extends Player{
    public Black() {
        super();
    }
    @Override
    public void addCaptures(int captures) {
        this.captures += captures;
    }
}
