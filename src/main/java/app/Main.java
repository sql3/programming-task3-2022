package app;

import app.Controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/primaryScene.fxml"));

        stage.setTitle("Long Backgammon");
        stage.setResizable(false);
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setResizable(false);
        stage.show();

        Controller controller = fxmlLoader.getController();
        controller.updateBoard();
    }

    public static void main(String[] args) {
        Application.launch();
    }
}