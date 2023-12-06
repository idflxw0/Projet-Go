package GO.Players;

public class White extends Player{
    public White() {
        super();
    }
    @Override
    public void addCaptures(int captures) {
        this.captures += captures;
    }
}
