package Board;

public enum Stone {
    BLACK,
    WHITE,
    EMPTY;
    @Override
    public String toString() {
        return switch (this) {
            case BLACK -> "x";
            case WHITE -> "o";
            case EMPTY -> ".";
            default -> throw new IllegalArgumentException();
        };
    }
}
