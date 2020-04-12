package com.jtse.tictactoe;

/**
 * An invalid move was attempted.
 */
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

    /**
     * @return the piece value that attempted to move
     */
    public Boolean getPiece() {
        return piece;
    }

    /**
     * @return the position on the board to which the piece attempted to move
     */
    public int getIdx() {
        return idx;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + Game.pieceName(piece) + " at idx " + idx;
    }
}
