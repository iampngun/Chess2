package sample.gameLogic;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.Main;
import sample.controllers.GameController;
import sample.controllers.MainMenuController;
import sample.controllers.SelectionController;
import sample.filework.FileReaderWriter;

import java.io.IOException;
import java.util.Objects;

public class MoveMaker {
    private boolean isCastling = false;
    private boolean continueMove = true;

    public MoveMaker() {

    }

    public void doMove(Integer moveType) {
        GameController.unmarkFigure(GameController.markedStackPane);
        moveFigures(moveType);
        if(continueMove) {
            setSave();
            if(GameController.saveSetuper.getOpponent().equals("player")) addToHistory();
            GameController.markedStackPane = null;
            isCastling = false;
            if (GameController.whiteTeamsTurn) Main.stage.setTitle("Шахматы Ход белых");
            else Main.stage.setTitle("Шахматы Ход чёрных");
            SaveSetuper.blackTeamHasMoves = false;
            SaveSetuper.whiteTeamHasMoves = false;
            GameController.saveSetuper.checkMoves(GameController.saveSetuper.getSave());
            GameController.saveSetuper.checkEndgame();
            if(GameController.saveSetuper.getOpponent().equals("pc") &&
                    !GameController.whiteTeamsTurn && GameController.descAnchorPane.isDisabled()) pcMove();//может не работать
        }
    }

    public void pcMove() {
        PcLogic pcLogic = new PcLogic(GameController.saveSetuper.getSave());
        pcLogic.changeSave();
        GameController.saveSetuper.setSave(pcLogic.getSave());
        addToHistory();
        GameController.saveSetuper.setupSave(GameController.gridPane);
    }

    public void moveFigures(Integer moveType) {
        if(!GameController.stackPane.getChildren().isEmpty()) {
            GameController.stackPane.getChildren().remove(0);
        }
        GameController.stackPane.getChildren().add(GameController.markedStackPane.getChildren().get(0));

        GameController.figureName = PlayerLogic.getFigureNameFromStackPane(GameController.stackPane);

        GameController.stackPane.getChildren().get(0).setId("#");

        GameController.whiteTeamsTurn = !GameController.whiteTeamsTurn;

        int x2 = 0;
        int rookPlace = +1;
        if(moveType == 1) {
            isCastling = true;
        } else if(moveType == 2) {
            x2 = 7;
            rookPlace = -1;
            isCastling = true;
        }

        if(isCastling) {
            doCastling(x2, rookPlace);
        } else if(moveType == 5) { //если это превращение пешки
            preparePawnTransformation();
        } else if(moveType == 3 || moveType == 4) { //если это взятие на проходе
            doEnPassant(moveType);
        }
    }

    public void doCastling(int x2, int rookPlace) {
        StackPane rookStackPane;
        rookStackPane = (StackPane) GameController.gridPane.getChildren().get(GameController.Cords.y2 * 8 + x2);
        GameController.saveSetuper.getSave().setCharAt(GameController.Cords.y2 * 8 + x2, '0');
        GameController.saveSetuper.getSave().setCharAt((GameController.Cords.y2 * 8 + x2) + 64, '#');
        rookStackPane.getChildren().get(0).setId("#");
        char figureName = PlayerLogic.getFigureNameFromStackPane(rookStackPane);
        ImageView imageView = (ImageView) rookStackPane.getChildren().get(0);
        rookStackPane = (StackPane) GameController.gridPane.getChildren().get(GameController.Cords.y2 * 8 + (GameController.Cords.x2 + rookPlace));
        GameController.saveSetuper.getSave().setCharAt(GameController.gridPane.getChildren().indexOf(rookStackPane),
                figureName);
        GameController.saveSetuper.getSave().setCharAt(GameController.gridPane.getChildren().indexOf(rookStackPane) + 64, '#');
        rookStackPane.getChildren().add(imageView);
    }

    public void doEnPassant(int moveType) {
        int x = 1;
        if(moveType == 3) x = -1;
        GameController.pawnStackPane = (StackPane) GameController.gridPane.getChildren().get(GameController.Cords.y1 * 8 + (GameController.Cords.x1 + x));
        GameController.pawnStackPane.getChildren().clear();

        GameController.saveSetuper.getSave().setCharAt(GameController.gridPane.getChildren().indexOf(GameController.pawnStackPane),
                '0');
        GameController.saveSetuper.getSave().setCharAt(GameController.gridPane.getChildren().indexOf(GameController.pawnStackPane) + 64,
                '#');
    }

    public void preparePawnTransformation() {
        GridPane selectionGridPane = new GridPane();
        selectionGridPane.setId("whiteCell");
        selectionGridPane.setAlignment(Pos.CENTER);

        final char queen, rook, knight, bishop;
        if(PlayerLogic.getFigureTeam(GameController.figureName).equals("white")) {
            queen = '6';
            rook = '2';
            knight = '3';
            bishop = '4';
        } else {
            queen = 'q';
            rook = 'r';
            knight = 'n';
            bishop = 'b';
        }
        ImageView queenImage = new ImageView(new Image("sample/images/" + queen + ".png"));
        ImageView rookImage = new ImageView(new Image("sample/images/" + rook + ".png"));
        ImageView knightImage = new ImageView(new Image("sample/images/" + knight + ".png"));
        ImageView bishopImage = new ImageView(new Image("sample/images/" + bishop + ".png"));

        queenImage.fitHeightProperty().bind(MainMenuController.stage.heightProperty().divide(8.8));
        queenImage.fitWidthProperty().bind(MainMenuController.stage.widthProperty().divide(10));
        rookImage.fitHeightProperty().bind(MainMenuController.stage.heightProperty().divide(8.8));
        rookImage.fitWidthProperty().bind(MainMenuController.stage.widthProperty().divide(10));
        knightImage.fitHeightProperty().bind(MainMenuController.stage.heightProperty().divide(8.8));
        knightImage.fitWidthProperty().bind(MainMenuController.stage.widthProperty().divide(10));
        bishopImage.fitHeightProperty().bind(MainMenuController.stage.heightProperty().divide(8.8));
        bishopImage.fitWidthProperty().bind(MainMenuController.stage.widthProperty().divide(10));

        StackPane queenPane = new StackPane(); queenPane.setId("figureCelection"); queenPane.getChildren().add(queenImage);
        StackPane rookPane = new StackPane(); rookPane.setId("figureCelection"); rookPane.getChildren().add(rookImage);
        StackPane knightPane = new StackPane(); knightPane.setId("figureCelection"); knightPane.getChildren().add(knightImage);
        StackPane bishopPane = new StackPane(); bishopPane.setId("figureCelection"); bishopPane.getChildren().add(bishopImage);

        selectionGridPane.addColumn(0, queenPane);
        selectionGridPane.addColumn(1, rookPane);
        selectionGridPane.addColumn(2, knightPane);
        selectionGridPane.addColumn(3, bishopPane);

        final Stage dialog = new Stage();
        queenPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                transformPawn(queen, dialog);
            }
        });
        rookPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                transformPawn(rook, dialog);
            }
        });
        knightPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                transformPawn(knight, dialog);
            }
        });
        bishopPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                transformPawn(bishop, dialog);
            }
        });

        createModalWindow(selectionGridPane, dialog);
    }

    public void transformPawn(char figureName, Stage dialog) {
        GameController.figureName = figureName;
        ImageView imageView = (ImageView) GameController.stackPane.getChildren().get(0);
        imageView.setImage(new Image("sample/images/" + figureName + ".png"));
        dialog.close();
    }

    public void createModalWindow(GridPane selectionGridPane, Stage dialog) {
        try {
            dialog.setTitle("Выберите фигуру");
            dialog.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) { MainMenuController.quit(); continueMove = false;
                }
            });
            dialog.initOwner(Main.stage);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setHeight(Main.stage.getHeight() / 100 * 19);
            dialog.setWidth(Main.stage.getWidth() / 100 * 42);
            dialog.setResizable(false);

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("sample/fxml/figureSelection.fxml")));
            SelectionController.selectionAnchorPane.getChildren().add(selectionGridPane);
            AnchorPane.setBottomAnchor(selectionGridPane, 0.0);
            AnchorPane.setRightAnchor(selectionGridPane, 0.0);
            AnchorPane.setLeftAnchor(selectionGridPane, 0.0);
            AnchorPane.setTopAnchor(selectionGridPane, 0.0);
            Scene scene = new Scene(root, dialog.getWidth(), dialog.getHeight());
            scene.getStylesheets().addAll("sample/css/styles.css");
            dialog.setScene(scene);

            dialog.showAndWait();
        } catch (IOException e) {e.printStackTrace();}
    }

    public void setSave() {
        int oldCellIndex = GameController.gridPane.getChildren().indexOf(GameController.markedStackPane);
        int newCellIndex = GameController.gridPane.getChildren().indexOf(GameController.stackPane);
        GameController.saveSetuper.getSave().setCharAt(oldCellIndex, '0');
        GameController.saveSetuper.getSave().setCharAt(newCellIndex, GameController.figureName);
        GameController.saveSetuper.getSave().setCharAt(oldCellIndex + 64, '#');
        GameController.saveSetuper.getSave().setCharAt(newCellIndex + 64, '#');
        char team; if(GameController.whiteTeamsTurn) team = '7'; else team = '8';
        GameController.saveSetuper.getSave().setCharAt(128, team);
        FileReaderWriter.writeFile(GameController.saveSetuper.getSave().toString(), "saves/" + GameController.saveSetuper.getOpponent() + ".txt", false);
    }

    public void addToHistory() {
        FileReaderWriter.writeFile("\n" + GameController.saveSetuper.getSave(), "saves/" + GameController.saveSetuper.getOpponent() + "History.txt", true);
    }
}
