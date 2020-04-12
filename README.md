# TicTacToeJ

A simple Tic-Tac-Toe implementation in Java

This is a toy project that implements a simple game of Tic-Tac-Toe.

You can play with it using a simple REPL.

## Building

From a suitable directory:

```sh
git clone https://github.com/JTimothyKing/TicTacToeJ.git
cd TicTacToeJ
./gradlew build
./gradlew jar
```

## Running
```sh
java -jar build/libs/TicTacToeJ-0.01-SNAPSHOT.jar
```

At the `> ` prompt, there are three commands supported:

* `new` - start a new game

* `move X 0` - move player X (or O) to location 0 (where locations are numbered 0-8)

* `exit` - exit the program back to the shell
