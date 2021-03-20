package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import sample.Main;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AboutController implements Initializable {

    public AboutController() {

    }

    @FXML
    public Label label = new Label();

    @FXML
    public void returnMenu() throws IOException {
        Main.stage.setTitle("Шахматы. Главное меню");
        Main.root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("sample/fxml/mainMenu.fxml")));
        Main.scene = new Scene(Main.root, Main.scene.getWidth(), Main.scene.getHeight());
        Main.scene.getStylesheets().addAll("sample/css/styles.css");
        MainMenuController.stage.setScene(Main.scene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        label.setText("Приложение Шахматы (Реализация игры против компьютера)\n" +
                "В программе используется JavaFX\n" +
                "Программа разработана студентом 4 курса КРЭ СГУ\n" +
                "Черняк Данилой");
        label.setFont(new Font(15.0));
    }
}
