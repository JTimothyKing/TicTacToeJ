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
}
