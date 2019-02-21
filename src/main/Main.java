package main;

import main.game.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final Integer NR_OF_GAMES = 1;

    public static void main(String[] args) throws IOException {
        // Starts a new main.game
        List<Game> games = new ArrayList<>(NR_OF_GAMES);

        for (int i = 0; i < NR_OF_GAMES; i++) {
            Game game = new Game();
            game.startGame();
            games.add(game);
        }

        for (Game game : games) {
            System.out.println(game);
        }

        analyzeScores(games);
    }

    // Use to see how all the game logs
    private static void showLogs(Game game) {
        game.getLogs().forEach(System.out::println);
    }

    private static void analyzeScores(List<Game> scores) {
        double aLevel = 0.0, aScore = 0.0, aTurn = 0.0;

        for (Game score : scores) {
            aLevel += score.getLevel();
            aScore += score.getScore();
            aTurn += score.getTurn();
        }
        aLevel = aLevel / scores.size();
        aScore = aScore / scores.size();
        aTurn = aTurn / scores.size();

        System.out.println("Average level: " + aLevel);
        System.out.println("Average score: " + aScore);
        System.out.println("Average turn: " + aTurn);
    }
}
