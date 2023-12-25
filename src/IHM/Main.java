package IHM;

import GO.Board.Board;
import GO.Board.IBoard;
import GO.Board.Stone;
import GO.Players.AI;
import GO.Players.Player;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InputHandler inputHandler = new InputHandler();
        while (true) {
            String input = scanner.nextLine().toLowerCase();
            if (inputHandler.processInput(input)) break;
        }
        scanner.close();
    }
}