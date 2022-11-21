package app.Controller;

import app.Logic.ChipColor;
import app.Logic.GameCore;
import javafx.scene.control.Button;

import static app.Logic.ChipColor.*;

public class MyButtons {

    public static class ProposalButton extends Button {

        public ProposalButton(int position, GameCore gameCore, Controller controller) {
            this.setOpacity(0.0);
            this.setPrefWidth(18);
            this.setPrefHeight(18);

            this.setOnMouseClicked(event -> {
                controller.setSelectedPosition(position);

                controller.updateBoard();

                for(int move : gameCore.getPossibleMoves(position)) {
                    if (move != 24) {
                        controller.getGridPanes()[move / 6].add(
                                new FinalButton(position, move, gameCore, controller), move % 6, 14 - gameCore.getChipsInPostion(move)
                        );
                    } else controller.showThrowingOut(gameCore.getQueue());
                }
            });
        }
    }

    public static class FinalButton extends Button {

        public FinalButton(int start, int finish, GameCore gameCore, Controller controller) {
            this.setPrefWidth(18);
            this.setPrefHeight(18);
            this.setOpacity(0.0);

            this.setOnMouseClicked(event -> {
                controller.setSelectedPosition(-1);
                gameCore.makeMove(start, finish);
                controller.hideThrowingOut(gameCore.getQueue());
            });
        }
    }

    public static class CancelButton extends Button {

        public CancelButton(Controller controller) {
            this.setOpacity(0.0);
            this.setPrefWidth(18);
            this.setPrefHeight(18);

            this.setOnMouseClicked(event -> {
                controller.setSelectedPosition(-1);
                controller.updateBoard();
                controller.hideThrowingOut(WHITE);
                controller.hideThrowingOut(BLACK);
            });
        }
    }
}
