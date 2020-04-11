package com.jtse.tictactoe;

public class InvalidBoardException extends Exception {
    public InvalidBoardException() {
    }

    public InvalidBoardException(String message) {
        super(message);
    }
}
