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
import sample.gameLogic.MoveMaker;
import sample.gameLogic.PlayerLogic;
import sample.gameLogic.SaveSetuper;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    public final PlayerLogic playerLogic = new PlayerLogic();
    public static MoveMaker moveMaker = new MoveMaker();
    public static SaveSetuper saveSetuper = new SaveSetuper();
    public static boolean whiteTeamsTurn;
    public static int castlingType = 0; //0 - не рокировка, 1 - рокировка влево, 2 - рокировка вправо
    public static char figureName = '_';
    public static StackPane stackPane;
    public static StackPane oldStackPane;
    public static StackPane markedStackPane;
    public static GridPane gridPane;

    public static int x1, y1, x2, y2; //1 - клетка с которой ходят; 2 - на которую ходят

    @FXML
    Button flip_button = new Button();

    @FXML
    GridPane desc_gridPane = new GridPane();

    @FXML
    AnchorPane desc_anchorPane = new AnchorPane();

    @FXML
    Button mainMenuButton = new Button();

    public GameController() {

    }

    @FXML
    private void cancel() {
        StringBuilder saveBackup = new StringBuilder(saveSetuper.getSave().toString());
        saveSetuper.loadSave(saveSetuper.getOpponent() + "History.txt");
        if(saveSetuper.getSave().length() != 130) {
            saveSetuper.getSave().delete(saveSetuper.getSave().length() - 130, saveSetuper.getSave().length());
            FileReaderWriter.writeFile(saveSetuper.getSave().toString(), "src/saves/" + saveSetuper.getOpponent() + "History.txt", false);

            saveSetuper.getSave().delete(0, saveSetuper.getSave().length() - 129);
            FileReaderWriter.writeFile(saveSetuper.getSave().toString(), "src/saves/" + saveSetuper.getOpponent() + ".txt", false);
            saveSetuper.setupSave(desc_gridPane);
            markedStackPane = null;
        } else {
            saveSetuper.setSave(saveBackup);
        }
    }

    @FXML
    private void restart() {
        saveSetuper.loadSave("default.txt");
        FileReaderWriter.writeFile(saveSetuper.getSave().toString(), "src/saves/" + saveSetuper.getOpponent() + ".txt", false);
        FileReaderWriter.writeFile("\n" + saveSetuper.getSave(), "src/saves/" + saveSetuper.getOpponent() + "History.txt", false);
        saveSetuper.setupSave(desc_gridPane);
    }

    @FXML
    private void flipDesc() {
        if(desc_anchorPane.getRotate() == 180) {
            desc_anchorPane.setRotate(0);
            for(ImageView imageView : saveSetuper.getFigures()) {
                imageView.setRotate(0);
            }
        } else {
            desc_anchorPane.setRotate(180);
            for(ImageView imageView : saveSetuper.getFigures()) {
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

        gridPane.getChildren().clear();
        desc_gridPane.getChildren().clear();

        System.out.println("Returned to menu");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { initializeCells(); }

    private void figureClicked(StackPane stackPane) {
        System.out.println("Figure was clicked");
        markFigure(stackPane);
        oldStackPane = stackPane;
        GameController.stackPane = stackPane;

        if(markedStackPane != null) {
            System.out.println("markedStackPane != null");
            if (markedStackPane != stackPane && playerLogic.isFigure(markedStackPane)) {
                if ((PlayerLogic.getFigureTeam(PlayerLogic.getFigureNameFromStackPane(markedStackPane)).equals("white") && whiteTeamsTurn)
                        || (PlayerLogic.getFigureTeam(PlayerLogic.getFigureNameFromStackPane(markedStackPane)).equals("black") && !whiteTeamsTurn)) {
                    System.out.println("figures are different and we have one marked figure");
                    setCords();
                    if(playerLogic.checkTeams()) {
                        moveMaker.doMove();
                    }
                }
            }
        }
    }

    public static void markFigure(StackPane stackPane) {
        System.out.println("marking figure");
        if(!stackPane.getChildren().isEmpty()) {
            if((PlayerLogic.getFigureTeam(PlayerLogic.getFigureNameFromStackPane(stackPane)).equals("white") && whiteTeamsTurn)
                    || (PlayerLogic.getFigureTeam(PlayerLogic.getFigureNameFromStackPane(stackPane)).equals("black") && !whiteTeamsTurn)) {
                ImageView focusImage = new ImageView(new Image("sample/images/focus1.png"));
                focusImage.fitHeightProperty().bind(MainMenuController.stage.heightProperty().divide(8.727272727272727));
                focusImage.fitWidthProperty().bind(MainMenuController.stage.widthProperty().divide(10));
                stackPane.getChildren().add(focusImage);
                System.out.println("figure marked");
                unmarkFigure(markedStackPane);
                markedStackPane = stackPane;
            }
        }
    }

    public static void unmarkFigure(StackPane stackPane) {
        System.out.println("unmarking figure");
        if(stackPane != null) {
            if (!stackPane.getChildren().isEmpty()) {
                if (stackPane.getChildren().size() > 1) {
                    System.out.println("figure unmarked");
                    stackPane.getChildren().remove(1);
                }
            }
        }
    }

    public void setCords() {
        System.out.println("Setting old and new x and y cords");
        if(GridPane.getRowIndex(markedStackPane) == null) y1 = 0;
        else y1 = GridPane.getRowIndex(markedStackPane);

        if(GridPane.getColumnIndex(markedStackPane) == null) x1 = 0;
        else x1 = GridPane.getColumnIndex(markedStackPane);

        if(GridPane.getRowIndex(stackPane) == null) y2 = 0;
        else y2 = GridPane.getRowIndex(stackPane);

        if(GridPane.getColumnIndex(stackPane) == null) x2 = 0;
        else x2 = GridPane.getColumnIndex(stackPane);
    }

    public void initializeCells() {
        createCells();
        saveSetuper.loadSave(saveSetuper.getOpponent() + ".txt");
        saveSetuper.setupSave(desc_gridPane);
    }

    public void createCells() {
        for(int i = 0; i < 64; i++) {
            final StackPane finalStackPane = (StackPane) desc_gridPane.getChildren().get(i);
            finalStackPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    figureClicked(finalStackPane);
                }
            });
        }
        gridPane = desc_gridPane;
    }
}
