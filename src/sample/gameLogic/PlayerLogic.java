package sample.gameLogic;

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import sample.controllers.GameController;

public class PlayerLogic {
    private AnchorPane anchorPane;
    private AnchorPane oldAnchorPane;

    public static boolean whiteTeamsTurn;

    private int x1, y1, x2, y2;

    private char figureName;

    public PlayerLogic() {

    }

    public void doMove() {
        if(canMove(figureName)) {

        }
    }

    private boolean isFigure(AnchorPane anchorPane) {
        return !anchorPane.getChildren().isEmpty();
    }

    public void checkTeams() {
        if(!getOldAnchorPane().equals(getAnchorPane())) {
            if (isFigure(getOldAnchorPane())) {

                StackPane stackPane = (StackPane) getOldAnchorPane().getChildren().get(0);
                ImageView imageView = (ImageView) stackPane.getChildren().get(0);
                String imageUrl = imageView.getImage().impl_getUrl();

                if (GameController.opponent.equals("player")) {
                    if(isWhiteTeam(imageUrl.charAt(14)) &&
                            whiteTeamsTurn) {
                        figureName = imageUrl.charAt(14);
                        doMove();
                    } else if(!isWhiteTeam(imageUrl.charAt(14)) &&
                            !whiteTeamsTurn) {
                        figureName = imageUrl.charAt(14);
                        doMove();
                    }
                } else if(whiteTeamsTurn) {
                    figureName = imageUrl.charAt(14);
                    doMove();
                }
            }
        }
    }

    private boolean canMove(char figureName) {
        boolean canMove = false;
        switch(figureName) {
            case '1':
                break;
            case '2':
                break;
            case '3':
                break;
            case '4':
                break;
            case '5':
                break;
            case '6':
                break;
            case 'p':
                break;
            case 'r':
                break;
            case 'n':
                break;
            case 'b':
                break;
            case 'k':
                break;
            case 'q':
                break;
        }

        return canMove;
    }

    private boolean isWhiteTeam(char figureName) {
        boolean isWhiteTeam = false;
        if(figureName == '1' || figureName == '2' || figureName == '3' || figureName == '4' ||
                figureName == '5' || figureName == '6') {
            isWhiteTeam = true;
        }
        return isWhiteTeam;
    }

    public AnchorPane getAnchorPane() { return anchorPane; }
    public void setAnchorPane(AnchorPane anchorPane) { this.anchorPane = anchorPane; }
    public AnchorPane getOldAnchorPane() { return oldAnchorPane; }
    public void setOldAnchorPane(AnchorPane oldAnchorPane) { this.oldAnchorPane = oldAnchorPane; }
    public int getX1() { return x1; }public void setX1(int x1) { this.x1 = x1; }public int getY1() { return y1; }
    public void setY1(int y1) { this.y1 = y1; }public int getX2() { return x2; }public void setX2(int x2) { this.x2 = x2; }
    public int getY2() { return y2; }public void setY2(int y2) { this.y2 = y2; }
}
