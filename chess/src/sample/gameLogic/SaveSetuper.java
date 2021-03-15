package sample.gameLogic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import sample.Main;
import sample.controllers.GameController;
import sample.controllers.MainMenuController;
import sample.filework.FileReaderWriter;

import java.util.ArrayList;
import java.util.List;

public class SaveSetuper {
    private StringBuilder save = new StringBuilder();
    private String opponent;
    private List<ImageView> figures = new ArrayList<>();
    private List<StackPane> stackPanes = new ArrayList<>();
    private List<List<Integer>> cellFlags = new ArrayList<>();

    public static boolean blackTeamHasMoves = false;
    public static boolean whiteTeamHasMoves = false;

    public SaveSetuper() {

    }

    public boolean isStalemate() { //пат
        boolean isStalemate = false;

        if(!blackTeamHasMoves && !GameController.whiteTeamsTurn) isStalemate = true;
        else if(!whiteTeamHasMoves && GameController.whiteTeamsTurn) isStalemate = true;

        return isStalemate;
    }

    public void checkEndgame() {
        boolean blackKingIsAlive = false;
        boolean whiteKingIsAlive = false;
        for(int i = 0; i < save.length(); i++) {
            if(save.charAt(i) == '5') whiteKingIsAlive = true; else if(save.charAt(i) == 'k') blackKingIsAlive = true;
        }
        if(!blackKingIsAlive) { Main.stage.setTitle("Шахматы. Победа белой команды"); GameController.descAnchorPane.setDisable(true); }
        if(!whiteKingIsAlive) { Main.stage.setTitle("Шахматы. Победа чёрной команды"); GameController.descAnchorPane.setDisable(true); }

        if(isStalemate()) { Main.stage.setTitle("Шахматы. Ничья"); GameController.descAnchorPane.setDisable(true); }

        int movesCount = -1; //количество ходов сделанных за историю игры
        // -1, потому что первый ход в истории всегда - начальное состояние игры и не должно считаться за ход
        boolean isDraw = false; //ничья
        StringBuilder history = new StringBuilder();
        history.append(FileReaderWriter.readFile("saves/" + opponent + "History.txt"));
        for(int i = 0; i < history.length(); i++) {
            if(history.charAt(i) == '\n') movesCount++; if(movesCount == 50) isDraw = true;
        }
        if(isDraw) { Main.stage.setTitle("Шахматы. Ничья"); GameController.descAnchorPane.setDisable(true); }
    }

    public void setupSave(GridPane desc_gridPane) {
        blackTeamHasMoves = false;
        whiteTeamHasMoves = false;
        GameController.descAnchorPane.setDisable(false);
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
                    Main.stage.setTitle("Шахматы. Ход белых");
                    break;
                case '8':
                    GameController.whiteTeamsTurn = false;
                    Main.stage.setTitle("Шахматы. Ход чёрных");
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
        checkAllMoves();
        checkEndgame();
    }

    public void checkAllMoves() {
        cellFlags.clear();
        int i = 0;
        for(StackPane markedStackPane : stackPanes) {
            cellFlags.add(new ArrayList<Integer>());
            GameController.markedStackPane = markedStackPane;
            GameController.figureName = PlayerLogic.getFigureNameFromStackPane(markedStackPane);
            if((PlayerLogic.getFigureTeam(GameController.figureName).equals("white") && GameController.whiteTeamsTurn)
                    || (PlayerLogic.getFigureTeam(GameController.figureName).equals("black") && !GameController.whiteTeamsTurn)) {
                int k = 0;
                for (StackPane stackPane : stackPanes) {
                    cellFlags.get(i).add(-1);
                    if(markedStackPane != stackPane) {
                        GameController.setCords(markedStackPane, stackPane);
                        if(GameController.playerLogic.checkTeams(GameController.figureName) &&
                                GameController.moveChecker.canItMove(GameController.gridPane, stackPane, markedStackPane,
                                        GameController.figureName, GameController.x1, GameController.x2, GameController.y1, GameController.y2)) {
                            cellFlags.get(i).set(k, GameController.moveType);
                        }
                    }
                    GameController.moveType = -1;
                    k++;
                }
            }

            GameController.figureName = '_';
            GameController.markedStackPane = null;
            i++;
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
        save.append(FileReaderWriter.readFile("saves/" + fileName));
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

    public List<StackPane> getStackPanes() {
        return stackPanes;
    }
    public void setStackPanes(List<StackPane> stackPanes) {
        this.stackPanes = stackPanes;
    }

    public List<List<Integer>> getCellFlags() {
        return cellFlags;
    }
    public void setCellFlags(List<List<Integer>> cellFlags) {
        this.cellFlags = cellFlags;
    }
}
