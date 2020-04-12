package com.jtse.tictactoe;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * A simple REPL for a game of Tic-Tac-Toe.
 */
public class REPL implements Runnable {
    final InputStream in;
    final PrintStream out;

    Game game;

    public REPL(InputStream in, PrintStream out) {
        this.in = in;
        this.out = out;
    }


    private class NewCommand implements Callable<String> {
        @Override
        public String call() {
            game = new Game();
            return null;
        }
    }

    private class MoveCommand implements Callable<String> {
        private final Scanner s;

        public MoveCommand(Scanner s) {
            this.s = s;
        }

        @Override
        public String call() {
            String pieceSymbol = s.next("[XO]");
            Boolean piece = pieceSymbol.equals("X") ? Game.PIECE_X : Game.PIECE_O;

            int idx = s.nextInt();

            try {
                game.move(piece, idx);
            } catch (InvalidMoveException e) {
                return e.getMessage();
            }

            return null;
        }
    }

    private Callable<String> parseCommand(String command) {
        Scanner s = new Scanner(command);
        String commandName = s.next();
        switch (commandName) {
            case "new":
                return new NewCommand();
            case "move":
                return new MoveCommand(s);
        }
        return null;
    }

    private String drawPiece(Boolean piece) {
        return piece == null ? " "
                : piece == Game.PIECE_X ? "X" : "O";
    }

    private String drawBoard() {
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

    public String eval(String commandStr) throws Exception {
        List<String> messages = new ArrayList<>();

        Callable<String> commandObj = parseCommand(commandStr);
        String commandMessage = commandObj == null
                ? "Invalid command: " + commandStr
                : commandObj.call();
        if (commandMessage != null && !commandMessage.isEmpty()) messages.add(commandMessage);

        Boolean winner = game.findWinner();
        if (winner != null) {
            messages.add( Game.pieceName(winner) + " wins!" );
        } else if (game.findNextPlayer() == null) {
            messages.add("The game is a draw.");
        }

        if (messages.isEmpty()) messages.add("OK");

        return String.join("\n", messages) + "\n" + drawBoard();
    }

    @Override
    public void run() {
        Scanner ins = new Scanner(in);

        while (true) {
            out.print("> ");
            String commandStr = ins.nextLine();
            if (in != System.in) out.println(commandStr);

            // Special-case the exit command.
            if (commandStr.equals("exit")) break;

            try {
                out.print( eval(commandStr) );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
