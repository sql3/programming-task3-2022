package app.Logic;

import java.util.ArrayList;
import java.util.List;

import static app.Logic.ChipColor.*;

public class Board {

    public static final int numberOfPositions = 24;

    public static final int blackHeadPosition = 12;

    public static final int whiteHeadPosition = 0;

    public static final int ejection = 24;

    private final List<Chips> boardPositions = new ArrayList<>();

    public Board() {
        for (int position = 0; position < numberOfPositions; position++) {
            boardPositions.add(new Chips());
        }

        getPosition(blackHeadPosition).setChipColor(BLACK).setQuantity(15);
        getPosition(whiteHeadPosition).setChipColor(WHITE).setQuantity(15);
    }

    public Chips getPosition(int position) { //Может быть ошибка
        if (position < 0) {
            position = numberOfPositions + position % numberOfPositions;
        }

        if (position >= numberOfPositions){
            position %= numberOfPositions;
        }
        return boardPositions.get(position);
    }

    public void moveTheChip (int start, int finish) {
        if (getPosition(start).getQuantity() == 0 || getPosition(start).getChipColor() == NO_COLOR) {
            throw new IllegalArgumentException("Move from an empty position");
        }

        if (finish == ejection) {
            getPosition(start).reduceQuantity();
            return;
        }

        if (start >= numberOfPositions) {
            start %= numberOfPositions;
        }

        if (finish > numberOfPositions) { //Может быть ошибка
            finish %= numberOfPositions;
        }

        getPosition(finish).increaseQuantity(getPosition(start).getChipColor());
        getPosition(start).reduceQuantity();
    }

    public boolean isBlock(int start, int finish) {
        ChipColor color = getPosition(start).getChipColor();

        getPosition(start).reduceQuantity();
        getPosition(finish).increaseQuantity(color);

        int sequence = 0;
        boolean flag = false;

        if (color == BLACK) {
            for (int position = whiteHeadPosition; position < whiteHeadPosition + numberOfPositions; position++) {
                if (getPosition(position).getChipColor() == color) {
                    sequence++;
                } else {
                    sequence = 0;
                }

                if (sequence > 5) {
                    flag = true;

                    for (int i = position; i < numberOfPositions; i++) {
                        if (getPosition(i).getChipColor() == WHITE) {
                            flag = false;
                        }
                    }
                }
            }
        }
        if (color == WHITE) {
            for (int position = blackHeadPosition; position < blackHeadPosition + numberOfPositions; position++) {
                if (getPosition(position).getChipColor() == color) {
                    sequence++;
                } else {
                    sequence = 0;
                }

                if (sequence > 5) {
                    flag = true;

                    for (int i = position; i < numberOfPositions; i++) {
                        if (getPosition(i).getChipColor() == WHITE) {
                            flag = false;
                        }
                    }
                }
            }
        }

        getPosition(start).increaseQuantity(color);
        getPosition(finish).reduceQuantity();
        return flag;
    }

    public ChipColor determineTheWinner() {
        boolean black = true;
        boolean white = true;

        for (int position = 0; position < numberOfPositions; position++) {
            if (getPosition(position).getChipColor() == BLACK) {
                black = false;
            }

            if (getPosition(position).getChipColor() == WHITE) {
                white = false;
            }
        }

        if (black) return BLACK;
        if (white) return WHITE;
        return null;
    }

    //for Test
    public void setPosition(int position, Chips chips) {
        if (position >= numberOfPositions) {
            position %= numberOfPositions;
        }
        boardPositions.set(position, chips);
    }
}
