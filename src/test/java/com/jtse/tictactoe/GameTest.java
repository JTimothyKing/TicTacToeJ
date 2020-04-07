package com.jtse.tictactoe;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 */
class GameTest {
    @Nested
    class Construction {
        @Test
        void defaultConstructor() {
            Game game = new Game();
            assertArrayEquals(
                    new Boolean[]{
                            null, null, null,
                            null, null, null,
                            null, null, null,
                    },
                    game.getBoard(),
                    "empty initial board");
        }

        @Test
        void boardInitialized() {
            final Boolean[] gameBoard = new Boolean[]{
                true, null, null,
                true, false, null,
                false, null, null,
            };

            Game game = new Game( gameBoard.clone() );

            assertArrayEquals(gameBoard, game.getBoard(), "game board initialized");
        }
    }
}