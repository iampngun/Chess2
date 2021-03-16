package sample.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sample.Main;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {
    public static Stage stage;

    @FXML
    Button playVsPcButton = new Button();

    @FXML
    Button playVsPlayerButton = new Button();

    @FXML
    Button quitButton = new Button();

    public MainMenuController() {

    }

    @FXML
    public void play(ActionEvent event) throws IOException {
        if(event.getSource().toString().contains("Player")) {
            GameController.saveSetuper.setOpponent("player");
        } else {
            GameController.saveSetuper.setOpponent("pc");
        }
        Main.root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("sample/fxml/game.fxml")));
        Main.scene = new Scene(Main.root, Main.scene.getWidth(), Main.scene.getHeight());
        Main.scene.getStylesheets().addAll("sample/css/styles.css");
        stage.setScene(Main.scene);
    }

    @FXML
    public static void quit() {
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
