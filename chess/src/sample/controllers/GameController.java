package sample.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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
import sample.gameLogic.PlayerLogic;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    public static StringBuilder save = new StringBuilder();
    public static String opponent;

    private final PlayerLogic playerLogic = new PlayerLogic();

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
        save.append(FileReaderWriter.readFile("src/saves/" + opponent + ".txt"));
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

        playerLogic.getGridPane().getChildren().removeAll();
        desc_gridPane.getChildren().removeAll();
        figures.clear();
        save.delete(0, save.length());

        System.out.println("Returned to menu");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        placeFigures();
    }

    private void figureClicked(StackPane stackPane) {
        System.out.println("Figure was clicked");
        markFigure();
        if(PlayerLogic.stackPane != null) {
            PlayerLogic.oldStackPane = PlayerLogic.stackPane;
        } else {
            PlayerLogic.oldStackPane = stackPane;
        }
        PlayerLogic.stackPane = stackPane;

        if(PlayerLogic.oldStackPane != PlayerLogic.stackPane && playerLogic.isFigure(PlayerLogic.oldStackPane)) {
            setCords();
            playerLogic.checkTeams();
        }
    }

    public static void markFigure() {

    }

    public static void unmarkFigure() {

    }

    private void setCords() {
        System.out.println("Setting old and new x and y cords");
        if(GridPane.getRowIndex(PlayerLogic.oldStackPane) == null) playerLogic.setY1(0);
        else playerLogic.setY1(GridPane.getRowIndex(PlayerLogic.oldStackPane));

        if(GridPane.getColumnIndex(PlayerLogic.oldStackPane) == null) playerLogic.setX1(0);
        else playerLogic.setX1(GridPane.getColumnIndex(PlayerLogic.oldStackPane));

        if(GridPane.getRowIndex(PlayerLogic.stackPane) == null) playerLogic.setY2(0);
        else playerLogic.setY2(GridPane.getRowIndex(PlayerLogic.stackPane));

        if(GridPane.getColumnIndex(PlayerLogic.stackPane) == null) playerLogic.setX2(0);
        else playerLogic.setX2(GridPane.getColumnIndex(PlayerLogic.stackPane));
    }

    private void placeFigures() {
        int figureNumber = 0;
        for(int i = 0; i < save.length(); i++) {
            StackPane stackPane = createCell(i);
            switch(save.charAt(i)) {
                case '0':
                    break;
                case '7':
                    PlayerLogic.whiteTeamsTurn = true;
                    break;
                case '8':
                    PlayerLogic.whiteTeamsTurn = false;
                    break;
                case '@':
                    break;
                case '#':
                    break;
                default:
                    try {
                        addFigureOnGrid(i, figureNumber, stackPane);
                        figureNumber++;
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
        }
        playerLogic.setGridPane(desc_gridPane);
    }

    private StackPane createCell(int i) {
        StackPane stackPane;
        try {
            stackPane = (StackPane) desc_gridPane.getChildren().get(i);
        } catch(IndexOutOfBoundsException e) {
            stackPane = (StackPane) desc_gridPane.getChildren().get(63);
        }
        final StackPane finalStackPane = stackPane;
        stackPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                figureClicked(finalStackPane);
            }
        });

        return stackPane;
    }

    private void addFigureOnGrid(int i, int figureNumber, StackPane stackPane) {
        figures.add(new ImageView(new Image("sample/images/" + save.charAt(i) + ".png")));
        figures.get(figureNumber).fitHeightProperty().bind(MainMenuController.stage.heightProperty().divide(10));
        figures.get(figureNumber).fitWidthProperty().bind(MainMenuController.stage.widthProperty().divide(10));
        stackPane.getChildren().add(figures.get(figureNumber));

        figures.get(figureNumber).setId(String.valueOf(save.charAt(i+64)));
    }
        /*
        StackPane stackPane1 = (StackPane) anchorPane.getChildren().get(0);
        imageView = (ImageView) stackPane1.getChildren().get(0);
        imageView.getImage().impl_getUrl();
        */
}
