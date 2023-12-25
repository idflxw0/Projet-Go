package GO.Players;


import GO.Board.Stone;

public class AI extends Player{
    public AI(Stone stone,Boolean turn) {
        super(stone,turn);
    }

    public int[] placeStonesRandomly(Stone[][] board) {
        int size = board.length;
        int x = (int) (Math.random() * size);
        int y = (int) (Math.random() * size);
        while (board[x][y] != Stone.EMPTY) {
            x = (int) (Math.random() * size);
            y = (int) (Math.random() * size);
        }
       return new int[]{x,y};
    }

}
