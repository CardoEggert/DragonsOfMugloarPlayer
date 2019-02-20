package main.game.probability;

public enum SuccessRateEnum {
    ZERO_SUCCESS_RATE (0), // <5% success rate
    LOW_SUCCESS_RATE (1),  // 5-33% success rate
    LOWER_THAN_MEDIUM_SUCCESS_RATE (2), // 34-49% success rate
    HIGHER_THAN_MEDIUM_SUCCESS_RATE (3), // 51-74% success rate
    HIGH_SUCCESS_RATE (4), // 75-90% success rate
    VERY_HIGH_SUCCESS_RATE(5);// >90% success rate

    private final int probabilityLevel;

    SuccessRateEnum(int probabilityLevel) {
        this.probabilityLevel = probabilityLevel;
    }

    public int getSuccessRate() {
        return probabilityLevel;
    }


    public static SuccessRateEnum getSuccessRateEnum(int level) {
        switch (level) {
            case 0:
                return ZERO_SUCCESS_RATE;
            case 1:
                return LOW_SUCCESS_RATE;
            case 2:
                return LOWER_THAN_MEDIUM_SUCCESS_RATE;
            case 3:
                return HIGHER_THAN_MEDIUM_SUCCESS_RATE;
            case 4:
                return HIGH_SUCCESS_RATE;
            case 5:
                return VERY_HIGH_SUCCESS_RATE;
            default:
                return null;
        }
    }
}
