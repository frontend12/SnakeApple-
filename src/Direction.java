enum Direction {
    LEFT(false), UP(true), RIGHT(false), DOWN(true);

    private final boolean isVertical;

    Direction(boolean isVertical) {
        this.isVertical = isVertical;
    }

    boolean canChangeDirectionTo(Direction d) {
        switch (this) {
            case LEFT:
            case RIGHT:
                return d.isVertical;
            case UP:
            case DOWN:
                return !d.isVertical;
        }
        return true;
    }
}