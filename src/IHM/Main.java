package IHM;

import GO.Board.Board;
import GO.Board.IBoard;

import java.util.Scanner;

public class Main {
    private static IBoard board = null;
    private static int commandCount = 0;
    private static boolean hasCommandCount;
    private static final int MIN_SIZE = 2;
    private static final int MAX_SIZE = 26;
    private static final int DEFAULT_SIZE = 19;
    private static final int INDEX_WITHOUT_COMMAND_COUNT = 1;
    private static final int COMMAND_INDEX_WITHOUT_COMMAND_COUNT = 2;
    private static final int COLOR_INDEX_WITH_COMMAND_COUNT = 2;
    private static final int COMMAND_INDEX_WITH_COMMAND_COUNT = 3;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();
            if (processInput(input)) break;
        }
        scanner.close();
    }

    private static boolean processInput(String input) {
        String[] commands = input.split(" ");

        if (commands[0].matches("\\d+")) {
            commandCount = Integer.parseInt(commands[0]);
            hasCommandCount = true;
        } else {
            hasCommandCount = false;
        }

        if ((hasCommandCount && input.equals("quit")) || commands[0].equals("quit")) {
            manageCommands("QUIT");
            return true;
        }

        if (input.contains("boardsize")) {
            initBoard(input);
        } else {
            useCommands(input);
        }
        return false;
    }

    public static void initBoard(String input) {
        String[] inputArray = input.split(" ");
        if (inputArray.length > 1) {
            int size;
            try {
                size = hasCommandCount ? Integer.parseInt(inputArray[2]) : Integer.parseInt(inputArray[1]);
            } catch (IndexOutOfBoundsException e) {
                manageCommands("NOT_INTEGER");
                return;
            }
            if (size < MIN_SIZE || size > MAX_SIZE) {
                manageCommands("SIZE_ERROR");
            } else {
                board = new Board(size);
                manageCommands("SUCCESS");
            }
        } else {
            manageCommands("NOT_INTEGER");
        }
    }

    public static void useCommands(String input) {
        String[] inputArray = input.split(" ");
        if (board == null) {
            board = new Board(DEFAULT_SIZE);
        }
        if (inputArray.length >= 1) {
            if (!hasCommandCount && input.equals("showboard") || inputArray.length > 1 && inputArray[INDEX_WITHOUT_COMMAND_COUNT].equals("showboard")) {
                manageCommands("SHOW_BOARD");
                board.showBoard();
            } else if (!hasCommandCount && input.equals("clear_board") ||(inputArray.length > 1 && inputArray[INDEX_WITHOUT_COMMAND_COUNT].equals("clear_board")) ) {
                board.clearBoard();
                manageCommands("SUCCESS");
            } else if (input.contains("play") && inputArray.length > 2) {
                int colorIndex = hasCommandCount ? COLOR_INDEX_WITH_COMMAND_COUNT : INDEX_WITHOUT_COMMAND_COUNT;
                int commandIndex = hasCommandCount ? COMMAND_INDEX_WITH_COMMAND_COUNT : COMMAND_INDEX_WITHOUT_COMMAND_COUNT;
                if (inputArray.length > commandIndex) {
                    String color = inputArray[colorIndex];
                    String command = inputArray[commandIndex];
                    String playedSituation = board.play(color, command);
                    manageCommands(playedSituation);
                } else manageCommands("INCORRECT_PLAY");

            } else manageCommands("UNKNOWN_COMMAND");

        } else manageCommands("");
    }

    public static void manageCommands(String event) {
        switch (event) {
            case "SUCCESS", "QUIT" -> {
                if (hasCommandCount) {
                    System.out.println("=" + commandCount);
                } else {
                    System.out.println("=");
                }
            }
            case "SHOW_BOARD" -> System.out.println("=");
            case "SIZE_ERROR" -> {
                if (hasCommandCount) {
                    System.out.println("?" + commandCount + " unacceptable size");
                } else {
                    System.out.println("? unacceptable size");
                }
            }
            case "NOT_INTEGER" -> {
                if (hasCommandCount) {
                    System.out.println("?" + commandCount + " boardsize not an integer");
                } else {
                    System.out.println("?" + " boardsize not an integer");
                }
            }
            case "UNKNOWN_COMMAND" -> {
                if (hasCommandCount) {
                    System.out.println("?" + commandCount + " unknown command");
                } else {
                    System.out.println("? unknown command");
                }
            }
            case "INCORRECT_PLAY" -> {
                if (hasCommandCount) {
                    System.out.println("?" + commandCount + " invalid color or coordinate");
                } else {
                    System.out.println("? invalid color or coordinate");
                }
            }
            case "ILLEGAL_MOVE" -> {
                if (hasCommandCount) {
                    System.out.println("?" + commandCount + " illegal move");
                } else {
                    System.out.println("? illegal move");
                }
            }
            default -> System.out.println("?" + commandCount);
        }
    }
}