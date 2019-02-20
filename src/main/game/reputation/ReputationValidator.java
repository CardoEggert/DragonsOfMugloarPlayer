package main.game.reputation;

import static main.game.tools.Consts.*;

public class ReputationValidator {
    public static ReputationAffectingEnum validateReputationAffect(String message) {
        String loweredMessage = message.toLowerCase();
        if (loweredMessage.contains(STEAL) || loweredMessage.contains(ADVERTISEMENT)) {
            return ReputationAffectingEnum.HIGH_REP_AFFECT;
        }
        if (loweredMessage.contains(MAGIC) || loweredMessage.contains(DERANGED)) {
            return ReputationAffectingEnum.MEDIUM_REP_AFFECT;
        }
        return ReputationAffectingEnum.LOW_REP_AFFECT;
    }
}
