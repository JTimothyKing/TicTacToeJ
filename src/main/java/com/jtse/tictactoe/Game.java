package com.jtse.tictactoe;

public class Game {
    private Boolean[] board = new Boolean[9];

    public Game() {}

    public Game(Boolean[] board) {
        this.board = board;
    }

    public Boolean[] getBoard() {
        return board;
    }

    public void setBoard(Boolean[] board) {
        this.board = board;
    }
}
