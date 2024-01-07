package IHM;

import GO.Board.Board;
import GO.Board.IBoard;
import GO.Board.Stone;

public class InputHandler {
    private static IBoard board; //Board of the game
    private static int commandCount; //Number of commands
    private static boolean hasCommandCount; //condition : True if the command has a command number, false otherwise

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
    private static boolean whiteAI;
    private static boolean blackAI;

    public InputHandler() {
        board = new Board();
        commandCount = 0;
        hasCommandCount = false;
        whiteAI = false;
        blackAI = false;
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
                whiteAI = false;
                blackAI = false;
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
            else if (input.contains("player")) {
                this.selectPlayer(inputArray);
            }
            else if ((whiteAI && blackAI) && input.equals("play")) {
                playAIvsAI();
            }
            else if (inputArraysContains(inputArray,"play")) {
//                board.placeStone(3,3, Stone.BLACK);
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
        try {
            int colorIndex = hasCommandCount ?
                    COLOR_INDEX_WITH_COMMAND_COUNT :INDEX_WITHOUT_COMMAND_COUNT;
            int commandIndex = hasCommandCount ?
                    COMMAND_INDEX_WITH_COMMAND_COUNT : PLAY_COMMAND_INDEX_WITHOUT_COMMAND_COUNT;
            String color = inputArray[colorIndex];
            String command = inputArray[commandIndex];
            toString(board.play(color, command));
        } catch (ArrayIndexOutOfBoundsException e) {
            toString("INCORRECT_PLAY");
        }
    }

    /**
     * Check if the input array contains the word
     * @param inputArray : input array of the command
     * @param word : word to check
     * @return : true if the input array contains the word, false otherwise
     */
    public boolean inputArraysContains(String[] inputArray, String word) {
        if (inputArray.length < 2) return false;
        for (String words : inputArray) {
            if (word.equals(words)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Play with the AI
     */
    private void playAIvsAI() {
        while (true) {
            String whiteMove = board.playBot("white");
            if (isGameOver(whiteMove)) {
                toString(whiteMove);
                break;
            }
            String blackMove = board.playBot("black");
            if (isGameOver(blackMove)) {
                toString(whiteMove);
                break;
            }
        }

    }

    /**
     * Check if the game is over
     * @param moveResult : result of the move
     * @return : true if the game is over, false otherwise
     */
    private boolean isGameOver(String moveResult) {
        return moveResult.equals("GAME OVER");
    }


    /**
     * Select the player
     * @param inputArray : input array of the command
     */
    public void selectPlayer(String[] inputArray) {
        try {
            String playerType = hasCommandCount ?
                    inputArray[AI_COMMAND_INDEX_WITH_COMMAND_COUNT] : inputArray[AI_COMMAND_INDEX];
            String color = hasCommandCount ?
                    inputArray[AI_COLOR_INDEX_WITH_COMMAND_COUNT] : inputArray[AI_COLOR_INDEX];
            switch (playerType) {
                case "console":
                    if (color.equals("white")) whiteAI = false;
                    else blackAI = false;
                    break;
                case "random":
                    play_with_AI(inputArray);
                    break;
                default:
                    toString("UNKNOWN_PLAYER");
                    break;
            }
        } catch (ArrayIndexOutOfBoundsException a) {
            toString("UNKNOWN_PLAYER");
        }

    }


    /**
     * Play with the AI
     * @param inputArray : input array of the command
     * @return : true if the AI has played, false otherwise
     */
    private boolean play_with_AI(String[] inputArray) {
        try {
            String color = hasCommandCount ? inputArray[AI_COLOR_INDEX_WITH_COMMAND_COUNT] : inputArray[AI_COLOR_INDEX];
            if (color.equals("white")) whiteAI = true;
            else blackAI = true;
            String output = board.playBot(color);
            if (output.contains("SUCCESS")) toString(output);
            return output.contains("played on");
        } catch (ArrayIndexOutOfBoundsException e) {
            toString("INCORRECT_PLAY");
            return false;
        }
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
            case "NOT_YOUR_TURN" -> {
                if (hasCommandCount) {
                    System.out.println("?" + commandCount + " it is not your turn");
                }
                else {
                    System.out.println("? it is not your turn");
                }
            }
            case "UNKNOWN_PLAYER" -> {
                if (hasCommandCount) {
                    System.out.println("?"+commandCount + " incorrect player selection");
                } else {
                    System.out.println("? incorrect player selection");
                }
            }
            case "GAME OVER" -> {
                if (hasCommandCount) {
                    System.out.println(commandCount + " GAME OVER! THERE IS NO EMPTY PLACES");
                }
                else {
                    System.out.println( "GAME OVER! THERE IS NO EMPTY PLACES");
                }
            }
            default -> System.out.println("?" + commandCount);
        }
    }
}
