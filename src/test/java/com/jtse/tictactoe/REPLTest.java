package com.jtse.tictactoe;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * An application-level test of {@code com.jtse.tictactoe} using the REPL.
 */
class REPLTest {
    // A list of command tests to be run. They are designed to be run all
    // against a single, newly initialized REPL, so the order matters. Unfortunately, if a test
    // fails, it might also fail future tests in the series.
    private static class CommandTests implements ArgumentsProvider {
        static class TestParams {
            final String description;
            final String command;
            final String response;
            final char[] board;
            final String gameStatus;

            TestParams(String description, String command, String response, char[] board, String gameStatus) {
                this.description = description;
                this.command = command;
                this.response = response;
                this.board = board;
                this.gameStatus = gameStatus;
            }

            @Override
            public String toString() {
                return description;
            }
        }

        static final TestParams TEST_INVALID_COMMAND = new TestParams(
                "nonsense command string",
                "foo bar baz",
                "Invalid command: foo",
                new char[]{' ',' ',' ', ' ',' ',' ', ' ',' ',' '},
                "'X' goes next");
        static final TestParams TEST_NEW_GAME = new TestParams(
                "new game",
                "new",
                "OK",
                new char[]{' ',' ',' ', ' ',' ',' ', ' ',' ',' '},
                "'X' goes next");
        static final TestParams TEST_INVALID_MOVE_PIECE = new TestParams(
                "foo moves to 0",
                "move foo 0",
                "move: foo is not a valid piece (must be X or O)",
                new char[]{' ',' ',' ', ' ',' ',' ', ' ',' ',' '},
                "'X' goes next");
        static final TestParams TEST_INVALID_MOVE_LOCATION = new TestParams(
                "X moves to blah",
                "move X blah",
                "move: blah is not a valid location (must be between 0 and 8)",
                new char[]{' ',' ',' ', ' ',' ',' ', ' ',' ',' '},
                "'X' goes next");
        static final TestParams TEST_MOVE_1 = new TestParams(
                "X moves to idx 0",
                "move X 0",
                "OK",
                new char[]{'X',' ',' ', ' ',' ',' ', ' ',' ',' '},
                "'O' goes next");
        static final TestParams TEST_MOVE_2 = new TestParams(
                "O moves to idx 1",
                "MOVE O 1",
                "OK",
                new char[]{'X','O',' ', ' ',' ',' ', ' ',' ',' '},
                "'X' goes next");
        static final TestParams TEST_INVALID_MOVE_3 = new TestParams(
                "O attempts to move again",
                "move O 2",
                "'O' has moved out of turn",
                new char[]{'X','O',' ', ' ',' ',' ', ' ',' ',' '},
                "'X' goes next");
        static final TestParams TEST_MOVE_3 = new TestParams(
                "X moves to idx 4",
                "Move x 4",
                "OK",
                new char[]{'X','O',' ', ' ','X',' ', ' ',' ',' '},
                "'O' goes next");
        static final TestParams TEST_MOVE_4a = new TestParams(
                "O moves to idx 5",
                "move o 5",
                "OK",
                new char[]{'X','O',' ', ' ','X','O', ' ',' ',' '},
                "'X' goes next");
        static final TestParams TEST_MOVE_5a = new TestParams(
                "X moves to idx 8",
                "move x 8",
                "OK",
                new char[]{'X','O',' ', ' ','X','O', ' ',' ','X'},
                "'X' wins!");
        static final TestParams TEST_INVALID_MOVE_6 = new TestParams(
                "O attempts to move again",
                "move O 2",
                "'X' has already won",
                new char[]{'X','O',' ', ' ','X','O', ' ',' ','X'},
                "'X' wins!");
        static final TestParams TEST_MOVE_4b = new TestParams(
                "O moves to idx 8",
                "move o 8",
                "OK",
                new char[]{'X','O',' ', ' ','X',' ', ' ',' ','O'},
                "'X' goes next");
        static final TestParams TEST_MOVE_5b = new TestParams(
                "X moves to idx 2",
                "move x 2",
                "OK",
                new char[]{'X','O','X', ' ','X',' ', ' ',' ','O'},
                "'O' goes next");
        static final TestParams TEST_MOVE_6 = new TestParams(
                "O moves to idx 6",
                "move O 6",
                "OK",
                new char[]{'X','O','X', ' ','X',' ', 'O',' ','O'},
                "'X' goes next");
        static final TestParams TEST_MOVE_7 = new TestParams(
                "X moves to idx 7",
                "move X 7",
                "OK",
                new char[]{'X','O','X', ' ','X',' ', 'O','X','O'},
                "'O' goes next");
        static final TestParams TEST_MOVE_8 = new TestParams(
                "O moves to idx 3",
                "move O 3",
                "OK",
                new char[]{'X','O','X', 'O','X',' ', 'O','X','O'},
                "'X' goes next");
        static final TestParams TEST_MOVE_9 = new TestParams(
                "X moves to idx 5",
                "move X 5",
                "OK",
                new char[]{'X','O','X', 'O','X','X', 'O','X','O'},
                "The game is a draw");
        static final TestParams TEST_INVALID_MOVE_10 = new TestParams(
                "O attempts to move again",
                "move O 2",
                "the game is a draw",
                new char[]{'X','O','X', 'O','X','X', 'O','X','O'},
                "The game is a draw");

        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    TEST_INVALID_COMMAND,
                    TEST_INVALID_MOVE_PIECE,
                    TEST_INVALID_MOVE_LOCATION,
                    TEST_MOVE_1,
                    TEST_MOVE_2,
                    TEST_INVALID_MOVE_3,
                    TEST_MOVE_3,
                    TEST_MOVE_4a,
                    TEST_MOVE_5a,
                    TEST_INVALID_MOVE_6,
                    TEST_NEW_GAME,
                    TEST_INVALID_COMMAND,
                    TEST_MOVE_1,
                    TEST_MOVE_2,
                    TEST_MOVE_3,
                    TEST_MOVE_4b,
                    TEST_MOVE_5b,
                    TEST_MOVE_6,
                    TEST_MOVE_7,
                    TEST_MOVE_8,
                    TEST_MOVE_9,
                    TEST_INVALID_MOVE_10
            ).map(Arguments::of);
        }
    }

    private static String drawBoard(char[] board) {
        return
              "   "+board[0]+" | "+board[1]+" | "+board[2]+" \n"
                      + "  ---+---+---\n"
            + "   "+board[3]+" | "+board[4]+" | "+board[5]+" \n"
                      + "  ---+---+---\n"
            + "   "+board[6]+" | "+board[7]+" | "+board[8]+" \n";
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class TestCommands {
        REPL repl = new REPL(System.in, System.out);

        @ParameterizedTest
        @ArgumentsSource(value = CommandTests.class)
        void testCommands(CommandTests.TestParams params) throws Exception {
            assertEquals(params.response, repl.eval(params.command), params.description + ": response");
            assertEquals(drawBoard(params.board), repl.drawBoard(), params.description + ": board");
        }
    }
}