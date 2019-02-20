package main.game.probability;

import static main.game.tools.Consts.*;

public class ProbabilityValidator {
    public static SuccessRateEnum validateProbability(String probability) {
        switch (probability) {
            case IMPOSSIBLE:
                return SuccessRateEnum.ZERO_SUCCESS_RATE;
            case SUICIDE_MISSION:
                return SuccessRateEnum.ZERO_SUCCESS_RATE;
            case RATHER_DETRIMENTAL:
                return SuccessRateEnum.LOW_SUCCESS_RATE;
            case PLAYING_WITH_FIRE:
                return SuccessRateEnum.LOW_SUCCESS_RATE;
            case RISKY:
                return SuccessRateEnum.LOWER_THAN_MEDIUM_SUCCESS_RATE;
            case GAMBLE: // Gamble is higher than 50% in the excel, but in reality it seemed to be more 50-50%
                return SuccessRateEnum.LOWER_THAN_MEDIUM_SUCCESS_RATE;
            case HMMM: // It is 78.7% probability in the excel file, but in reality it is more like 60%
                return SuccessRateEnum.HIGHER_THAN_MEDIUM_SUCCESS_RATE;
            case QUITE_LIKELY:
                return SuccessRateEnum.HIGH_SUCCESS_RATE;
            case A_WALK_IN_THE_PARK:
                return SuccessRateEnum.HIGH_SUCCESS_RATE;
            case PIECE_OF_CAKE:
                return SuccessRateEnum.VERY_HIGH_SUCCESS_RATE;
            case SURE_THING:
                return SuccessRateEnum.VERY_HIGH_SUCCESS_RATE;
            default:
                return null;
        }
    }
}
