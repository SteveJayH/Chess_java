package util;

import pieces.Piece;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MoveLogger implements Serializable {

    /**
     * Wrapper class for a pair of moves.
     */
    public static class MoveRound implements Serializable{
        private Move whiteMove;
        private Move blackMove;

        public MoveRound(Move whiteMove, Move blackMove) {
            this.whiteMove = whiteMove;
            this.blackMove = blackMove;
        }

    }

    private static List<MoveRound> moveHistory;
    private static List<Move> moveRoundBuffer;

    private static MoveLogger moveLoggerInstance = new MoveLogger();

    public static MoveLogger getInstance() {
        return moveLoggerInstance;
    }

    private MoveLogger() {
        initialize();
    }

    /**
     * Add a executeMove to the history
     * @param move
     */
    public static void addMove(Move move) {
        moveRoundBuffer.add(move);
        if (moveRoundBuffer.size() == 2) {
            moveHistory.add(new MoveRound(moveRoundBuffer.get(0), moveRoundBuffer.get(1)));
            moveRoundBuffer.clear();
        }
    }

    public static List<MoveRound> getMoveList() {
        return moveHistory;
    }

    private void initialize() {
        moveHistory = new ArrayList<MoveRound>();
        moveRoundBuffer = new ArrayList<Move>();
    }
}
