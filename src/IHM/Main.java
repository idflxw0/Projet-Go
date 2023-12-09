package IHM;

import GO.Board.Board;
import GO.Board.IBoard;

import java.util.Scanner;

public class Main {
    private static IBoard board = null; //board of the game
    private static int commandCount = 0; //number of commands
    private static boolean hasCommandCount; //true if the command has a command number, false otherwise

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();
            if (processInput(input)) break;
        }
        scanner.close();
    }

    /**
     * Process the input
     * @param input : input of the user
     * @return : true if the input is quit, false otherwise
     */
    private static boolean processInput(String input) {
        String[] commands = input.split(" ");

        if (commands[0].matches("\\d+")) {
            commandCount = Integer.parseInt(commands[0]);
            hasCommandCount = true;
        } else {
            hasCommandCount = false;
        }

        if ((!hasCommandCount && input.equals("quit"))|| hasCommandCount && commands[1].equals("quit") ) {
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

    /**
     * Initialize the board
     * @param input : input of the user
     */
    public static void initBoard(String input) {
        String[] inputArray = input.split(" ");
        if (inputArray.length > Constants.CORRECT_ARRAY_LENGTH) {
            int size;
            try {
                size = hasCommandCount ? Integer.parseInt(inputArray[Constants.INDEX_WITH_COMMAND_COUNT])
                        : Integer.parseInt(inputArray[Constants.INDEX_WITHOUT_COMMAND_COUNT]);

            } catch (IndexOutOfBoundsException e) {
                manageCommands("NOT_INTEGER");
                return;
            }
            if (size < Constants.BOARD_MIN_SIZE || size > Constants.BOARD_MAX_SIZE) {
                manageCommands("SIZE_ERROR");
            } else {
                board = new Board(size);
                manageCommands("SUCCESS");
            }
        } else {
            manageCommands("NOT_INTEGER");
        }
    }

    /**
     * Use the commands
     * @param input : input of the user
     */
    public static void useCommands(String input) {
        String[] inputArray = input.split(" ");
        boolean correct_length = inputArray.length > Constants.CORRECT_ARRAY_LENGTH;
        if (board == null) {
            board = new Board(Constants.BOARD_DEFAULT_SIZE);
        }
        if (inputArray.length >= Constants.CORRECT_ARRAY_LENGTH) {
            if (!hasCommandCount && input.equals("showboard")
                    || correct_length && inputArray[Constants.INDEX_WITHOUT_COMMAND_COUNT].equals("showboard"))
            {
                manageCommands("SHOW_BOARD");
                board.showBoard();
            }
            else if (!hasCommandCount && input.equals("clear_board")
                    ||(correct_length && inputArray[Constants.INDEX_WITHOUT_COMMAND_COUNT].equals("clear_board")) )
            {
                board.clearBoard();
                manageCommands("SUCCESS");
            }
            else if (input.contains("play") || inputArray.length > Constants.CORRECT_PLAY_ARRAY_LENGTH) {
               play(inputArray);
            }
            else manageCommands("UNKNOWN_COMMAND");

        } else manageCommands("");
    }

    /**
     * Play : let us place a stone on the board with the color and the position
     * @param inputArray : input array of the command
     */
    private static void play(String[] inputArray) {
        int colorIndex = hasCommandCount ?
                Constants.COLOR_INDEX_WITH_COMMAND_COUNT : Constants.INDEX_WITHOUT_COMMAND_COUNT;
        int commandIndex = hasCommandCount ?
                Constants.COMMAND_INDEX_WITH_COMMAND_COUNT : Constants.PLAY_COMMAND_INDEX_WITHOUT_COMMAND_COUNT;

        if (inputArray.length > commandIndex) {
            String color = inputArray[colorIndex];
            String command = inputArray[commandIndex];

            String playedSituation = board.play(color, command);

            manageCommands(playedSituation);

        } else manageCommands("INCORRECT_PLAY");
    }

    /**
     * Manage the commands
     * @param event : event of the command
     */
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