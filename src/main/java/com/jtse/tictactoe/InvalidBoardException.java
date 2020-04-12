package com.jtse.tictactoe;

/**
 * The game was initialized to an invalid board configuration.
 */
public class InvalidBoardException extends Exception {
    public InvalidBoardException(String message) {
        super(message);
    }
}
