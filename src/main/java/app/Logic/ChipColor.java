package app.Logic;

public enum ChipColor {
    BLACK, WHITE, NO_COLOR;

    public ChipColor changeColor() {
        return this == WHITE ? BLACK : WHITE;
    }
}
