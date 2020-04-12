package com.jtse.tictactoe;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 *
 */
class GameTest {
    static class Boards {
        // These symbols are just to make the following board definitions more readable in the editor.
        private static final Boolean ___ = null;
        private static final Boolean _X_ = Game.PIECE_X;
        private static final Boolean _O_ = Game.PIECE_O;

        /**
         * An empty board (the initial board state).
         */
        public static final Boolean[] BOARD_EMPTY = {
                ___, ___, ___,
                ___, ___, ___,
                ___, ___, ___,
        };

        /**
         * A board with _X_ in location 0, but otherwise empty.
         */
        public static final Boolean[] BOARD_X = {
                _X_, ___, ___,
                ___, ___, ___,
                ___, ___, ___,
        };

        /**
         * A board with _X_ in location 0, _O_ in location 1, and all other locations
         * empty.
         */
        public static final Boolean[] BOARD_XO = {
                _X_, _O_, ___,
                ___, ___, ___,
                ___, ___, ___,
        };

        /**
         * A board with no empty spaces but with no winner.
         */
        public static final Boolean[] BOARD_DRAW = {
                _X_, _O_, _X_,
                _X_, _O_, _O_,
                _O_, _X_, _X_,
        };

        /**
         * A board in which _X_ has won by filling column 0.
         *
         * This board has empty spaces at locations 5 and 7.
         */
        public static final Boolean[] BOARD_X_WINS_COL_0 = {
                _X_, _O_, _O_,
                _X_, _X_, ___,
                _X_, ___, _O_,
        };

        /**
         * A board in which _O_ has won by filling column 1.
         *
         * This board has empty spaces at locations 5, 6, and 8.
         */
        public static final Boolean[] BOARD_O_WINS_COL_1 = {
                _X_, _O_, _X_,
                _X_, _O_, ___,
                ___, _O_, ___,
        };

        /**
         * A board in which _X_ has won by filling column 2.
         *
         * This board has empty spaces at locations 0, 3, 6, and 7.
         */
        public static final Boolean[] BOARD_X_WINS_COL_2 = {
                ___, _O_, _X_,
                ___, _O_, _X_,
                ___, ___, _X_,
        };

        /**
         * A board in which _X_ has won by filling row 0.
         *
         * This board has empty spaces at locations 3, 5, 7, and 8.
         */
        public static final Boolean[] BOARD_X_WINS_ROW_0 = {
                _X_, _X_, _X_,
                ___, _O_, ___,
                _O_, ___, ___,
        };

        /**
         * A board in which _O_ has won by filling row 1.
         *
         * This board has an empty space at location 7.
         */
        public static final Boolean[] BOARD_O_WINS_ROW_1 = {
                _X_, _X_, _O_,
                _O_, _O_, _O_,
                _X_, ___, _X_,
        };

        /**
         * A board in which _O_ has won by filling row 2.
         *
         * This board has empty spaces at locations 1, 3, and 5.
         */
        public static final Boolean[] BOARD_O_WINS_ROW_2 = {
                _X_, ___, _X_,
                ___, _X_, ___,
                _O_, _O_, _O_,
        };

        /**
         * A board in which _X_ has won by filling diagonal upper-left to
         * lower-right.
         *
         * This board has empty spaces at locations 2, 3, 6, and 7.
         */
        public static final Boolean[] BOARD_X_WINS_BACKSLASH = {
                _X_, _O_, ___,
                ___, _X_, _O_,
                ___, ___, _X_,
        };

        /**
         * A board in which _O_ has won by filling diagonal upper-right to
         * lower-left.
         *
         * This board has empty spaces at locations 5, 7, and 8.
         */
        public static final Boolean[] BOARD_O_WINS_SLASH = {
                _X_, _X_, _O_,
                _X_, _O_, ___,
                _O_, ___, ___,
        };

        /**
         * A board with only 8 spaces, one too few.
         */
        public static final Boolean[] BOARD_INVALID_SHORT_ARRAY = {
                ___, ___, ___,
                ___, ___, ___,
                ___, ___
        };

        /**
         * A board with 10 spaces, one too many.
         */
        public static final Boolean[] BOARD_INVALID_LONG_ARRAY = {
                ___, ___, ___,
                ___, ___, ___,
                ___, ___, ___, ___
        };

        /**
         * A board with 4 X's but only 2 O's, in which X has moved once too many
         * turns.
         */
        public static final Boolean[] BOARD_INVALID_TOO_MANY_X = {
                _X_, _O_, _X_,
                _O_, _X_, _X_,
                ___, ___, ___
        };

        /**
         * A board with 3 O's but only 2 X's, in which O has moved once too many
         * turns.
         */
        public static final Boolean[] BOARD_INVALID_TOO_MANY_O = {
                _X_, _O_, _X_,
                _O_, _O_, ___,
                ___, ___, ___,
        };
    }

    static class InvalidBoardTests implements ArgumentsProvider {
        static class TestParams {
            private final String description;
            private final Boolean[] board;
            private final String message;

            private TestParams(String description, Boolean[] board, String message) {
                this.description = description;
                this.board = board;
                this.message = message;
            }

            @Override
            public String toString() {
                return description;
            }
        }

        private static final TestParams TEST_BOARD_INVALID_SHORT_ARRAY = new TestParams(
                "fewer than 9 spaces",
                Boards.BOARD_INVALID_SHORT_ARRAY,
                "board must have exactly 9 elements"
        );
        private static final TestParams TEST_BOARD_INVALID_LONG_ARRAY = new TestParams(
                "more than 9 spaces",
                Boards.BOARD_INVALID_LONG_ARRAY,
                "board must have exactly 9 elements"
        );
        private static final TestParams TEST_BOARD_INVALID_TOO_MANY_X = new TestParams(
                "X has moved out of turn",
                Boards.BOARD_INVALID_TOO_MANY_X,
                "'X' has moved out of turn"
        );
        private static final TestParams TEST_BOARD_INVALID_TOO_MANY_O = new TestParams(
                "O has moved out of turn",
                Boards.BOARD_INVALID_TOO_MANY_O,
                "'O' has moved out of turn"
        );

        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    TEST_BOARD_INVALID_SHORT_ARRAY,
                    TEST_BOARD_INVALID_LONG_ARRAY,
                    TEST_BOARD_INVALID_TOO_MANY_X,
                    TEST_BOARD_INVALID_TOO_MANY_O
            ).map(Arguments::of);
        }
    }

    static class WinningBoardsTests implements ArgumentsProvider {
        static class TestParams {
            private final String description;
            private final Boolean[] board;
            private final Boolean winner;

            private TestParams(String description, Boolean[] board, Boolean winner) {
                this.description = description;
                this.board = board;
                this.winner = winner;
            }

            @Override
            public String toString() {
                return description;
            }
        }

        private static final TestParams TEST_DRAW = new TestParams(
                "no winner",
                Boards.BOARD_DRAW,
                null
        );
        private static final TestParams TEST_WIN_COL_0 = new TestParams(
                "X wins column 0",
                Boards.BOARD_X_WINS_COL_0,
                Game.PIECE_X
        );
        private static final TestParams TEST_WIN_COL_1 = new TestParams(
                "O wins column 1",
                Boards.BOARD_O_WINS_COL_1,
                Game.PIECE_O
        );
        private static final TestParams TEST_WIN_COL_2 = new TestParams(
                "X wins column 2",
                Boards.BOARD_X_WINS_COL_2,
                Game.PIECE_X
        );
        private static final TestParams TEST_WIN_ROW_0 = new TestParams(
                "X wins row 0",
                Boards.BOARD_X_WINS_ROW_0,
                Game.PIECE_X
        );
        private static final TestParams TEST_WIN_ROW_1 = new TestParams(
                "O wins row 1",
                Boards.BOARD_O_WINS_ROW_1,
                Game.PIECE_O
        );
        private static final TestParams TEST_WIN_ROW_2 = new TestParams(
                "O wins row 2",
                Boards.BOARD_O_WINS_ROW_2,
                Game.PIECE_O
        );
        private static final TestParams TEST_WIN_BACKSLASH = new TestParams(
                "X wins \\ diagonal",
                Boards.BOARD_X_WINS_BACKSLASH,
                Game.PIECE_X
        );
        private static final TestParams TEST_WIN_SLASH = new TestParams(
                "O wins / diagonal",
                Boards.BOARD_O_WINS_SLASH,
                Game.PIECE_O
        );

        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    TEST_DRAW,
                    TEST_WIN_COL_0,
                    TEST_WIN_COL_1,
                    TEST_WIN_COL_2,
                    TEST_WIN_ROW_0,
                    TEST_WIN_ROW_1,
                    TEST_WIN_ROW_2,
                    TEST_WIN_BACKSLASH,
                    TEST_WIN_SLASH
            ).map(Arguments::of);
        }
    }

    static class NextPlayerTests implements ArgumentsProvider {
        static class TestParams {
            private final String description;
            private final Boolean[] board;
            private final Boolean nextPlayer;

            private TestParams(String description, Boolean[] board, Boolean nextPlayer) {
                this.description = description;
                this.board = board;
                this.nextPlayer = nextPlayer;
            }

            @Override
            public String toString() {
                return description;
            }
        }

        private static final TestParams TEST_NEW_GAME = new TestParams(
                "new game; X goes first",
                Boards.BOARD_EMPTY,
                Game.PIECE_X
        );
        private static final TestParams TEST_O_NEXT = new TestParams(
                "X has played; O goes next",
                Boards.BOARD_X,
                Game.PIECE_O
        );
        private static final TestParams TEST_X_NEXT = new TestParams(
                "both have played; X goes next",
                Boards.BOARD_XO,
                Game.PIECE_X
        );
        private static final TestParams TEST_ALREADY_WON = new TestParams(
                "game already won",
                Boards.BOARD_X_WINS_COL_0,
                null
        );
        private static final TestParams TEST_DRAW = new TestParams(
                "game is a draw",
                Boards.BOARD_DRAW,
                null
        );

        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    TEST_NEW_GAME,
                    TEST_O_NEXT,
                    TEST_X_NEXT,
                    TEST_ALREADY_WON,
                    TEST_DRAW
            ).map(Arguments::of);
        }
    }

    static class InvalidMoveTests implements ArgumentsProvider {
        static class TestParams {
            private final String description;
            private final Boolean[] board;
            private final Boolean piece;
            private final int idx;
            private final String message;

            private TestParams(String description, Boolean[] board, Boolean piece, int idx, String message) {
                this.description = description;
                this.board = board;
                this.piece = piece;
                this.idx = idx;
                this.message = message;
            }

            @Override
            public String toString() {
                return description;
            }
        }

        private static final TestParams TEST_INVALID_IDX_9 = new TestParams(
                "invalid idx 9",
                Boards.BOARD_EMPTY,
                Game.PIECE_X,
                9,
                "invalid location"
        );
        private static final TestParams TEST_INVALID_IDX_NEGATIVE_1 = new TestParams(
                "invalid idx -1",
                Boards.BOARD_EMPTY,
                Game.PIECE_X,
                -1,
                "invalid location"
        );
        private static final TestParams TEST_NULL_PIECE = new TestParams(
                "null piece",
                Boards.BOARD_EMPTY,
                null,
                0,
                "piece must be specified"
        );
        private static final TestParams TEST_IDX_ALREADY_OCCUPIED = new TestParams(
                "space already occupied",
                Boards.BOARD_X,
                Game.PIECE_O,
                0,
                "the space is already occupied"
        );
        private static final TestParams TEST_X_MOVES_OUT_OF_TURN = new TestParams(
                "X moves out of turn",
                Boards.BOARD_X,
                Game.PIECE_X,
                4,
                "'X' has moved out of turn"
        );
        private static final TestParams TEST_O_MOVES_OUT_OF_TURN = new TestParams(
                "O moves out of turn",
                Boards.BOARD_XO,
                Game.PIECE_O,
                4,
                "'O' has moved out of turn"
        );
        private static final TestParams TEST_X_ALREADY_WON = new TestParams(
                "X already won",
                Boards.BOARD_X_WINS_COL_0,
                Game.PIECE_O,
                5,
                "'X' has already won"
        );
        private static final TestParams TEST_O_ALREADY_WON = new TestParams(
                "O already won",
                Boards.BOARD_O_WINS_COL_1,
                Game.PIECE_X,
                5,
                "'O' has already won"
        );

        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    TEST_INVALID_IDX_9,
                    TEST_INVALID_IDX_NEGATIVE_1,
                    TEST_NULL_PIECE,
                    TEST_IDX_ALREADY_OCCUPIED,
                    TEST_X_MOVES_OUT_OF_TURN,
                    TEST_O_MOVES_OUT_OF_TURN,
                    TEST_X_ALREADY_WON,
                    TEST_O_ALREADY_WON
            ).map(Arguments::of);
        }
    }


    @Nested
    class Construction {
        @Test
        void defaultConstructor() {
            Game game = new Game();
            assertArrayEquals(Boards.BOARD_EMPTY,
                    game.getBoard(),
                    "empty initial board");
        }

        @Test
        void boardInitialized() throws InvalidBoardException {
            final Boolean[] gameBoard = Boards.BOARD_DRAW;
            Game game =  new Game( gameBoard.clone() );
            assertArrayEquals(gameBoard, game.getBoard(), "initialized game board");
        }

        @ParameterizedTest
        @ArgumentsSource(InvalidBoardTests.class)
        void invalidBoardInitialization(InvalidBoardTests.TestParams params) {
            InvalidBoardException e = assertThrows(InvalidBoardException.class,
                    () -> new Game(params.board),
                    params.description + ": exception");
            assertEquals(params.message, e.getMessage(), params.description + ": message");
        }
    }

    @ParameterizedTest
    @ArgumentsSource(WinningBoardsTests.class)
    void findWinner(WinningBoardsTests.TestParams params) throws InvalidBoardException {
        Game game = new Game(params.board);
        Boolean winner = game.findWinner();
        assertEquals(params.winner, winner, params.description + ": winner");
    }

    @ParameterizedTest
    @ArgumentsSource(NextPlayerTests.class)
    void findNextPlayer(NextPlayerTests.TestParams params) throws InvalidBoardException {
        Game game = new Game(params.board);
        Boolean nextPlayer = game.findNextPlayer();
        assertEquals(params.nextPlayer, nextPlayer, params.description + ": nextPlayer");
    }

    @Nested
    class Move {
        @Test
        void validMove() throws InvalidMoveException {
            Game game = new Game();
            assumeTrue(Arrays.deepEquals(Boards.BOARD_EMPTY, game.getBoard()));

            game.move(Game.PIECE_X, 0);
            assertArrayEquals(Boards.BOARD_X, game.getBoard(),
                    "board with X at index 0");

            game.move(Game.PIECE_O, 1);
            assertArrayEquals(Boards.BOARD_XO, game.getBoard(),
                    "board with O added at index 1");
        }

        @ParameterizedTest
        @ArgumentsSource(InvalidMoveTests.class)
        void invalidMove(InvalidMoveTests.TestParams params) throws InvalidBoardException {
            Game game = new Game(params.board);

            InvalidMoveException e = assertThrows(InvalidMoveException.class,
                    () -> game.move(params.piece, params.idx),
                    params.description + ": exception");

            assertEquals(params.message, e.getMessage(), params.description + ": message");
            assertEquals(params.piece, e.getPiece(), params.description + ": piece");
            assertEquals(params.idx, e.getIdx(), params.description + ": idx");
        }
    }
}