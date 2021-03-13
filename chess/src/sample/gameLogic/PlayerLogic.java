package sample.gameLogic;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import sample.controllers.GameController;

public class PlayerLogic {

    public PlayerLogic() {

    }

    public boolean isFigure(StackPane stackPane) {
        return !stackPane.getChildren().isEmpty();
    }

    public static char getFigureNameFromStackPane(StackPane stackPane) { 
        String imageUrl = "{.";
        if(stackPane != null) {
            if (!stackPane.getChildren().isEmpty()) {
                ImageView figureImage = (ImageView) stackPane.getChildren().get(0);
                imageUrl = figureImage.getImage().impl_getUrl();
                System.out.println("figure src is " + imageUrl);
            }
        }
        char figureName = '_';
        System.out.println(imageUrl.length() - 4);
        if(imageUrl.charAt(imageUrl.length() - 4) == '.') {
            figureName = imageUrl.charAt(imageUrl.length() - 5);
        }
        System.out.println("Figure name is " + figureName);
        return figureName;
    }

    public boolean checkTeams() {
        boolean shouldDo = false;
        char figureName = getFigureNameFromStackPane(GameController.markedStackPane);
        System.out.println("checking teams");
        if (GameController.saveSetuper.getOpponent().equals("player")) {
            if(getFigureTeam(figureName).equals("white") &&
                    GameController.whiteTeamsTurn) {
                GameController.figureName = figureName;
                if(canMove()) shouldDo = true;
            } else if(!getFigureTeam(figureName).equals("white") &&
                    !GameController.whiteTeamsTurn) {
                GameController.figureName = figureName;
                if(canMove()) shouldDo = true;
            }
        } else if(GameController.whiteTeamsTurn) {
            GameController.figureName = figureName;
            if(canMove()) shouldDo = true;
        }

        return shouldDo;
    }

    public boolean canMove() {
        System.out.println("Can it move?");
        MoveChecker moveChecker = new MoveChecker();
        return moveChecker.canItMove(GameController.gridPane, GameController.stackPane, GameController.markedStackPane,
                GameController.figureName, GameController.x1, GameController.x2, GameController.y1, GameController.y2);
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
}
