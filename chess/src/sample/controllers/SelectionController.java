package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class SelectionController implements Initializable {

    public static AnchorPane selectionAnchorPane;

    @FXML
    AnchorPane anchorPane = new AnchorPane();

    public SelectionController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectionAnchorPane = anchorPane;
    }
}
