package com.jtse.tictactoe;

public class InvalidMoveException extends Exception {
    private final Boolean piece;
    private final int idx;

    public InvalidMoveException(String message, Boolean piece, int idx) {
        super(message);
        this.piece = piece;
        this.idx = idx;
    }

    public InvalidMoveException(String message, Throwable cause, Boolean piece, int idx) {
        super(message, cause);
        this.piece = piece;
        this.idx = idx;
    }

    public Boolean getPiece() {
        return piece;
    }

    public int getIdx() {
        return idx;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + Game.pieceName(piece) + " at idx " + idx;
    }
}
