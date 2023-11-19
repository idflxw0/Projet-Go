package Board;

public enum Stone {
    BLACK,
    WHITE,
    EMPTY;
    @Override
    public String toString() {
        return switch (this) {
            case BLACK -> "B";
            case WHITE -> "W";
            case EMPTY -> "."; // Using '+' or any other character to represent an empty space
            default -> throw new IllegalArgumentException();
        };
    }
}
