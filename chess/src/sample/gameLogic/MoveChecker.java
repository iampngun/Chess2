package sample.gameLogic;

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class MoveChecker { //Добавить рокировку и взятие на проходе и превращение пешки
    public MoveChecker() {

    }

    private boolean wayIsFree(GridPane gridPane, StackPane stackPane, StackPane oldStackPane, int x1, int x2, int y1, int y2) {
        boolean isFree = true;
        int maxI;

        if(Math.abs(x1 - x2) == Math.abs(y1 - y2)) { //движение наискосок
            maxI = Math.abs(x1 - x2) - 1;
            System.out.println("Движение наискосок");
        }
        else {
            maxI = (Math.abs(x1 - x2) - 1) + (Math.abs(y1 - y2) - 1); //движение по прямой
            System.out.println("Движение по прямой");
        }

        int xStep;
        int yStep;
        if(Math.abs(x2 - x1) == 0) xStep = 0; else xStep = (x2 - x1) / Math.abs(x2 - x1);
        if(Math.abs(y2 - y1) == 0) yStep = 0; else yStep = (y2 - y1) / Math.abs(y2 - y1);

//        получить номер элемента на доске по его координатам: y * 8 + x

        AnchorPane checkingAnchorPane;
        StackPane checkingStackPane;
        int x3 = x1;
        int y3 = y1;

        System.out.println("Число шагов: " + maxI + "\nШаги: " + xStep + "," + yStep); 
        for(int i = 0; i < maxI; i++) {
            x3 += xStep;
            y3 += yStep;

            System.out.println("Checking cell at " + x3 + "," + y3);

            checkingAnchorPane = (AnchorPane) gridPane.getChildren().get(y3 * 8 + x3);
            checkingStackPane = (StackPane) checkingAnchorPane.getChildren().get(0);
            if(!checkingStackPane.getChildren().isEmpty()) {
                System.out.println("Way is not free");
                isFree = false;
            }
        }

        return isFree;
    }

    public boolean canItMove(GridPane gridPane, StackPane stackPane, StackPane oldStackPane, char figureName, int x1, int x2, int y1, int y2) {
        System.out.println("Checking, first cell is " + x1 + "," + y1 + "; second is " + x2 + "," + y2);
        boolean canMove = false;

        switch(figureName) {
            case '1': //белая пешка
                if(x1 == x2 && y1 - y2 == 1) {
                    if(stackPane.getChildren().isEmpty()) {
                        canMove = true;
                    }
                }
                if(x1 == x2 && y1 - y2 == 2 && oldStackPane.getChildren().get(0).getId().equals("@")) {
                    if(stackPane.getChildren().isEmpty() && wayIsFree(gridPane, stackPane, oldStackPane, x1, x2, y1, y2)) {
                        canMove = true;
                    }
                }
                if(Math.abs(x1 - x2) == 1 && y1 - y2 == 1) {
                    if(!stackPane.getChildren().isEmpty()) {
                        ImageView imageView = (ImageView) stackPane.getChildren().get(0);
                        char enemyFigureName = PlayerLogic.getFigureName(imageView.getImage().impl_getUrl());
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("black")) {
                            canMove = true;
                        }
                    }
                }
                break;
            case '2': //белая тура
                if(((x1 == x2 && y1 != y2) || (x1 != x2 && y1 == y2)) && wayIsFree(gridPane, stackPane, oldStackPane, x1, x2, y1, y2)) {
                    if(!stackPane.getChildren().isEmpty()) {
                        ImageView imageView = (ImageView) stackPane.getChildren().get(0);
                        char enemyFigureName = PlayerLogic.getFigureName(imageView.getImage().impl_getUrl());
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("black")) {
                            canMove = true;
                        }
                    } else {
                        canMove = true;
                    }
                }
                break;
            case '3': //белый конь
                if((Math.abs(x1 - x2) == 3 && Math.abs(y1 - y2) == 2) || (Math.abs(x1 - x2) == 2 && Math.abs(y1 - y2) == 3)) {
                    if(!stackPane.getChildren().isEmpty()) {
                        ImageView imageView = (ImageView) stackPane.getChildren().get(0);
                        char enemyFigureName = PlayerLogic.getFigureName(imageView.getImage().impl_getUrl());
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("black")) {
                            canMove = true;
                        }
                    } else {
                        canMove = true;
                    }
                }
                break;
            case '4': //белый слон
                if((Math.abs(x1 - x2) == Math.abs(y1 - y2)) && wayIsFree(gridPane, stackPane, oldStackPane, x1, x2, y1, y2)) {
                    if(!stackPane.getChildren().isEmpty()) {
                        ImageView imageView = (ImageView) stackPane.getChildren().get(0);
                        char enemyFigureName = PlayerLogic.getFigureName(imageView.getImage().impl_getUrl());
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("black")) {
                            canMove = true;
                        }
                    } else {
                        canMove = true;
                    }
                }
                break;
            case '5': //белый король
                if(Math.abs(x1 - x2) <= 1 && Math.abs(y1 - y2) <= 1) {
                    if(!stackPane.getChildren().isEmpty()) {
                        ImageView imageView = (ImageView) stackPane.getChildren().get(0);
                        char enemyFigureName = PlayerLogic.getFigureName(imageView.getImage().impl_getUrl());
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("black")) {
                            canMove = true;
                        }
                    } else {
                        canMove = true;
                    }
                } else if(oldStackPane.getChildren().get(0).getId().equals("@")) {

                }
                break;
            case '6': //белый ферзь
                if(((x1 == x2 && y1 != y2) || (x1 != x2 && y1 == y2)) && wayIsFree(gridPane, stackPane, oldStackPane, x1, x2, y1, y2)) {
                    if(!stackPane.getChildren().isEmpty()) {
                        ImageView imageView = (ImageView) stackPane.getChildren().get(0);
                        char enemyFigureName = PlayerLogic.getFigureName(imageView.getImage().impl_getUrl());
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("black")) {
                            canMove = true;
                        }
                    } else {
                        canMove = true;
                    }
                }
                if((Math.abs(x1 - x2) == Math.abs(y1 - y2)) && wayIsFree(gridPane, stackPane, oldStackPane, x1, x2, y1, y2)) {
                    if(!stackPane.getChildren().isEmpty()) {
                        ImageView imageView = (ImageView) stackPane.getChildren().get(0);
                        char enemyFigureName = PlayerLogic.getFigureName(imageView.getImage().impl_getUrl());
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("black")) {
                            canMove = true;
                        }
                    } else {
                        canMove = true;
                    }
                }
                break;
            case 'p': //чёрная пешка
                if(x1 == x2 && y2 - y1 == 1) {
                    if(stackPane.getChildren().isEmpty()) {
                        canMove = true;
                    }
                }
                if(x1 == x2 && y2 - y1 == 2 && oldStackPane.getChildren().get(0).getId().equals("@")) {
                    if(stackPane.getChildren().isEmpty() && wayIsFree(gridPane, stackPane, oldStackPane, x1, x2, y1, y2)) {
                        canMove = true;
                    }
                }
                if(Math.abs(x1 - x2) == 1 && y2 - y1 == 1) {
                    if(!stackPane.getChildren().isEmpty()) {
                        ImageView imageView = (ImageView) stackPane.getChildren().get(0);
                        char enemyFigureName = PlayerLogic.getFigureName(imageView.getImage().impl_getUrl());
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("white")) {
                            canMove = true;
                        }
                    }
                }
                break;
            case 'r': //чёрная тура
                if(((x1 == x2 && y1 != y2) || (x1 != x2 && y1 == y2)) && wayIsFree(gridPane, stackPane, oldStackPane, x1, x2, y1, y2)) {
                    if(!stackPane.getChildren().isEmpty()) {
                        ImageView imageView = (ImageView) stackPane.getChildren().get(0);
                        char enemyFigureName = PlayerLogic.getFigureName(imageView.getImage().impl_getUrl());
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("white")) {
                            canMove = true;
                        }
                    } else {
                        canMove = true;
                    }
                }
                break;
            case 'n': //чёрный конь
                if((Math.abs(x1 - x2) == 3 && Math.abs(y1 - y2) == 2) || (Math.abs(x1 - x2) == 2 && Math.abs(y1 - y2) == 3)) {
                    if(!stackPane.getChildren().isEmpty()) {
                        ImageView imageView = (ImageView) stackPane.getChildren().get(0);
                        char enemyFigureName = PlayerLogic.getFigureName(imageView.getImage().impl_getUrl());
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("white")) {
                            canMove = true;
                        }
                    } else {
                        canMove = true;
                    }
                }
                break;
            case 'b': //чёрный слон
                if((Math.abs(x1 - x2) == Math.abs(y1 - y2)) && wayIsFree(gridPane, stackPane, oldStackPane, x1, x2, y1, y2)) {
                    if(!stackPane.getChildren().isEmpty()) {
                        ImageView imageView = (ImageView) stackPane.getChildren().get(0);
                        char enemyFigureName = PlayerLogic.getFigureName(imageView.getImage().impl_getUrl());
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("white")) {
                            canMove = true;
                        }
                    } else {
                        canMove = true;
                    }
                }
                break;
            case 'k': //чёрный король
                if(Math.abs(x1 - x2) <= 1 && Math.abs(y1 - y2) <= 1) {
                    if(!stackPane.getChildren().isEmpty()) {
                        ImageView imageView = (ImageView) stackPane.getChildren().get(0);
                        char enemyFigureName = PlayerLogic.getFigureName(imageView.getImage().impl_getUrl());
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("white")) {
                            canMove = true;
                        }
                    } else {
                        canMove = true;
                    }
                } else if(oldStackPane.getChildren().get(0).getId().equals("@")) {

                }
                break;
            case 'q': //чёрный ферзь
                if(((x1 == x2 && y1 != y2) || (x1 != x2 && y1 == y2)) && wayIsFree(gridPane, stackPane, oldStackPane, x1, x2, y1, y2)) {
                    if(!stackPane.getChildren().isEmpty()) {
                        ImageView imageView = (ImageView) stackPane.getChildren().get(0);
                        char enemyFigureName = PlayerLogic.getFigureName(imageView.getImage().impl_getUrl());
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("white")) {
                            canMove = true;
                        }
                    } else {
                        canMove = true;
                    }
                }
                if((Math.abs(x1 - x2) == Math.abs(y1 - y2)) && wayIsFree(gridPane, stackPane, oldStackPane, x1, x2, y1, y2)) {
                    if(!stackPane.getChildren().isEmpty()) {
                        ImageView imageView = (ImageView) stackPane.getChildren().get(0);
                        char enemyFigureName = PlayerLogic.getFigureName(imageView.getImage().impl_getUrl());
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("white")) {
                            canMove = true;
                        }
                    } else {
                        canMove = true;
                    }
                }
                break;
        }

        return canMove;
    }
}
