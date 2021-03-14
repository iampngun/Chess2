package sample.gameLogic;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import sample.Main;
import sample.controllers.GameController;
import sample.filework.FileReaderWriter;

public class MoveMaker {
    private boolean isCastling = false;

    public MoveMaker() {

    }

    public void doMove(Integer moveType) {
        GameController.unmarkFigure(GameController.markedStackPane);
        moveFigures(moveType);
        setSave();
        addToHistory();
        GameController.markedStackPane = null;
        isCastling = false;
        if(GameController.whiteTeamsTurn) Main.stage.setTitle("Шахматы Ход белых"); else Main.stage.setTitle("Шахматы Ход чёрных");
        GameController.saveSetuper.checkAllMoves();
    }

    public void moveFigures(Integer moveType) {
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
        if(moveType == 1) {
            isCastling = true;
        } else if(moveType == 2) {
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
        } else if(GameController.pawnTransformation) {


            GameController.pawnTransformation = false;
        } else if(moveType == 3) { //если это взятие на проходе
            GameController.pawnStackPane.getChildren().clear();

            GameController.saveSetuper.getSave().setCharAt(GameController.gridPane.getChildren().indexOf(GameController.pawnStackPane),
                     '0');
            GameController.saveSetuper.getSave().setCharAt(GameController.gridPane.getChildren().indexOf(GameController.pawnStackPane) + 64,
                    '#');

            GameController.pawnStackPane = null;
        }
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
