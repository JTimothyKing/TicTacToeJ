package com.jtse.tictactoe;

import java.util.HashMap;
import java.util.Map;

/**
 * A game of Tic-Tac-Toe.
 *
 * This class tracks the board and knows the gameplay rules.
 *
 * The board is stored as an array of {@code Boolean} values.
 * A {@code null} element represents an empty space, and non-null
 * elements represent X's and O's, according to defined constants.
 *
 * The board array is exactly 9 elements long, and locations are
 * specified by index:
 * <ul>
 *     <li>0-2 - the top row</li>
 *     <li>3-5 - the middle row</li>
 *     <li>6-8 - the bottom row</li>
 * </ul>
 *
 * Note that you can find out whether the game is ongoing, won, or a draw
 * by examining the return values of {@code findNextPlayer()} and {@code findWinner()}:
 * <ul>
 *     <li>If {@code findNextPlayer()} returns non-null, the game is ongoing.</li>
 *     <li>If {@code findWinner()} returns non-null, the game has been won.</li>
 *     <li>If both return {@code null}, the game is a draw.</li>
 * </ul>
 *
 * @author Tim King
 */
public class Game {
    /**
     * The value of an element in which player X has played.
     */
    public static final Boolean PIECE_X = false;
    /**
     * The value of an element in which player O has played.
     */
    public static final Boolean PIECE_O = true;

    private Boolean[] board = new Boolean[9];

    // Counts the number of each piece and empty space on the board.
    // Returns a map with board element values as key and the corresponding count as value.
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

    // Validates that the board is valid, and throws an appropriate exception if not.
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

    /**
     * Start a new game with an empty board.
     */
    public Game() {}

    /**
     * Restore a game from a previously saved board.
     *
     * Throws an {@code InvalidBoardException} if the specified board
     * configuration is invalid. That is, the board array must be exactly
     * 9 elements long, and if any players have already moved, no player
     * may have moved twice in a row.
     *
     * @param board the board array to restore
     * @throws InvalidBoardException if {@code board} is invalid
     */
    public Game(Boolean[] board) throws InvalidBoardException {
        validateBoard(board);
        this.board = board;
    }

    public final Boolean[] getBoard() {
        return board;
    }

    // Helper method for findWinner().
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

    /**
     * Find the winner of the game, if there is one.
     *
     * This may return {@code null}, if the game is in progress or is a draw.
     *
     * @return {@code Game.PIECE_X} or {@code Game.PIECE_O} if a player
     * has won the game; or {@code null} if no winner has been declared.
     */
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

    /**
     * Figure out which player's move is next.
     *
     * This may return {@code null}, if the game has already been won
     * or is a draw.
     *
     * @return {@code Game.PIECE_X} or {@code Game.PIECE_O} according to which
     * player should move next; or {@code null} if the game is over.
     */
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

    /**
     * Move a player's piece onto a space on the board.
     *
     * The move must be a valid move. That is:
     * <ul>
     *     <li>The target space must not already be occupied.</li>
     *     <li>The game must still be in progress.</li>
     *     <li>It must be the corresponding player's turn.</li>
     * </ul>
     *
     * Even though {@code piece} is a {@code Boolean}, for consistency with the
     * rest of this class's API, it may not be {@code null}. Attempting to move
     * a null piece onto the board will produce an {@code InvalidMoveException}.
     *
     * @param piece The player to move: {@code Game.PIECE_X} or {@code Game.PIECE_O}.
     * @param idx The space onto which the player would like to move.
     * @throws InvalidMoveException if the specified piece cannot be moved onto the
     * specified space.
     */
    public void move(Boolean piece, int idx) throws InvalidMoveException {
        try {
            if (piece == null) throw new InvalidMoveException("piece must be specified", piece, idx);

            final Boolean winner = findWinner();
            if (winner != null) {
                throw new InvalidMoveException(pieceName(winner) + " has already won", piece, idx);
            }

            final Boolean nextPlayer = findNextPlayer();
            if (nextPlayer == null) {
                throw new InvalidMoveException("the game is a draw", piece, idx);
            }
            if (nextPlayer != piece) {
                throw new InvalidMoveException(pieceName(piece) + " has moved out of turn", piece, idx);
            }

            if (board[idx] != null) throw new InvalidMoveException("the space is already occupied", piece, idx);

            board[idx] = piece;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidMoveException("invalid location", e, piece, idx);
        }
    }

    /**
     * Finds the human-readable name of the specified piece value.
     * @param piece {@code Game.PIECE_X}, {@code Game.PIECE_O}, or {@code null}
     * @return the human-readable name of the piece
     */
    public static String pieceName(Boolean piece) {
        return piece == null ? "empty" : piece == Game.PIECE_X ? "'X'" : "'O'";
    }
}
