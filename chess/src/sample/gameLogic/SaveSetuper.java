package sample.gameLogic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import sample.controllers.GameController;
import sample.controllers.MainMenuController;
import sample.filework.FileReaderWriter;

import java.util.ArrayList;
import java.util.List;

public class SaveSetuper {
    private StringBuilder save = new StringBuilder();
    private String opponent;
    private List<ImageView> figures = new ArrayList<>();

    public SaveSetuper() {

    }

    public void setupSave(GridPane desc_gridPane) {
        figures.clear();
        GameController.unmarkFigure(GameController.markedStackPane);
        for(int i = 0; i < desc_gridPane.getChildren().size(); i++) {
            StackPane stackPane = (StackPane) desc_gridPane.getChildren().get(i);
            stackPane.getChildren().clear();
        }
        int figureNumber = 0;
        for(int i = 0; i < save.length(); i++) {
            switch(save.charAt(i)) {
                case '0':
                    break;
                case '7':
                    GameController.whiteTeamsTurn = true;
                    break;
                case '8':
                    GameController.whiteTeamsTurn = false;
                    break;
                case '@':
                    break;
                case '#':
                    break;
                default:
                    try {
                        placeFigure(i, figureNumber, (StackPane) desc_gridPane.getChildren().get(i));
                        figureNumber++;
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    public void placeFigure(int i, int figureNumber, StackPane stackPane) {
        figures.add(new ImageView(new Image("sample/images/" + save.charAt(i) + ".png")));
        figures.get(figureNumber).fitHeightProperty().bind(MainMenuController.stage.heightProperty().divide(8.8));
        figures.get(figureNumber).fitWidthProperty().bind(MainMenuController.stage.widthProperty().divide(10));
        stackPane.getChildren().add(figures.get(figureNumber));

        figures.get(figureNumber).setId(String.valueOf(save.charAt(i+64)));
    }

    public void loadSave(String fileName) {
        save.delete(0, save.length());
        save.append(FileReaderWriter.readFile("src/saves/" + fileName));
    }

    public StringBuilder getSave() {
        return save;
    }
    public void setSave(StringBuilder save) {
        this.save = save;
    }

    public List<ImageView> getFigures() {
        return figures;
    }
    public void setFigures(List<ImageView> figures) {
        this.figures = figures;
    }

    public String getOpponent() {
        return opponent;
    }
    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }
}
