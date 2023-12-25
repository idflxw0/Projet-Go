package IHM;

import GO.Board.Board;
import GO.Board.IBoard;
import GO.Players.AI;
import jdk.jshell.spi.SPIResolutionException;

public class InputHandler {
    private static IBoard board; //board of the game
    private static int commandCount; //number of commands
    private static boolean hasCommandCount; //true if the command has a command number, false otherwise

    //SPLIT CONSTANTS
    private static final int CHECK_HAS_COMMAND = 0;
    private static final int CORRECT_ARRAY_LENGTH = 1;


    //BOARD INIT CONSTANTS
    private static final int INDEX_WITHOUT_COMMAND_COUNT = 1;
    private static final int INDEX_WITH_COMMAND_COUNT = 2;

    //PLAY CONSTANTS
    private static final int AI_COMMAND_INDEX = 2;
    private static final int AI_COLOR_INDEX = 1;
    private static final int AI_COMMAND_INDEX_WITH_COMMAND_COUNT =3;
    private static final int AI_COLOR_INDEX_WITH_COMMAND_COUNT = 2;
    private static final int CORRECT_PLAY_ARRAY_LENGTH = 2;
    private static final int PLAY_COMMAND_INDEX_WITHOUT_COMMAND_COUNT = 2;
    private static final int COLOR_INDEX_WITH_COMMAND_COUNT = 2;
    private static final int COMMAND_INDEX_WITH_COMMAND_COUNT = 3;

    public InputHandler() {
        board = new Board();
        commandCount = 0;
        hasCommandCount = false;
    }

    /**
     * Process the input
     * @param input : input of the user
     * @return : true if the input is quit, false otherwise
     */
    public boolean processInput(String input) {

        String[] commands = input.split(" ");

        if (commands[CHECK_HAS_COMMAND].matches("\\d+")) {
            commandCount = Integer.parseInt(commands[CHECK_HAS_COMMAND]);
            hasCommandCount = true;
        } else {
            hasCommandCount = false;
        }

        if ((!hasCommandCount && input.equals("quit"))|| hasCommandCount && commands[INDEX_WITHOUT_COMMAND_COUNT].equals("quit") ) {
            toString("QUIT");
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
    private void initBoard(String input) {
        String[] inputArray = input.split(" ");
        if (inputArray.length > CORRECT_ARRAY_LENGTH) {
            int size;
            try {
                size = hasCommandCount ? Integer.parseInt(inputArray[INDEX_WITH_COMMAND_COUNT])
                        : Integer.parseInt(inputArray[INDEX_WITHOUT_COMMAND_COUNT]);

            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e)  {
                toString("NOT_INTEGER");
                return;
            }
            try {
                board = new Board(size);
                toString("SUCCESS");
            } catch (IllegalArgumentException e) {
                toString("SIZE_ERROR");
            }
        } else {
            toString("NOT_INTEGER");
        }
    }

    /**
     * Use the commands
     * @param input : input of the user
     */
    private void useCommands(String input) {
        String[] inputArray = input.split(" ");
        boolean correct_length = inputArray.length > CORRECT_ARRAY_LENGTH;
        if (inputArray.length >= CORRECT_ARRAY_LENGTH) {
            if (!hasCommandCount && input.equals("showboard")
                    || correct_length && inputArray[INDEX_WITHOUT_COMMAND_COUNT].equals("showboard"))
            {
                toString("SHOW_BOARD");
                board.showBoard();
            }
            else if (!hasCommandCount && input.equals("clear_board")
                    ||(correct_length && inputArray[INDEX_WITHOUT_COMMAND_COUNT].equals("clear_board")) )
            {
                board.clearBoard();
                toString("SUCCESS");
            }
            else if (input.contains("play")) {
                play(inputArray);
            }
            else toString("UNKNOWN_COMMAND");

        } else toString("UNKNOWN_COMMAND");
    }

    /**
     * Play : let us place a stone on the board with the color and the position
     * @param inputArray : input array of the command
     */
    private void play(String[] inputArray) {
        if (inputArraysContains(inputArray,"console")) if (play_with_AI(inputArray)) return;

        int colorIndex = hasCommandCount ?
                COLOR_INDEX_WITH_COMMAND_COUNT :INDEX_WITHOUT_COMMAND_COUNT;
        int commandIndex = hasCommandCount ?
                COMMAND_INDEX_WITH_COMMAND_COUNT : PLAY_COMMAND_INDEX_WITHOUT_COMMAND_COUNT;
        String color = inputArray[colorIndex];
        String command = inputArray[commandIndex];
        String playedSituation = board.play(color, command);
        toString(playedSituation);
    }

    public boolean inputArraysContains(String[] inputArray, String word) {
        if (inputArray.length < 2) return false;
        for (String words : inputArray) {
            if (word.equals(words)) {
                return true;
            }
        }
        return false;
    }

    private boolean play_with_AI(String[] inputArray) {
        String color = hasCommandCount ? inputArray[AI_COLOR_INDEX_WITH_COMMAND_COUNT] : inputArray[AI_COLOR_INDEX];
        String output = board.playBot(color);
        if (output.equals("SUCCESS")) {
            toString(output);
            return true;
        }

        return false;
    }


    /**
     * Manage the commands
     * @param event : event of the command
     */
    private void toString(String event) {
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
