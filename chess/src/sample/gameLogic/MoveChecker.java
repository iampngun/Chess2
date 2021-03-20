package sample.gameLogic;

import sample.controllers.GameController;
import sample.filework.FileReaderWriter;

public class MoveChecker {

    public MoveChecker() {

    }

    public boolean checkWay(StringBuilder save, int x1, int x2, int y1, int y2) {
        boolean isFree = true;
        int maxI;

        if(Math.abs(x1 - x2) == Math.abs(y1 - y2)) { //движение наискосок
            maxI = Math.abs(x1 - x2) - 1;
        }
        else {
            maxI = (Math.abs(x1 - x2) + Math.abs(y1 - y2)) - 1; //движение по прямой
        }

        int xStep;
        int yStep;
        if(Math.abs(x2 - x1) == 0) xStep = 0; else xStep = (x2 - x1) / Math.abs(x2 - x1);
        if(Math.abs(y2 - y1) == 0) yStep = 0; else yStep = (y2 - y1) / Math.abs(y2 - y1);

//        получить номер элемента на доске по его координатам: y * 8 + x

        int x3 = x1;
        int y3 = y1;

        for(int i = 0; i < maxI; i++) {
            x3 += xStep;
            y3 += yStep;

            char checkingCell = save.charAt(y3 * 8 + x3);
            if(!(checkingCell == '0')) {
                isFree = false;
            }
        }

        return isFree;
    }

    public boolean checkEnPassant(StringBuilder save, char figureName, int x1, int x2, int y1, SaveSetuper.MoveType moveType) {
        boolean isEnPassant = false;
        boolean left = false;

        int x = 1;
        if(x1 > x2) {
            x = -1;
            left = true;
        }

        char checkingCell = save.charAt(y1 * 8 + (x1 + x));

        char enemyFigureName;
        int y;
        if(figureName == '1') { enemyFigureName = 'p'; y = -2; } else { enemyFigureName = '1'; y = +2; }

        if(checkingCell == enemyFigureName) {
            StringBuilder oldSave = new StringBuilder();
            oldSave.append(FileReaderWriter.readFile("saves/" + GameController.saveSetuper.getOpponent() + "History.txt"));

            oldSave.delete(oldSave.length() - 130, oldSave.length());
            oldSave.delete(0, oldSave.length() - 130);

            if(oldSave.charAt(((y1 + y) * 8 + (x1 + x)) + 1) == enemyFigureName) {
                isEnPassant = true;
                if(left) moveType.value = 3; else moveType.value = 4;
            }
        }

        return isEnPassant;
    }

    public boolean checkCastling(StringBuilder save, char figureName, int x1, int x2, int y1, int y2, SaveSetuper.MoveType moveType) {
        boolean isCastling = false;

        if(Math.abs(x1 - x2) == 2 && y1 == y2) {
            char checkingCell;
            int checkingCellIndex;
            int x;
            int castlingType;
            if(x1 > x2) {
                checkingCellIndex = y2 * 8;
                x = 0;
                castlingType = 1;
            } else {
                checkingCellIndex = y2 * 8 + 7;
                x = 7;
                castlingType = 2;
            }
            checkingCell = save.charAt(checkingCellIndex);

            if(PlayerLogic.getFigureTeam(checkingCell).equals(PlayerLogic.getFigureTeam(figureName))
                    && (checkingCell == '2' || checkingCell == 'r')
                    && save.charAt(checkingCellIndex + 64) == '@') { //если на крайней клетке дружественная тура, которая не ходила
                if(checkWay(save, x1, x, y1, y2)) {
                    isCastling = true;
                    moveType.value = castlingType;
                }
            }
        }

        return isCastling;
    }

    public boolean checkMove(StringBuilder save, int figureIndex, char figureName, char enemyFigureName,
                             int x1, int x2, int y1, int y2, SaveSetuper.MoveType moveType) {
        boolean canMove = false;

        final boolean straightMove = (x1 == x2 && y1 != y2) || (x1 != x2 && y1 == y2);
        final boolean knightMove = (Math.abs(x1 - x2) == 2 && Math.abs(y1 - y2) == 1) || (Math.abs(x1 - x2) == 1 && Math.abs(y1 - y2) == 2);
        final boolean diagonalMove = Math.abs(x1 - x2) == Math.abs(y1 - y2);
        final boolean oneCellMove = Math.abs(x1 - x2) <= 1 && Math.abs(y1 - y2) <= 1;

        switch(figureName) {
            case '1': //белая пешка
                if(x1 == x2 && y1 - y2 == 1) {
                    if(enemyFigureName == '0') {
                        canMove = true;
                        SaveSetuper.whiteTeamHasMoves = true;
                        if(y2 == 0) moveType.value = 5; else moveType.value = 0;
                    }
                }
                if(x1 == x2 && y1 - y2 == 2 && save.charAt(figureIndex + 64) == '@') {
                    if(enemyFigureName == '0' && checkWay(save, x1, x2, y1, y2)) {
                        canMove = true;
                        SaveSetuper.whiteTeamHasMoves = true;
                        moveType.value = 0;
                    }
                }
                if(Math.abs(x1 - x2) == 1 && y1 - y2 == 1) {
                    if(!(enemyFigureName == '0')) {
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("black")) {
                            canMove = true;
                            SaveSetuper.whiteTeamHasMoves = true;
                            if(y2 == 0) moveType.value = 5; else moveType.value = 0;
                        }
                    } else if(y1 == 3) {
                        if(checkEnPassant(save, figureName, x1, x2, y1, moveType)) {
                            canMove = true;
                            SaveSetuper.whiteTeamHasMoves = true;
                        }
                    }
                }
                break;
            case '2': //белая тура
                if(straightMove && checkWay(save, x1, x2, y1, y2)) {
                    if(!(enemyFigureName == '0')) {
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("black")) {
                            moveType.value = 0;
                            canMove = true;
                            SaveSetuper.whiteTeamHasMoves = true;
                        }
                    } else {
                        moveType.value = 0;
                        canMove = true;
                        SaveSetuper.whiteTeamHasMoves = true;
                    }
                }
                break;
            case '3': //белый конь
                if(knightMove) {
                    if(!(enemyFigureName == '0')) {
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("black")) {
                            moveType.value = 0;
                            canMove = true;
                            SaveSetuper.whiteTeamHasMoves = true;
                        }
                    } else {
                        moveType.value = 0;
                        canMove = true;
                        SaveSetuper.whiteTeamHasMoves = true;
                    }
                }
                break;
            case '4': //белый слон
                if(diagonalMove && checkWay(save, x1, x2, y1, y2)) {
                    if(!(enemyFigureName == '0')) {
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("black")) {
                            canMove = true;
                            SaveSetuper.whiteTeamHasMoves = true;
                            moveType.value = 0;
                        }
                    } else {
                        moveType.value = 0;
                        canMove = true;
                        SaveSetuper.whiteTeamHasMoves = true;
                    }
                }
                break;
            case '5': //белый король
                if(oneCellMove) {
                    if(!(enemyFigureName == '0')) {
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("black")) {
                            canMove = true;
                            SaveSetuper.whiteTeamHasMoves = true;
                            moveType.value = 0;
                        }
                    } else {
                        moveType.value = 0;
                        canMove = true;
                        SaveSetuper.whiteTeamHasMoves = true;
                    }
                } else if(save.charAt(figureIndex + 64) == '@') {
                    if(checkCastling(save, figureName, x1, x2, y1, y2, moveType)) {
                        canMove = true;
                        SaveSetuper.whiteTeamHasMoves = true;
                    }
                }
                break;
            case '6': //белый ферзь
                if(straightMove && checkWay(save, x1, x2, y1, y2)) {
                    if(!(enemyFigureName == '0')) {
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("black")) {
                            canMove = true;
                            SaveSetuper.whiteTeamHasMoves = true;
                            moveType.value = 0;
                        }
                    } else {
                        canMove = true;
                        SaveSetuper.whiteTeamHasMoves = true;
                        moveType.value = 0;
                    }
                }
                if(diagonalMove && checkWay(save, x1, x2, y1, y2)) {
                    if(!(enemyFigureName == '0')) {
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("black")) {
                            canMove = true;
                            SaveSetuper.whiteTeamHasMoves = true;
                            moveType.value = 0;
                        }
                    } else {
                        canMove = true;
                        SaveSetuper.whiteTeamHasMoves = true;
                        moveType.value = 0;
                    }
                }
                break;
            case 'p': //чёрная пешка
                if(x1 == x2 && y2 - y1 == 1) {
                    if(enemyFigureName == '0') {
                        canMove = true;
                        SaveSetuper.blackTeamHasMoves = true;
                        if(y2 == 7) moveType.value = 5; else moveType.value = 0;
                    }
                }
                if(x1 == x2 && y2 - y1 == 2 && save.charAt(figureIndex + 64) == '@') {
                    if(enemyFigureName == '0' && checkWay(save, x1, x2, y1, y2)) {
                        canMove = true;
                        SaveSetuper.blackTeamHasMoves = true;
                        moveType.value = 0;
                    }
                }
                if(Math.abs(x1 - x2) == 1 && y2 - y1 == 1) {
                    if(!(enemyFigureName == '0')) {
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("white")) {
                            canMove = true;
                            SaveSetuper.blackTeamHasMoves = true;
                            if(y2 == 7) moveType.value = 5; else moveType.value = 0;
                        }
                    } else if(y1 == 4) {
                        if(checkEnPassant(save, figureName, x1, x2, y1, moveType)) {
                            canMove = true;
                            SaveSetuper.blackTeamHasMoves = true;
                        }
                    }
                }
                break;
            case 'r': //чёрная тура
                if(straightMove && checkWay(save, x1, x2, y1, y2)) {
                    if(!(enemyFigureName == '0')) {
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("white")) {
                            canMove = true;
                            SaveSetuper.blackTeamHasMoves = true;
                            moveType.value = 0;
                        }
                    } else {
                        canMove = true;
                        SaveSetuper.blackTeamHasMoves = true;
                        moveType.value = 0;
                    }
                }
                break;
            case 'n': //чёрный конь
                if(knightMove) {
                    if(!(enemyFigureName == '0')) {
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("white")) {
                            canMove = true;
                            SaveSetuper.blackTeamHasMoves = true;
                            moveType.value = 0;
                        }
                    } else {
                        canMove = true;
                        SaveSetuper.blackTeamHasMoves = true;
                        moveType.value = 0;
                    }
                }
                break;
            case 'b': //чёрный слон
                if(diagonalMove && checkWay(save, x1, x2, y1, y2)) {
                    if(!(enemyFigureName == '0')) {
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("white")) {
                            canMove = true;
                            SaveSetuper.blackTeamHasMoves = true;
                            moveType.value = 0;
                        }
                    } else {
                        canMove = true;
                        SaveSetuper.blackTeamHasMoves = true;
                        moveType.value = 0;
                    }
                }
                break;
            case 'k': //чёрный король
                if(oneCellMove) {
                    if(!(enemyFigureName == '0')) {
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("white")) {
                            canMove = true;
                            SaveSetuper.blackTeamHasMoves = true;
                            moveType.value = 0;
                        }
                    } else {
                        canMove = true;
                        SaveSetuper.blackTeamHasMoves = true;
                        moveType.value = 0;
                    }
                } else if(save.charAt(figureIndex + 64) == '@') {
                    if(checkCastling(save, figureName, x1, x2, y1, y2, moveType)) {
                        canMove = true;
                        SaveSetuper.blackTeamHasMoves = true;
                    }
                }
                break;
            case 'q': //чёрный ферзь
                if(straightMove && checkWay(save, x1, x2, y1, y2)) {
                    if(!(enemyFigureName == '0')) {
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("white")) {
                            canMove = true;
                            SaveSetuper.blackTeamHasMoves = true;
                            moveType.value = 0;
                        }
                    } else {
                        canMove = true;
                        SaveSetuper.blackTeamHasMoves = true;
                        moveType.value = 0;
                    }
                }
                if(diagonalMove && checkWay(save, x1, x2, y1, y2)) {
                    if(!(enemyFigureName == '0')) {
                        if(PlayerLogic.getFigureTeam(enemyFigureName).equals("white")) {
                            canMove = true;
                            SaveSetuper.blackTeamHasMoves = true;
                            moveType.value = 0;
                        }
                    } else {
                        canMove = true;
                        SaveSetuper.blackTeamHasMoves = true;
                        moveType.value = 0;
                    }
                }
                break;
        }

        return canMove;
    }
}
