package com.jtse.tictactoe;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

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
         * A board with no empty spaces but with no winner.
         */
        public static final Boolean[] BOARD_DRAW = {
                _X_, _O_, _X_,
                _X_, _O_, _O_,
                _O_, _X_, _X_,
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

    static class InvalidBoardsTests implements ArgumentsProvider {
        static class TestParams {
            public String description;
            public Boolean[] board;
            public String message;

            TestParams(String description, Boolean[] board, String message) {
                this.description = description;
                this.board = board;
                this.message = message;
            }

            @Override
            public String toString() {
                return description;
            }

        }

        private static final TestParams BOARD_INVALID_SHORT_ARRAY_TEST = new TestParams(
                "fewer than 9 spaces",
                Boards.BOARD_INVALID_SHORT_ARRAY,
                "board must have exactly 9 elements"
        );
        private static final TestParams BOARD_INVALID_LONG_ARRAY_TEST = new TestParams(
                "more than 9 spaces",
                Boards.BOARD_INVALID_LONG_ARRAY,
                "board must have exactly 9 elements"
        );
        private static final TestParams BOARD_INVALID_TOO_MANY_X_TEST = new TestParams(
                "X has moved out of turn",
                Boards.BOARD_INVALID_TOO_MANY_X,
                "'X' has moved out of turn"
        );
        private static final TestParams BOARD_INVALID_TOO_MANY_O_TEST = new TestParams(
                "O has moved out of turn",
                Boards.BOARD_INVALID_TOO_MANY_O,
                "'O' has moved out of turn"
        );

        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    BOARD_INVALID_SHORT_ARRAY_TEST,
                    BOARD_INVALID_LONG_ARRAY_TEST,
                    BOARD_INVALID_TOO_MANY_X_TEST,
                    BOARD_INVALID_TOO_MANY_O_TEST
            ).map(Arguments::of);
        }
    }


    @Nested
    class Construction {
        @Test
        void defaultConstructor() {
            Game game = new Game();
            assertArrayEquals(
                    Boards.BOARD_EMPTY,
                    game.getBoard(),
                    "empty initial board");
        }

        @Test
        void boardInitialized() throws InvalidBoardException {
            final Boolean[] gameBoard = Boards.BOARD_DRAW;
            Game game =  new Game( gameBoard.clone() );
            assertArrayEquals(gameBoard, game.getBoard(), "game board initialized");
        }

        @ParameterizedTest
        @ArgumentsSource(InvalidBoardsTests.class)
        void invalidBoardInitialization(InvalidBoardsTests.TestParams params) {
            InvalidBoardException e = assertThrows(
                    InvalidBoardException.class,
                    () -> new Game(params.board),
                    params.description
            );
            assertEquals(params.message, e.getMessage(), params.description);
        }
    }
}