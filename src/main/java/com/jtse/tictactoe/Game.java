package com.jtse.tictactoe;

import java.util.HashMap;
import java.util.Map;

public class Game {
    public static final Boolean PIECE_X = false;
    public static final Boolean PIECE_O = true;

    private Boolean[] board = new Boolean[9];

    private Map<Boolean, Integer> pieceCounts(Boolean[] board) {
        Map<Boolean, Integer> counts = new HashMap<>();

        counts.put(null, 0);
        counts.put(PIECE_X, 0);
        counts.put(PIECE_O, 0);

        for (Boolean piece: board) {
            int oldCount = counts.get(piece);
            counts.replace(piece, oldCount+1);
        }

        return counts;
    }

    private void validateBoard(Boolean[] board) throws InvalidBoardException {
        if (board.length != 9) {
            throw new InvalidBoardException("board must have exactly 9 elements");
        }

        Map<Boolean, Integer> numPieces = pieceCounts(board);
        if (numPieces.get(PIECE_X) - numPieces.get(PIECE_O) > 1) {
            throw new InvalidBoardException("'X' has moved out of turn");
        }
        if (numPieces.get(PIECE_O) - numPieces.get(PIECE_X) > 0) {
            throw new InvalidBoardException("'O' has moved out of turn");
        }
    }

    public Game() {}

    public Game(Boolean[] board) throws InvalidBoardException {
        validateBoard(board);
        this.board = board;
    }

    public Boolean[] getBoard() {
        return board;
    }

    public void setBoard(Boolean[] board) throws InvalidBoardException {
        validateBoard(board);
        this.board = board;
    }

    // If all the indexed board spaces are equal, return that value;
    // otherwise, return null.
    private Boolean winnerForSeq(int[] idxs) {
        Boolean winner = null;
        for(int i: idxs) {
            if (board[i] == null) return null;
            else if (winner == null) winner = board[i];
            else if (winner != board[i]) return null;
        }
        return winner;
    }
    public Boolean findWinner() {
        int[][] winningSeqs = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 4, 8},
            {2, 4, 6},
        };
        for (int[] seq: winningSeqs) {
            Boolean winner = winnerForSeq(seq);
            if (winner != null) return winner;
        }
        return null;
    }

    public Boolean findNextPlayer() {
        if (findWinner() != null) return null;

        Map<Boolean, Integer> counts = pieceCounts(board);

        if (counts.get(null) == 0) return null; // draw

        if (counts.get(PIECE_X) > counts.get(PIECE_O)) {
            return PIECE_O;
        } else {
            return PIECE_X;
        }
    }
}
