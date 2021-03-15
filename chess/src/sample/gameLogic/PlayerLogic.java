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
        String imageUrl = "_.png";
        if(stackPane != null) {
            if (!stackPane.getChildren().isEmpty()) {
                ImageView figureImage = (ImageView) stackPane.getChildren().get(0);
                imageUrl = figureImage.getImage().impl_getUrl();
            }
        }
        char figureName = '_';
        if(imageUrl.charAt(imageUrl.length() - 4) == '.') {
            figureName = imageUrl.charAt(imageUrl.length() - 5);
        }
        return figureName;
    }

    public boolean checkTeams(char figureName) {
        boolean shouldDo = false;

        if(getFigureTeam(figureName).equals("white") && GameController.whiteTeamsTurn) {
            shouldDo = true;
        } else if(getFigureTeam(figureName).equals("black") && !GameController.whiteTeamsTurn) {
            shouldDo = true;
        }

        return shouldDo;
    }

    public static String getFigureTeam(char figureName) {
        String figureTeam = "";
        if(figureName == '1' || figureName == '2' || figureName == '3' || figureName == '4' ||
                figureName == '5' || figureName == '6') {
            figureTeam = "white";
        } else if(figureName == 'p' || figureName == 'r' || figureName == 'n' || figureName == 'b' ||
                figureName == 'k' || figureName == 'q') {
            figureTeam = "black";
        }
        return figureTeam;
    }
}
