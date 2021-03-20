package sample.gameLogic;

import sample.controllers.GameController;

import java.util.ArrayList;
import java.util.List;

public class PcLogic {
    private boolean botInWhiteTeam;

    public PcLogic() {

    }

    public double[] reverseArray(double[] array) {
        for(int i = 0; i < array.length / 2; i++) {
            double temp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = temp;
        }

        return array;
    }

    public StringBuilder changeSave(StringBuilder save, List<List<Integer>> cellFlags, int depth) {
        botInWhiteTeam = save.charAt(save.length() - 1) == '7';
        double bestMoveStrength = -9999.0;
        double moveStrength;
        StringBuilder move;
        String saveString = save.toString();

        for(int markedCellIndex = 0; markedCellIndex < cellFlags.size(); markedCellIndex++) {
            for(int cellIndex = 0; cellIndex < cellFlags.get(markedCellIndex).size(); cellIndex++) {
                if(cellFlags.get(markedCellIndex).get(cellIndex) > -1) {
                    move = simulateMove(saveString, markedCellIndex, cellIndex, cellFlags.get(markedCellIndex).get(cellIndex));
                    if (depth > 0)
                        moveStrength = findWorstMove(move.toString(), depth, cellFlags);
                    else
                        moveStrength = getMoveStrength(move.toString());

                    if (moveStrength > bestMoveStrength) {
                        bestMoveStrength = moveStrength;
                        save = new StringBuilder(move.toString());
                    }
                }
            }
        }

        return save;
    }

    public double findWorstMove(String save, int depth, List<List<Integer>> oldCellFlags) {
        double moveStrength;
        double worstMoveStrength = 9999.0;
        StringBuilder move = new StringBuilder(save);
        List<List<Integer>> cellFlags = new ArrayList<>();
        for(int i = 0; i < oldCellFlags.size(); i++) {
            cellFlags.add(new ArrayList<Integer>());
            for(int k = 0; k < oldCellFlags.get(i).size(); k++) {
                cellFlags.get(i).add(oldCellFlags.get(i).get(k));
            }
        }

        if(depth > 0) {
            GameController.saveSetuper.checkMoves(move, cellFlags);
            for (int markedCellIndex = 0; markedCellIndex < cellFlags.size(); markedCellIndex++) {
                for(int cellIndex = 0; cellIndex < cellFlags.get(markedCellIndex).size(); cellIndex++) {

                    if (cellFlags.get(markedCellIndex).get(cellIndex) > -1) {
                        move = simulateMove(move.toString(), markedCellIndex, cellIndex, cellFlags.get(markedCellIndex).get(cellIndex));
                        //--
                        moveStrength = getMoveStrength(move.toString());
                        if(moveStrength <= worstMoveStrength)
                            moveStrength = findWorstMove(move.toString(), depth - 1, cellFlags);

                        if(moveStrength < worstMoveStrength) {
                            worstMoveStrength = moveStrength;
                        }
                    }

                }
            }
        } else {
            worstMoveStrength = getMoveStrength(move.toString());
        }

        return worstMoveStrength;
    }

    public double getMoveStrength(String save) {
        double moveStrength = 0.0;
        StringBuilder move = new StringBuilder(save);

        for(int i = 0; i < 64; i++) {
            //int y = i / 8;
            //int x = i - y * 8;
            moveStrength += getFigureStrength(move.charAt(i), i);
        }

        return moveStrength;
    }

    public double getFigureStrength(char figureName, int index) {
        double figureStrength = 0.0;
        int teamFactor = 1;
        if(botInWhiteTeam) teamFactor = -1;

        if(figureName == '1') {
            figureStrength += 10 + pawnEvalWhite[index];
            figureStrength *= teamFactor * -1;
        } else if(figureName == 'p') {
            figureStrength += 10 + pawnEvalBlack[index];
            figureStrength *= teamFactor;
        } else if(figureName == 'n') {
            figureStrength += 30 + knightEval[index];
            figureStrength *= teamFactor;
        } else if(figureName == '3') {
            figureStrength += 30 + knightEval[index];
            figureStrength *= teamFactor * -1;
        } else if(figureName == '4') {
            figureStrength += 30 + bishopEvalWhite[index];
            figureStrength *= teamFactor * -1;
        } else if(figureName == 'b') {
            figureStrength += 30 + bishopEvalBlack[index];
            figureStrength *= teamFactor;
        } else if(figureName == '2') {
            figureStrength += 50 + rookEvalWhite[index];
            figureStrength *= teamFactor * -1;
        } else if(figureName == 'r') {
            figureStrength += 50 + rookEvalBlack[index];
            figureStrength *= teamFactor;
        } else if(figureName == 'q') {
            figureStrength += 90 + evalQueen[index];
            figureStrength *= teamFactor;
        } else if(figureName == '6') {
            figureStrength += 90 + evalQueen[index];
            figureStrength *= teamFactor * -1;
        } else if(figureName == '5') {
            figureStrength += 900 + kingEvalWhite[index];
            figureStrength *= teamFactor * -1;
        } else if(figureName == 'k') {
            figureStrength += 900 + kingEvalBlack[index];
            figureStrength *= teamFactor;
        }

        return figureStrength;
    }

    public StringBuilder simulateMove(String save, int markedCellIndex, int cellIndex, int moveType) {
        StringBuilder move = new StringBuilder(save);
        move.setCharAt(cellIndex, move.charAt(markedCellIndex));
        move.setCharAt(markedCellIndex, '0');
        move.setCharAt(cellIndex + 64, '#');
        move.setCharAt(markedCellIndex + 64, '#');
        if(move.charAt(move.length() - 1) == '8') {
            move.setCharAt(move.length() - 1, '7');
        } else {
            move.setCharAt(move.length() - 1, '8');
        }

        boolean isCastling = false;
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
            move = simulateCastling(move.toString(), x2, rookPlace, cellIndex);
        } else if(moveType == 5) { //если это превращение пешки
            move = simulatePawnTransformation(move.toString(), cellIndex);
        } else if(moveType == 3 || moveType == 4) { //если это взятие на проходе
            move = simulateEnPassant(move.toString(), moveType, markedCellIndex);
        }

        return move;
    }

    public StringBuilder simulateCastling(String save, int x2, int rookPlace, int cellIndex) {
        StringBuilder move = new StringBuilder(save);
        int y = cellIndex / 8;
        int x = cellIndex - y * 8;
        int rookIndex = (y * 8 + (x + rookPlace));
        if(move.charAt(cellIndex) == 'k') {
            move.setCharAt(rookIndex, 'r');
        } else {
            move.setCharAt(rookIndex, '2');
        }

        move.setCharAt(rookIndex + 64, '#');
        rookIndex = (y * 8 + x2);
        move.setCharAt(rookIndex, '0');
        move.setCharAt(rookIndex + 64, '#');

        return move;
    }

    public StringBuilder simulatePawnTransformation(String save, int cellIndex) {
        StringBuilder move = new StringBuilder(save);
        if(move.charAt(cellIndex) == 'p') {
            move.setCharAt(cellIndex, 'q');
        } else {
            move.setCharAt(cellIndex, '6');
        }

        return move;
    }

    public StringBuilder simulateEnPassant(String save, int moveType, int markedCellIndex) {
        StringBuilder move = new StringBuilder(save);
        int x2 = 1;
        if(moveType == 3) x2 = -1;
        int y = markedCellIndex / 8;
        int x = markedCellIndex - y * 8;
        int pawnIndex = y * 8 + (x + x2);
        move.setCharAt(pawnIndex, '0');
        move.setCharAt(pawnIndex + 64, '#');

        return move;
    }

    private final double[] pawnEvalWhite = new double[] {
            0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,
            5.0,  5.0,  5.0,  5.0,  5.0,  5.0,  5.0,  5.0,
            1.0,  1.0,  2.0,  3.0,  3.0,  2.0,  1.0,  1.0,
            0.5,  0.5,  1.0,  2.5,  2.5,  1.0,  0.5,  0.5,
            0.0,  0.0,  0.0,  2.0,  2.0,  0.0,  0.0,  0.0,
            0.5, -0.5, -1.0,  0.0,  0.0, -1.0, -0.5,  0.5,
            0.5,  1.0, 1.0,  -2.0, -2.0,  1.0,  1.0,  0.5,
            0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0
    };
    private final double[] pawnEvalBlack = reverseArray(pawnEvalWhite);

    private final double[] knightEval = new double[] {
            -5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0,
            -4.0, -2.0,  0.0,  0.0,  0.0,  0.0, -2.0, -4.0,
            -3.0,  0.0,  1.0,  1.5,  1.5,  1.0,  0.0, -3.0,
            -3.0,  0.5,  1.5,  2.0,  2.0,  1.5,  0.5, -3.0,
            -3.0,  0.0,  1.5,  2.0,  2.0,  1.5,  0.0, -3.0,
            -3.0,  0.5,  1.0,  1.5,  1.5,  1.0,  0.5, -3.0,
            -4.0, -2.0,  0.0,  0.5,  0.5,  0.0, -2.0, -4.0,
            -5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0
    };

    private final double[] bishopEvalWhite = new double[] {
            -2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0,
            -1.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -1.0,
            -1.0,  0.0,  0.5,  1.0,  1.0,  0.5,  0.0, -1.0,
            -1.0,  0.5,  0.5,  1.0,  1.0,  0.5,  0.5, -1.0,
            -1.0,  0.0,  1.0,  1.0,  1.0,  1.0,  0.0, -1.0,
            -1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0, -1.0,
            -1.0,  0.5,  0.0,  0.0,  0.0,  0.0,  0.5, -1.0,
            -2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0
    };
    private final double[] bishopEvalBlack = reverseArray(bishopEvalWhite);

    private final double[] rookEvalWhite = new double[] {
            0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,
            0.5,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  0.5,
            -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5,
            -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5,
            -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5,
            -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5,
            -0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5,
            0.0,   0.0, 0.0,  0.5,  0.5,  0.0,  0.0,  0.0
    };
    private final double[] rookEvalBlack = reverseArray(rookEvalWhite);

    private final double[] evalQueen = new double[] {
            -2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0,
            -1.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -1.0,
            -1.0,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -1.0,
            -0.5,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -0.5,
            0.0,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -0.5,
            -1.0,  0.5,  0.5,  0.5,  0.5,  0.5,  0.0, -1.0,
            -1.0,  0.0,  0.5,  0.0,  0.0,  0.0,  0.0, -1.0,
            -2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0
    };

    private final double[] kingEvalWhite = new double[] {
            -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0,
            -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0,
            -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0,
            -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0,
            -2.0, -3.0, -3.0, -4.0, -4.0, -3.0, -3.0, -2.0,
            -1.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -1.0,
            2.0,  2.0,  0.0,  0.0,  0.0,  0.0,  2.0,  2.0,
            2.0,  3.0,  1.0,  0.0,  0.0,  1.0,  3.0,  2.0
    };
    private final double[] kingEvalBlack = reverseArray(kingEvalWhite);
}
