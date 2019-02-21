package test;

import main.game.Game;
import main.game.messageboard.Message;
import main.game.probability.SuccessRateEnum;
import main.game.reputation.ReputationAffectingEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class TestTools {

    static List<Message> generateMessages() {
        List<Message> messages = new ArrayList<>();
        for (ReputationAffectingEnum reputationAffectingEnum : ReputationAffectingEnum.values()) {
            for (SuccessRateEnum successRateEnum : SuccessRateEnum.values()) {
                for (int turn = 0; turn < generateNumberBetween(1,5); turn++) {
                    messages.add(new Message(String.valueOf(System.currentTimeMillis()),
                            successRateEnum, reputationAffectingEnum, String.valueOf(generateNumberBetween(5, 60))));
                }
            }
        }
        return messages;
    }

    private static int generateNumberBetween(int low, int high) {
        // Link : https://stackoverflow.com/questions/5271598/java-generate-random-number-between-two-given-values
        Random r = new Random();
        return r.nextInt(high-low) + low;
    }

    public static void removeBestChoicesFromMap(Game game, ReputationAffectingEnum repAffect, SuccessRateEnum endSuccessRate) {
        for (int repInt = SuccessRateEnum.VERY_HIGH_SUCCESS_RATE.getSuccessRate(); repInt >= endSuccessRate.getSuccessRate(); repInt--) {
            game.getMessageBoard().get(repAffect).get(SuccessRateEnum.getSuccessRateEnum(repInt)).clear();
        }
    }
}
