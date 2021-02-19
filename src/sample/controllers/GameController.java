package sample.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import sample.Main;
import sample.filework.FileReaderWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    public static String save;
    public static String opponent;

    public static boolean whiteTeamsTurn;

    List<ImageView> figures = new ArrayList<>();

    @FXML
    Button flip_button = new Button();

    @FXML
    GridPane desc_gridPane = new GridPane();

    @FXML
    AnchorPane desc_anchorPane = new AnchorPane();

    @FXML
    Button mainMenuButton = new Button();

    public GameController() {
        save = FileReaderWriter.readFile("src/saves/" + opponent + ".txt");
    }

    @FXML
    private void flipDesc() {
        if(desc_anchorPane.getRotate() == 180) {
            desc_anchorPane.setRotate(0);
            for(ImageView imageView : figures) {
                imageView.setRotate(0);
            }
        } else {
            desc_anchorPane.setRotate(180);
            for(ImageView imageView : figures) {
                imageView.setRotate(180);
            }
        }
    }

    @FXML
    private void returnMenu() throws IOException {
        Main.root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("sample/fxml/mainMenu.fxml")));
        Main.scene = new Scene(Main.root, Main.scene.getWidth(), Main.scene.getHeight());
        Main.scene.getStylesheets().addAll("sample/css/styles.css");
        MainMenuController.stage.setScene(Main.scene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        placeFigures();
    }

    private void figureClicked(AnchorPane anchorPane) {

    }

    private void placeFigures() {
        int figureNumber = 0;
        for(int i = 0; i < save.length(); i++) {
            switch(save.charAt(i)) {
                case '0':
                    break;
                case '7':
                    whiteTeamsTurn = true;
                    break;
                case '8':
                    whiteTeamsTurn = false;
                    break;
                default:
                    try {
                        figures.add(new ImageView(new Image("sample/images/" + save.charAt(i) + ".png")));
                        figures.get(figureNumber).fitHeightProperty().bind(MainMenuController.stage.heightProperty().divide(10));
                        figures.get(figureNumber).fitWidthProperty().bind(MainMenuController.stage.widthProperty().divide(10));
                        // row(строка) = i div 8, column(столбец) = i - 8 * (i div 8)
                        final AnchorPane anchorPane = (AnchorPane) desc_gridPane.getChildren().get((i / 8) * 8 + (i - 8 * (i / 8)));
                        anchorPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                figureClicked(anchorPane);
                            }
                        });
                        StackPane stackPane = new StackPane();
                        anchorPane.getChildren().add(stackPane);
                        AnchorPane.setTopAnchor(stackPane, 0.0);
                        AnchorPane.setBottomAnchor(stackPane, 0.0);
                        AnchorPane.setRightAnchor(stackPane, 0.0);
                        AnchorPane.setLeftAnchor(stackPane, 0.0);
                        stackPane.getChildren().add(figures.get(figureNumber));
                        figureNumber++;
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
        /*
        StackPane stackPane1 = (StackPane) anchorPane.getChildren().get(0);
        imageView = (ImageView) stackPane1.getChildren().get(0);
        imageView.getImage().impl_getUrl();
        */
}
