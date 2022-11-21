package app;

import app.Logic.Board;
import app.Logic.Chips;
import app.Logic.GameCore;
import app.Logic.Listener;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static app.Logic.ChipColor.*;
import static org.junit.jupiter.api.Assertions.*;

public class Tests {

    private class NewListener implements Listener {
        @Override
        public void update() {}
    }

    @Test
    void checkBoard() {
        GameCore gameCore = new GameCore().setListener(new NewListener());

        Board board = gameCore.getBoard();
        //2 хода с головы
        gameCore.setDice(new ArrayList<>(List.of(5, 6)));
        gameCore.makeMove(0, 5);
        gameCore.makeMove(0, 6);
        //не должно остаться ходов
        assertTrue(gameCore.noDice());
        //после хода на голове должно остаться 13 шашек
        assertEquals(13, board.getPosition(0).getQuantity());
        //количество шашек на позициях 5 и 6 должно быть 1, а их цвет - белый
        assertEquals(1, board.getPosition(5).getQuantity());
        assertEquals(1, board.getPosition(6).getQuantity());
        assertEquals(WHITE, board.getPosition(5).getChipColor());
        assertEquals(WHITE, board.getPosition(6).getChipColor());
        //то же самое, но не с головы
        gameCore.setDice(new ArrayList<>(List.of(3)));
        gameCore.makeMove(5, 8);
        assertEquals(0, board.getPosition(5).getQuantity());
        assertEquals(NO_COLOR, board.getPosition(5).getChipColor());
        assertEquals(1, board.getPosition(8).getQuantity());
        assertEquals(WHITE, board.getPosition(8).getChipColor());
        //нельзя ходить на фишку другого цвета
        assertThrows(IllegalArgumentException.class, () -> {
            gameCore.makeMove(6, 12);
        });
        //нельзя ходить из пустой позиции
        assertThrows(IllegalArgumentException.class, () -> {
            gameCore.makeMove(2, 3);
        });
        //то же самое, но теперь для черных фишек
        gameCore.setDice(new ArrayList<>(List.of(5, 6)));
        gameCore.makeMove(12, 17);
        gameCore.makeMove(12, 18);
        assertTrue(gameCore.noDice());
        assertEquals(13, board.getPosition(12).getQuantity());
        assertEquals(1, board.getPosition(17).getQuantity());
        assertEquals(1, board.getPosition(18).getQuantity());
        assertEquals(BLACK, board.getPosition(17).getChipColor());
        assertEquals(BLACK, board.getPosition(18).getChipColor());
        gameCore.setDice(new ArrayList<>(List.of(3)));
        gameCore.makeMove(17, 20);
        assertEquals(0, board.getPosition(17).getQuantity());
        assertEquals(NO_COLOR, board.getPosition(17).getChipColor());
        assertEquals(1, board.getPosition(20).getQuantity());
        assertEquals(BLACK, board.getPosition(20).getChipColor());
        //проход по "списку" с конца в начало
        gameCore.setDice(new ArrayList<>(List.of(4)));
        board.setPosition(0, new Chips());
        gameCore.makeMove(20, 0);
        assertTrue(gameCore.noDice());
        assertEquals(0, board.getPosition(20).getQuantity());
        assertEquals(1, board.getPosition(0).getQuantity());
        assertEquals(NO_COLOR, board.getPosition(20).getChipColor());
        assertEquals(BLACK, board.getPosition(0).getChipColor());
    }
}