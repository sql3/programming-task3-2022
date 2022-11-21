package app.Controller;

import app.Controller.MyButtons.*;
import app.Logic.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static app.Logic.Board.ejection;
import static app.Logic.Board.numberOfPositions;
import static app.Logic.ChipColor.*;

public class Controller implements Listener {

    @Override
    public void update() {
        updateBoard();
        chageDice();
    }

    private final Map<ChipColor, ImagePattern> chipImage = new HashMap<>();

    public Controller() {
        chipImage.put(BLACK, new ImagePattern(new Image("/black.png")));
        chipImage.put(WHITE, new ImagePattern(new Image("/white.png")));
    }

    private final GameCore gameCore = new GameCore().setListener(this);

    @FXML
    private Rectangle blackThrowing;

    @FXML
    private Label firstLabel;

    @FXML
    private GridPane quarter1;

    @FXML
    private GridPane quarter2;

    @FXML
    private GridPane quarter3;

    @FXML
    private GridPane quarter4;

    @FXML
    private Label secondLabel;

    @FXML
    private Rectangle whiteThrowing;

    @FXML
    void diceClick() {
        gameCore.roll();
        chageDice();
        updateBoard();
    }

    public GridPane[] getGridPanes() {
        return new GridPane[] {quarter1, quarter2, quarter3, quarter4};
    }

    private Integer selectedPosition = -1;

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public void updateBoard() {
        Board board = gameCore.getBoard();

        GridPane[] gridPanes = getGridPanes();

        Color possible = new Color(0.0, 1.0, 0.0, 1.0);
        Color selectedСhip = new Color(1.0, 0.0, 0.0, 1.0);

        boolean oneMoveButton = false;

        for (GridPane quarter : gridPanes) {
            quarter.getChildren().clear();
        }

        for (int position = 0; position < numberOfPositions; position++) {
            int numberChips = board.getPosition(position).getQuantity();

            if (numberChips > 0) {
                ChipColor chipsColor = board.getPosition(position).getChipColor();

                for (int i = 0; i < numberChips; i++) {
                    gridPanes[position / 6].add(new Circle(12, chipImage.get(chipsColor)), position % 6, 14 - i);
                }

                if (selectedPosition == -1 && chipsColor == gameCore.getQueue()) {

                    if (!gameCore.getPossibleMoves(position).isEmpty()) {
                        Button proposalButton = new ProposalButton(position, gameCore, this);

                        gridPanes[position / 6].add(new Circle(10, possible), position % 6, 15 - numberChips);
                        gridPanes[position / 6].add(proposalButton, position % 6, 15 - numberChips);

                        oneMoveButton = true;
                    }
                }

                if (selectedPosition == position) {
                    gridPanes[position / 6].add(new Circle(10, selectedСhip), position % 6, 15 - numberChips);
                }
            }
        }
        if (selectedPosition > -1) {
            List<Integer> possibleMoves = gameCore.getPossibleMoves(selectedPosition);

            for (int finish : possibleMoves) {
                if (finish == ejection) {
                    showThrowingOut(board.getPosition(selectedPosition).getChipColor());
                }
                else {
                    gridPanes[finish / 6].add(
                            new Circle(10, possible), finish % 6, 14 - board.getPosition(finish).getQuantity()
                    );
                }
            }

            gridPanes[selectedPosition / 6].add(
                    new MyButtons.CancelButton(this), selectedPosition % 6, 15 - board.getPosition(selectedPosition).getQuantity()
            );
        } else {
            if (!oneMoveButton && !gameCore.noDice()) {
                gameCore.skipMove();
                skipAlert();
            }
        }

        if (board.determineTheWinner() != null) {
            endGameAlert(board.determineTheWinner());
        }


    }

    void showThrowingOut (ChipColor chipColor) {
        Rectangle rectangle = chipColor == BLACK ? blackThrowing : whiteThrowing;
        rectangle.setOpacity(1.0);

        rectangle.setOnMouseClicked(event -> {
            gameCore.makeMove(selectedPosition, ejection);
            hideThrowingOut(chipColor);
        });
    }

    void hideThrowingOut (ChipColor chipColor) {
        selectedPosition = -1;

        Rectangle rectangle = chipColor == BLACK ? blackThrowing : whiteThrowing;

        rectangle.setOpacity(0.0);
        rectangle.setOnMouseClicked(event -> {});
        updateBoard();
    }

    public void skipAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Вы пропускаете ход");

        alert.setTitle("Пропуск хода");
        alert.setHeaderText("Невозможно сделать ход");

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.show();
    }

    public void chageDice() {
        List<Integer> dice = gameCore.getDice();

        if (dice.size() >= 2) {
            firstLabel.setText(dice.get(0).toString());
            secondLabel.setText(dice.get(1).toString());
        }

        int name1 = Integer.parseInt(firstLabel.getText());
        int name2 = Integer.parseInt(secondLabel.getText());

        if (!dice.contains(name1)) {
            firstLabel.setOpacity(0.1);
        } else {
            firstLabel.setOpacity(1.0);
        }
        if (!dice.contains(name2)) {
            secondLabel.setOpacity(0.1);
        } else {
            secondLabel.setOpacity(1.0);
        }
    }

    public void endGameAlert(ChipColor winnerColor) {
        String name = winnerColor == BLACK ? "Черная сторона" : "Белая сторона";

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Закройте приложение и откройте снова, если хотите сыграть еще раз");

        alert.setTitle("Конец игры");
        alert.setHeaderText(name + " победила !!!");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.show();
    }
}
