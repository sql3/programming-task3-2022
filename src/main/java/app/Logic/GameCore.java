package app.Logic;

import java.util.ArrayList;
import java.util.List;

import static app.Logic.Board.*;
import static app.Logic.ChipColor.*;

public class GameCore {

    private final Board board = new Board();

    private ChipColor queue = WHITE;

    //private Pair<Integer, Integer> dice; не подходит для случая "куш"

    private List<Integer> dice = new ArrayList<>();

    public boolean fromHead = false;

    private int numberOfMoves = 0;

    private boolean throwingOutBlack;
    private boolean throwingOutWhite;

    private Listener listener;

    public Board getBoard() {
        return board;
    }

    public GameCore setListener (Listener l) {
        listener = l;
        return this;
    }

    public ChipColor getQueue() {
        return queue;
    }

    public ArrayList<Integer> getDice() {
        return new ArrayList<>(dice);
    }

    public void roll() {
        if (dice.isEmpty()) {
            dice.add(Dice.rollTheDice());
            dice.add(Dice.rollTheDice());

            if (dice.get(0).equals(dice.get(1))) {
                dice.add(dice.get(0));
                dice.add(dice.get(0));
            }

            numberOfMoves++;
            fromHead = false;
        }
    }

    public void skipMove() {
        dice = new ArrayList<>();
        queue = queue.changeColor();
    }

    public List<Integer> getPossibleMoves(int start) {
        List<Integer> moves = new ArrayList<>();
        ChipColor chipColor = board.getPosition(start).getChipColor();

        if (dice.isEmpty() || chipColor == NO_COLOR) {
            return moves;
        }

        if (fromHead && (start == blackHeadPosition || start == whiteHeadPosition)) {
            return moves;
        }

        if (dice.size() >= 2) {
            addCorrectMove(start + dice.get(0), chipColor, moves);
            addCorrectMove(start + dice.get(1), chipColor, moves);
            addCorrectMove(start + dice.get(0) + dice.get(1), chipColor, moves);
        } else {
            addCorrectMove(start + dice.get(0), chipColor, moves);
        }

        List<Integer> finishRemove = new ArrayList<>();

        boolean throwingOut = chipColor == WHITE ? throwingOutWhite : throwingOutBlack;

        if (!throwingOut) {
            if (chipColor == WHITE) {
                for (int i : moves) {
                    if (start >= blackHeadPosition && i < blackHeadPosition) {
                        finishRemove.add(i);
                    }
                }
            }

            if (chipColor == BLACK) {
                for (int i : moves) {
                    if (start < blackHeadPosition && i >= blackHeadPosition) {
                        finishRemove.add(i);
                    }
                }
            }
        }

        for (int i : moves) {
            if (i != ejection) {
                if (board.isBlock(start, i)) {
                    finishRemove.add(i);
                }
            }
        }

        moves.removeAll(finishRemove);


        if (moves.size() > 0) {
            if (!moves.contains((start + dice.get(0)) % numberOfPositions) &&
            !moves.contains((start + dice.get(1)) % numberOfPositions)) {
                moves.remove((Object)((start + dice.get(0) + dice.get(1)) % numberOfPositions));
            }
        }

        return moves;
    }

    private void addCorrectMove (int finish, ChipColor chipColor, List<Integer> moves) {
        ChipColor finishColor = board.getPosition(finish).getChipColor();
        boolean throwingOut = chipColor == WHITE ? throwingOutWhite : throwingOutBlack;

        if ((finishColor == chipColor || finishColor == NO_COLOR) && !throwingOut) {
            moves.add(finish % numberOfPositions);
        }

        if (throwingOutWhite && chipColor == WHITE) {
            if (finish >= numberOfPositions) {
                moves.add(ejection);
            }
            else {
                moves.add(finish);
            }
        }

        if (throwingOutBlack && chipColor == BLACK) {
            if (finish >= blackHeadPosition) {
                moves.add(ejection);
            } else {
                moves.add(finish);
            }
        }

    }

    public void makeMove(int start, int finish) {
        board.moveTheChip(start, finish);

        if (dice.size() >= 2) { // ????????????
            int finishSum = dice.get(0) + dice.get(1);

            int box = finish - start < 0 ? (24 - start + finish) : (finish - start); //?????????

            if (throwingOutBlack && finish == ejection && queue == BLACK) {
                box -= 12;
            }

            if (box == finishSum) {
                /*dice.clear(); не подойдет для "куш" */
                dice.remove(0);
                dice.remove(0);
                if (dice.isEmpty()) {
                    queue = queue.changeColor();
                }
            } else if (box == dice.get(0)) {
                dice.remove(0);
            } else if (box == dice.get(1)) {
                dice.remove(1);
            } else {
                boolean throwingOut = queue == WHITE ? throwingOutWhite : throwingOutBlack;
                if (throwingOut) {
                    int minMove = dice.get(0) < dice.get(1) ? dice.get(0) : dice.get(1);
                    int maxMove = dice.get(0) > dice.get(1) ? dice.get(0) : dice.get(1);

                    if (box <= minMove) {
                        dice.remove(minMove);
                    } else if (box <= maxMove) {
                        dice.remove(maxMove);
                    } else {
                        dice.remove(minMove);
                        dice.remove(maxMove);
                        queue = queue.changeColor();
                    }
                }
            }

        } else if (dice.size() == 1) {
            dice.remove(0);
            queue = queue.changeColor();
        }

        if ((queue == BLACK && start == blackHeadPosition) || (queue == WHITE && start == whiteHeadPosition)) {
            fromHead = true;
        }

        if (numberOfMoves <= 2 && getPossibleMoves(finish).isEmpty()) {
            if (queue == BLACK && board.getPosition(blackHeadPosition).getQuantity() > 13) {
                fromHead = false;
            }
            if (queue == WHITE && board.getPosition(whiteHeadPosition).getQuantity() > 13) {
                    fromHead = false;
            }
        }

        accessEjection();
        listener.update();
    }

    public void accessEjection() {
        boolean black = true;
        boolean white = true;

        for (int position = blackHeadPosition; position < blackHeadPosition + 18; position++) {
            if (board.getPosition(position).getChipColor() == BLACK) black = false;
        }

        for (int position = whiteHeadPosition; position < whiteHeadPosition + 18; position++) {
            if (board.getPosition(position).getChipColor() == WHITE) white = false;
        }

        throwingOutBlack = black;
        throwingOutWhite = white;
    }

    public int getChipsInPostion(int position) {
        return board.getPosition(position).getQuantity();
    }

    public boolean noDice() {
        return dice.isEmpty();
    }

    //for Test
    public void setDice(List<Integer> dice) {
        this.dice = dice;
    }
}
