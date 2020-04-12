package com.jtse.tictactoe;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * An application-level test of {@code com.jtse.tictactoe} using the REPL.
 */
class REPLTest {
    static REPL repl = new REPL(System.in, System.out);

    private static class CommandTests implements ArgumentsProvider {
        static class TestParams {
            final String description;
            final String command;
            final String message;
            final char[] board;

            TestParams(String description, String command, String message, char[] board) {
                this.description = description;
                this.command = command;
                this.message = message;
                this.board = board;
            }

            @Override
            public String toString() {
                return description;
            }
        }

        static final TestParams TEST_NEW_GAME = new TestParams(
                "new game",
                "new",
                "OK",
                new char[]{' ',' ',' ', ' ',' ',' ', ' ',' ',' '});
        static final TestParams TEST_MOVE_1 = new TestParams(
                "X moves to idx 0",
                "move X 0",
                "OK",
                new char[]{'X',' ',' ', ' ',' ',' ', ' ',' ',' '});
        static final TestParams TEST_MOVE_2 = new TestParams(
                "O moves to idx 1",
                "move O 1",
                "OK",
                new char[]{'X','O',' ', ' ',' ',' ', ' ',' ',' '});
        static final TestParams TEST_INVALID_MOVE = new TestParams(
                "O attempts to move again",
                "move O 2",
                "'O' has moved out of turn",
                new char[]{'X','O',' ', ' ',' ',' ', ' ',' ',' '});
        static final TestParams TEST_MOVE_3 = new TestParams(
                "X moves to idx 4",
                "move X 4",
                "OK",
                new char[]{'X','O',' ', ' ','X',' ', ' ',' ',' '});
        static final TestParams TEST_MOVE_4a = new TestParams(
                "O moves to idx 5",
                "move O 5",
                "OK",
                new char[]{'X','O',' ', ' ','X','O', ' ',' ',' '});
        static final TestParams TEST_MOVE_5a = new TestParams(
                "X moves to idx 8",
                "move X 8",
                "'X' wins!",
                new char[]{'X','O',' ', ' ','X','O', ' ',' ','X'});
        // After a new game is started...
        static final TestParams TEST_INVALID_COMMAND = new TestParams(
                "nonsense command string",
                "foo bar baz",
                "Invalid command: foo bar baz",
                new char[]{' ',' ',' ', ' ',' ',' ', ' ',' ',' '});
        static final TestParams TEST_MOVE_4b = new TestParams(
                "O moves to idx 8",
                "move O 8",
                "OK",
                new char[]{'X','O',' ', ' ','X',' ', ' ',' ','O'});
        static final TestParams TEST_MOVE_5b = new TestParams(
                "X moves to idx 2",
                "move X 2",
                "OK",
                new char[]{'X','O','X', ' ','X',' ', ' ',' ','O'});
        static final TestParams TEST_MOVE_6 = new TestParams(
                "O moves to idx 6",
                "move O 6",
                "OK",
                new char[]{'X','O','X', ' ','X',' ', 'O',' ','O'});
        static final TestParams TEST_MOVE_7 = new TestParams(
                "X moves to idx 7",
                "move X 7",
                "OK",
                new char[]{'X','O','X', ' ','X',' ', 'O','X','O'});
        static final TestParams TEST_MOVE_8 = new TestParams(
                "O moves to idx 3",
                "move O 3",
                "OK",
                new char[]{'X','O','X', 'O','X',' ', 'O','X','O'});
        static final TestParams TEST_MOVE_9 = new TestParams(
                "X moves to idx 5",
                "move X 5",
                "The game is a draw.",
                new char[]{'X','O','X', 'O','X','X', 'O','X','O'});

        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    TEST_NEW_GAME,
                    TEST_MOVE_1,
                    TEST_MOVE_2,
                    TEST_INVALID_MOVE,
                    TEST_MOVE_3,
                    TEST_MOVE_4a,
                    TEST_MOVE_5a,
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
                    TEST_MOVE_9
            ).map(Arguments::of);
        }
    }

    static String drawBoard(char[] board) {
        return
              "   "+board[0]+" | "+board[1]+" | "+board[2]+" \n"
                      + "  ---+---+---\n"
            + "   "+board[3]+" | "+board[4]+" | "+board[5]+" \n"
                      + "  ---+---+---\n"
            + "   "+board[6]+" | "+board[7]+" | "+board[8]+" \n";
    }

    @ParameterizedTest
    @ArgumentsSource(value = CommandTests.class)
    void testCommands(CommandTests.TestParams params) throws Exception {
        String gotResponse = repl.eval(params.command);
        String expectedResponse = params.message + "\n" + drawBoard(params.board);
        assertEquals(expectedResponse, gotResponse, params.description + ": response");
    }
}