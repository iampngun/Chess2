package sample.gameLogic;

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import sample.controllers.GameController;
import sample.filework.FileReaderWriter;

public class PlayerLogic {
    public static StackPane stackPane;
    public static StackPane oldStackPane;
    public static StackPane focusedStackPane;
    private GridPane gridPane;

    public static boolean whiteTeamsTurn;

    private int x1, y1, x2, y2; //1 - клетка с которой ходят; 2 - на которую ходят

    private char figureName = '_';

    public PlayerLogic() {

    }

    public void doMove() {
        System.out.println("Doing move");
        GameController.unmarkFigure();
        moveFigures();
        setSave();
        addToHistory();
    }

    private void moveFigures() {
        System.out.println("Moving figures");
        if(!stackPane.getChildren().isEmpty()) {
            stackPane.getChildren().remove(0);
        }
        stackPane.getChildren().add(oldStackPane.getChildren().get(0));

        ImageView imageView = (ImageView) stackPane.getChildren().get(0);
        figureName = getFigureName(imageView.getImage().impl_getUrl());

        stackPane.getChildren().get(0).setId("#");

        whiteTeamsTurn = !whiteTeamsTurn;
    }

    private void setSave() {
        System.out.println("Setting save");
        int oldCellIndex = gridPane.getChildren().indexOf(oldStackPane);
        int newCellIndex = gridPane.getChildren().indexOf(stackPane);
        GameController.save.setCharAt(oldCellIndex, '0'); System.out.println("Setting 0 at " + (oldCellIndex));
        GameController.save.setCharAt(newCellIndex, figureName); System.out.println("Setting " + figureName + " at " + (newCellIndex));
        GameController.save.setCharAt(oldCellIndex + 64, '#'); System.out.println("Setting # at " + (oldCellIndex + 64));
        GameController.save.setCharAt(newCellIndex + 64, '#'); System.out.println("Setting # at " + (newCellIndex + 64));
        char team; if(whiteTeamsTurn) team = '7'; else team = '8';
        GameController.save.setCharAt(128, team); System.out.println("Setting " + team + " at " + (128));
        FileReaderWriter.writeFile(GameController.save.toString(), "src/saves/" + GameController.opponent + ".txt", false);
    }

    private void addToHistory() {
        System.out.println("Adding to history");
        FileReaderWriter.writeFile("\n" + GameController.save, "src/saves/" + GameController.opponent + "History.txt", true);
    }

    public boolean isFigure(StackPane stackPane) {
        return !stackPane.getChildren().isEmpty();
    }

    public static char getFigureName(String imageUrl) {
        char figureName = '_';
        for(int i = 0; i < imageUrl.length(); i++) {
            if(imageUrl.charAt(i) == '.') {
                figureName = imageUrl.charAt(i - 1);
                break;
            }
        }
        System.out.println("Figure name is " + figureName);
        return figureName;
    }

    public void checkTeams() {
        ImageView figureImage = (ImageView) oldStackPane.getChildren().get(0);
        char figureName = getFigureName(figureImage.getImage().impl_getUrl());
        if (GameController.opponent.equals("player")) {
            if(getFigureTeam(figureName).equals("white") &&
                    whiteTeamsTurn) {
                this.figureName = figureName;
                if(canMove()) doMove();
            } else if(!getFigureTeam(figureName).equals("white") &&
                    !whiteTeamsTurn) {
                this.figureName = figureName;
                if(canMove()) doMove();
            }
        } else if(whiteTeamsTurn) {
            this.figureName = figureName;
            if(canMove()) doMove();
        }
    }

    private boolean canMove() {
        System.out.println("Can it move?");
        MoveChecker moveChecker = new MoveChecker();
        return moveChecker.canItMove(gridPane, stackPane, oldStackPane, figureName, x1, x2, y1, y2);
    }

    public static String getFigureTeam(char figureName) {
        System.out.println("Checking what team is " + figureName);
        String figureTeam = "";
        if(figureName == '1' || figureName == '2' || figureName == '3' || figureName == '4' ||
                figureName == '5' || figureName == '6') {
            figureTeam = "white";
        } else if(figureName == 'p' || figureName == 'r' || figureName == 'n' || figureName == 'b' ||
                figureName == 'k' || figureName == 'q') {
            figureTeam = "black";
        }
        System.out.println("Figure team is " + figureTeam);
        return figureTeam;
    }

    public int getX1() { return x1; }public void setX1(int x1) { this.x1 = x1; }public int getY1() { return y1; }
    public void setY1(int y1) { this.y1 = y1; }public int getX2() { return x2; }public void setX2(int x2) { this.x2 = x2; }
    public int getY2() { return y2; }public void setY2(int y2) { this.y2 = y2; }
    public GridPane getGridPane() {
        return gridPane;
    }

    public void setGridPane(GridPane gridPane) {
        this.gridPane = gridPane;
    }
}
