package sample.gameLogic;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import sample.controllers.GameController;
import sample.filework.FileReaderWriter;

public class MoveChecker { //Добавить превращение пешки

    public MoveChecker() {

    }

    public boolean wayIsFree(GridPane gridPane, int x1, int x2, int y1, int y2) {
        boolean isFree = true;
        int maxI;

        if(Math.abs(x1 - x2) == Math.abs(y1 - y2)) { //движение наискосок
            maxI = Math.abs(x1 - x2) - 1;
            System.out.println("Движение наискосок");
        }
        else {
            System.out.println(x1 + " " + x2);
            maxI = (Math.abs(x1 - x2) + Math.abs(y1 - y2)) - 1; //движение по прямой
            System.out.println("Движение по прямой");
        }

        int xStep;
        int yStep;
        if(Math.abs(x2 - x1) == 0) xStep = 0; else xStep = (x2 - x1) / Math.abs(x2 - x1);
        if(Math.abs(y2 - y1) == 0) yStep = 0; else yStep = (y2 - y1) / Math.abs(y2 - y1);

//        получить номер элемента на доске по его координатам: y * 8 + x

        StackPane checkingStackPane;
        int x3 = x1;
        int y3 = y1;

        System.out.println("Число шагов: " + maxI + "\nШаги: " + xStep + "," + yStep);
        for(int i = 0; i < maxI; i++) {
            x3 += xStep;
            y3 += yStep;

            System.out.println("Checking cell at " + x3 + "," + y3);

            checkingStackPane = (StackPane) gridPane.getChildren().get(y3 * 8 + x3);
            if(!checkingStackPane.getChildren().isEmpty()) {
                System.out.println("Way is not free");
                isFree = false;
            }
        }

        return isFree;
    }

    public boolean isEnPassant(GridPane gridPane, char figureName, int x1, int x2, int y1) {
        boolean isEnPassant = false;

        int x = 1;
        if(x1 > x2) x = -1;

        StackPane checkingStackPane = (StackPane) gridPane.getChildren().get(y1 * 8 + (x1 + x));

        char enemyFigureName;
        int y;
        if(figureName == '1') { enemyFigureName = 'p'; y = -2; } else { enemyFigureName = '1'; y = +2; }

        if(!checkingStackPane.getChildren().isEmpty()) {
            if(PlayerLogic.getFigureNameFromStackPane(checkingStackPane) == enemyFigureName) {
                StringBuilder save = new StringBuilder();
                save.append(FileReaderWriter.readFile("src/saves/" + GameController.saveSetuper.getOpponent() + "History.txt"));
                save.delete(save.length() - 130, save.length());
                save.delete(0, save.length() - 130);
                if(save.charAt(((y1 + y) * 8 + (x1 + x)) + 1) == enemyFigureName) {
                    System.out.println("its enPassant");
                    isEnPassant = true;
                    GameController.pawnStackPane = checkingStackPane;
                }
            }
        }

        return isEnPassant;
    }

    public boolean isCastling(GridPane gridPane, char figureName, int x1, int x2, int y1, int y2) {
        boolean isCastling = false;
        System.out.println("maybe its a castling");

        if(Math.abs(x1 - x2) == 2 && y1 == y2) {
            StackPane checkingStackPane;
            int x;
            int castlingType;
            if(x1 > x2) {
                checkingStackPane = (StackPane) gridPane.getChildren().get(y2 * 8);
                x = 0;
                castlingType = 1;
            } else {
                checkingStackPane = (StackPane) gridPane.getChildren().get(y2 * 8 + 7);
                x = 7;
                castlingType = 2;
                System.out.println("castling to right side");
            }

            if(PlayerLogic.getFigureTeam(PlayerLogic.getFigureNameFromStackPane(checkingStackPane))
                    .equals(PlayerLogic.getFigureTeam(figureName))
                    && (PlayerLogic.getFigureNameFromStackPane(checkingStackPane) == '2' || PlayerLogic.getFigureNameFromStackPane(checkingStackPane) == 'r')
                    && checkingStackPane.getChildren().get(0).getId().equals("@")) { //если на крайней клетке дружественная тура, которая не ходила
                System.out.println("на крайней клетке дружественная тура, которая не ходила");
                if(wayIsFree(gridPane, x1, x, y1, y2)) {
                    isCastling = true;
                    GameController.castlingType = castlingType;
                }
            }
        }

        System.out.println("it was a castling? " + isCastling);
        return isCastling;
    }

    public boolean canItMove(GridPane gridPane, StackPane stackPane, StackPane markedStackPane, char figureName, int x1, int x2, int y1, int y2) {
        System.out.println("Checking, first cell is " + x1 + "," + y1 + "; second is " + x2 + "," + y2);
        boolean canMove = false;

        final boolean straightMove = (x1 == x2 && y1 != y2) || (x1 != x2 && y1 == y2);
        final boolean knightMove = (Math.abs(x1 - x2) == 2 && Math.abs(y1 - y2) == 1) || (Math.abs(x1 - x2) == 1 && Math.abs(y1 - y2) == 2);
        final boolean diagonalMove = Math.abs(x1 - x2) == Math.abs(y1 - y2);
        final boolean oneCellMove = Math.abs(x1 - x2) <= 1 && Math.abs(y1 - y2) <= 1;

        switch(figureName) {
            case '1': //белая пешка
                if(x1 == x2 && y1 - y2 == 1) {
                    if(stackPane.getChildren().isEmpty()) {
                        canMove = true;
                    }
                }
                if(x1 == x2 && y1 - y2 == 2 && markedStackPane.getChildren().get(0).getId().equals("@")) {
                    if(stackPane.getChildren().isEmpty() && wayIsFree(gridPane, x1, x2, y1, y2)) {
                        canMove = true;
                    }
                }
                if(Math.abs(x1 - x2) == 1 && y1 - y2 == 1) {
                    if(!stackPane.getChildren().isEmpty()) {
                        char enemyFigureName = PlayerLogic.getFigureNameFromStackPane(stackPane);
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("black")) {
                            canMove = true;
                        }
                    } else {
                        System.out.println("maybe its enPassant");
                        canMove = isEnPassant(gridPane, figureName, x1, x2, y1);
                    }
                }
                break;
            case '2': //белая тура
                if(straightMove && wayIsFree(gridPane, x1, x2, y1, y2)) {
                    if(!stackPane.getChildren().isEmpty()) {
                        char enemyFigureName = PlayerLogic.getFigureNameFromStackPane(stackPane);
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("black")) {
                            canMove = true;
                        }
                    } else {
                        canMove = true;
                    }
                }
                break;
            case '3': //белый конь
                if(knightMove) {
                    if(!stackPane.getChildren().isEmpty()) {
                        char enemyFigureName = PlayerLogic.getFigureNameFromStackPane(stackPane);
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("black")) {
                            canMove = true;
                        }
                    } else {
                        canMove = true;
                    }
                }
                break;
            case '4': //белый слон
                if(diagonalMove && wayIsFree(gridPane, x1, x2, y1, y2)) {
                    if(!stackPane.getChildren().isEmpty()) {
                        char enemyFigureName = PlayerLogic.getFigureNameFromStackPane(stackPane);
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("black")) {
                            canMove = true;
                        }
                    } else {
                        canMove = true;
                    }
                }
                break;
            case '5': //белый король
                if(oneCellMove) {
                    if(!stackPane.getChildren().isEmpty()) {
                        char enemyFigureName = PlayerLogic.getFigureNameFromStackPane(stackPane);
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("black")) {
                            canMove = true;
                        }
                    } else {
                        canMove = true;
                    }
                } else if(markedStackPane.getChildren().get(0).getId().equals("@")) {
                        canMove = isCastling(gridPane, figureName, x1, x2, y1, y2);
                }
                break;
            case '6': //белый ферзь
                if(straightMove && wayIsFree(gridPane, x1, x2, y1, y2)) {
                    if(!stackPane.getChildren().isEmpty()) {
                        char enemyFigureName = PlayerLogic.getFigureNameFromStackPane(stackPane);
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("black")) {
                            canMove = true;
                        }
                    } else {
                        canMove = true;
                    }
                }
                if(diagonalMove && wayIsFree(gridPane, x1, x2, y1, y2)) {
                    if(!stackPane.getChildren().isEmpty()) {
                        char enemyFigureName = PlayerLogic.getFigureNameFromStackPane(stackPane);
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
                if(x1 == x2 && y2 - y1 == 2 && markedStackPane.getChildren().get(0).getId().equals("@")) {
                    if(stackPane.getChildren().isEmpty() && wayIsFree(gridPane, x1, x2, y1, y2)) {
                        canMove = true;
                    }
                }
                if(Math.abs(x1 - x2) == 1 && y2 - y1 == 1) {
                    if(!stackPane.getChildren().isEmpty()) {
                        char enemyFigureName = PlayerLogic.getFigureNameFromStackPane(stackPane);
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("white")) {
                            canMove = true;
                        }
                    } else {
                        System.out.println("maybe its enPassant");
                        canMove = isEnPassant(gridPane, figureName, x1, x2, y1);
                    }
                }
                break;
            case 'r': //чёрная тура
                if(straightMove && wayIsFree(gridPane, x1, x2, y1, y2)) {
                    if(!stackPane.getChildren().isEmpty()) {
                        char enemyFigureName = PlayerLogic.getFigureNameFromStackPane(stackPane);
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("white")) {
                            canMove = true;
                        }
                    } else {
                        canMove = true;
                    }
                }
                break;
            case 'n': //чёрный конь
                if(knightMove) {
                    if(!stackPane.getChildren().isEmpty()) {
                        char enemyFigureName = PlayerLogic.getFigureNameFromStackPane(stackPane);
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("white")) {
                            canMove = true;
                        }
                    } else {
                        canMove = true;
                    }
                }
                break;
            case 'b': //чёрный слон
                if(diagonalMove && wayIsFree(gridPane, x1, x2, y1, y2)) {
                    if(!stackPane.getChildren().isEmpty()) {
                        char enemyFigureName = PlayerLogic.getFigureNameFromStackPane(stackPane);
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("white")) {
                            canMove = true;
                        }
                    } else {
                        canMove = true;
                    }
                }
                break;
            case 'k': //чёрный король
                if(oneCellMove) {
                    if(!stackPane.getChildren().isEmpty()) {
                        char enemyFigureName = PlayerLogic.getFigureNameFromStackPane(stackPane);
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("white")) {
                            canMove = true;
                        }
                    } else {
                        canMove = true;
                    }
                } else if(markedStackPane.getChildren().get(0).getId().equals("@")) {
                    canMove = isCastling(gridPane, figureName, x1, x2, y1, y2);
                }
                break;
            case 'q': //чёрный ферзь
                if(straightMove && wayIsFree(gridPane, x1, x2, y1, y2)) {
                    if(!stackPane.getChildren().isEmpty()) {
                        char enemyFigureName = PlayerLogic.getFigureNameFromStackPane(stackPane);
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("white")) {
                            canMove = true;
                        }
                    } else {
                        canMove = true;
                    }
                }
                if(diagonalMove && wayIsFree(gridPane, x1, x2, y1, y2)) {
                    if(!stackPane.getChildren().isEmpty()) {
                        char enemyFigureName = PlayerLogic.getFigureNameFromStackPane(stackPane);
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
