package sample.gameLogic;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import sample.controllers.GameController;
import sample.filework.FileReaderWriter;

public class MoveMaker {
    private boolean isCastling = false;

    public MoveMaker() {

    }

    public void doMove() {
        System.out.println("Doing move");
        GameController.unmarkFigure(GameController.markedStackPane);
        moveFigures();
        setSave();
        addToHistory();
        GameController.markedStackPane = null;
        isCastling = false;
        GameController.castlingType = 0;
    }

    public void moveFigures() {
        System.out.println("Moving figures");
        if(!GameController.stackPane.getChildren().isEmpty()) {
            GameController.stackPane.getChildren().remove(0);
        }
        GameController.stackPane.getChildren().add(GameController.markedStackPane.getChildren().get(0));

        GameController.figureName = PlayerLogic.getFigureNameFromStackPane(GameController.stackPane);

        GameController.stackPane.getChildren().get(0).setId("#");

        GameController.whiteTeamsTurn = !GameController.whiteTeamsTurn;

        StackPane rookStackPane;
        int x2 = 0;
        int rookPlace = +1;
        if(GameController.castlingType == 1) {
            isCastling = true;
        } else if(GameController.castlingType == 2) {
            x2 = 7;
            rookPlace = -1;
            isCastling = true;
        }

        if(isCastling) {
            rookStackPane = (StackPane) GameController.gridPane.getChildren().get(GameController.y2 * 8 + x2);
            GameController.saveSetuper.getSave().setCharAt(GameController.y2 * 8 + x2, '0');
            GameController.saveSetuper.getSave().setCharAt((GameController.y2 * 8 + x2) + 64, '#');
            rookStackPane.getChildren().get(0).setId("#");
            char figureName = PlayerLogic.getFigureNameFromStackPane(rookStackPane);
            ImageView imageView = (ImageView) rookStackPane.getChildren().get(0);
            rookStackPane = (StackPane) GameController.gridPane.getChildren().get(GameController.y2 * 8 + (GameController.x2 + rookPlace));
            GameController.saveSetuper.getSave().setCharAt(GameController.gridPane.getChildren().indexOf(rookStackPane),
                    figureName);
            GameController.saveSetuper.getSave().setCharAt(GameController.gridPane.getChildren().indexOf(rookStackPane) + 64, '#');
            rookStackPane.getChildren().add(imageView);
        } else if(GameController.pawnStackPane != null) {
            GameController.pawnStackPane.getChildren().clear();

            GameController.saveSetuper.getSave().setCharAt(GameController.gridPane.getChildren().indexOf(GameController.pawnStackPane),
                     '0');
            GameController.saveSetuper.getSave().setCharAt(GameController.gridPane.getChildren().indexOf(GameController.pawnStackPane) + 64,
                    '#');

            GameController.pawnStackPane = null;
        }
    }

    public void setSave() {
        System.out.println("Setting save");
        int oldCellIndex = GameController.gridPane.getChildren().indexOf(GameController.markedStackPane);
        int newCellIndex = GameController.gridPane.getChildren().indexOf(GameController.stackPane);
        GameController.saveSetuper.getSave().setCharAt(oldCellIndex, '0'); System.out.println("Setting 0 at " + (oldCellIndex));
        GameController.saveSetuper.getSave().setCharAt(newCellIndex, GameController.figureName); System.out.println("Setting " + GameController.figureName + " at " + (newCellIndex));
        GameController.saveSetuper.getSave().setCharAt(oldCellIndex + 64, '#'); System.out.println("Setting # at " + (oldCellIndex + 64));
        GameController.saveSetuper.getSave().setCharAt(newCellIndex + 64, '#'); System.out.println("Setting # at " + (newCellIndex + 64));
        char team; if(GameController.whiteTeamsTurn) team = '7'; else team = '8';
        GameController.saveSetuper.getSave().setCharAt(128, team); System.out.println("Setting " + team + " at " + (128));
        FileReaderWriter.writeFile(GameController.saveSetuper.getSave().toString(), "src/saves/" + GameController.saveSetuper.getOpponent() + ".txt", false);
    }

    public void addToHistory() {
        System.out.println("Adding to history");
        FileReaderWriter.writeFile("\n" + GameController.saveSetuper.getSave(), "src/saves/" + GameController.saveSetuper.getOpponent() + "History.txt", true);
    }
}
