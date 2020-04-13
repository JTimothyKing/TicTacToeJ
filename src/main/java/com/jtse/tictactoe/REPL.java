package com.jtse.tictactoe;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * A simple REPL for a game of Tic-Tac-Toe.
 *
 * It supports the following commands:
 * <ul>
 *     <li><tt>new</tt> -  start a new game</li>
 *     <li><tt>move X 0</tt> - move X (or O) to location 0 (through 8)</li>
 *     <li><tt>exit</tt> - exit the REPL</li>
 * </ul>
 *
 * All commands are case-insensitive.
 */
public class REPL implements Runnable {
    final InputStream in;
    final PrintStream out;

    private Game game = new Game();

    /**
     * Construct a new REPL, specifying the in and out streams.
     *
     * In most cases, these can be set to System.in and System.out,
     * respectively.
     *
     * @param in the stream from which commands will be read
     * @param out the stream to which output will be sent
     */
    public REPL(InputStream in, PrintStream out) {
        this.in = in;
        this.out = out;
    }


    // Command classes for REPL commands.

    private class NewCommand implements Callable<String> {
        @Override
        public String call() {
            game = new Game();
            return null;
        }
    }

    private class MoveCommand implements Callable<String> {
        final Scanner s;

        MoveCommand(Scanner s) {
            this.s = s;
        }

        @Override
        public String call() {
            Boolean piece;
            try {
                String pieceSymbol = s.next("[xo]");
                piece = pieceSymbol.equals("x") ? Game.PIECE_X : Game.PIECE_O;
            } catch (NoSuchElementException e) {
                return "move: " + s.next() + " is not a valid piece (must be X or O)";
            }

            int idx;
            try {
                idx = s.nextInt();
            } catch (NoSuchElementException e) {
                return "move: " + s.next() + " is not a valid location (must be between 0 and 8)";
            }

            try {
                game.move(piece, idx);
            } catch (InvalidMoveException e) {
                return e.getMessage();
            }

            return null;
        }
    }

    private static class InvalidCommand implements Callable<String> {
        final String name;

        InvalidCommand(String name) {
            this.name = name;
        }

        @Override
        public String call() {
            return "Invalid command: " + name;
        }
    }

    // Parse a command, returning an initialized version of the
    // corresponding command class. Any parse errors will be
    // returned when the command is called.
    private Callable<String> parseCommand(String commandStr) {
        Scanner s = new Scanner(commandStr.toLowerCase());
        String commandName = s.next();
        switch (commandName) {
            case "new":
                return new NewCommand();
            case "move":
                return new MoveCommand(s);
        }
        return new InvalidCommand(commandName);
    }

    // Draws an individual piece: "X", "O", or " "
    private String drawPiece(Boolean piece) {
        return piece == null ? " "
                : piece == Game.PIECE_X ? "X" : "O";
    }

    /**
     * Draw and return the current game board as ASCII art.
     *
     * The resulting string is terminated with a final newline,
     * so one does <em>not</em> need to be added when printing.
     *
     * @return a monospace-printable representation current game board
     */
    public String drawBoard() {
        final String space = "  ";  // 2 spaces before each line

        final Boolean[] board = game.getBoard();

        List<String> rows = new ArrayList<>();
        for (int i = 0; i < board.length; i += 3) {
            String row = Arrays.stream(board, i, i+3)
                    .map(this::drawPiece)
                    .map(s -> " "+s+" ")
                    .collect(Collectors.joining("|"));
            rows.add(space + row + "\n");
        }

        return String.join(space+"---+---+---\n", rows);
    }

    /**
     * Calculate the game status as human-readable text.
     *
     * @return the current status of the game.
     */
    public String findGameStatus() {
        Boolean winner = game.findWinner();
        if (winner != null) {
            return Game.pieceName(winner) + " wins!";
        }

        Boolean nextPlayer = game.findNextPlayer();
        if (nextPlayer == null) {
            return "The game is a draw";
        } else {
            return Game.pieceName(nextPlayer) + " goes next";
        }
    }

    /**
     * Evaluate a game command.
     *
     * This involves parsing the command and executing it.
     *
     * @param commandStr the command to be evaluated
     * @return the result of the command (usually "OK")
     * @throws Exception if something goes wrong during execution of the command
     */
    public String eval(String commandStr) throws Exception {
        List<String> messages = new ArrayList<>();

        String message = parseCommand(commandStr).call();

        if (message == null || message.isEmpty()) message = "OK";

        return message;
    }

    /**
     * Run the REPL.
     *
     * The special command <tt>exit</tt> can be used to break out of the loop.
     */
    @Override
    public void run() {
        Scanner ins = new Scanner(in);

        while (true) {
            out.println(drawBoard()); // println() adds a blank line after the board
            out.println(findGameStatus());

            out.print("> ");
            String commandStr = ins.nextLine().toLowerCase();

            // Special-case the exit command.
            if (commandStr.equals("exit")) break;

            try {
                out.println( eval(commandStr) );
            } catch (Exception e) {
                e.printStackTrace();
            }
            out.println(); // a blank line before the next loop
        }

        ins.close();
    }
}
