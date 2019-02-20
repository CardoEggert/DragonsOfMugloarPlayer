package main.game.reputation;

public enum ReputationAffectingEnum {
    LOW_REP_AFFECT(0),
    MEDIUM_REP_AFFECT(1),
    HIGH_REP_AFFECT(2);

    private final int repAffectLevel;

    ReputationAffectingEnum(int level) {
        this.repAffectLevel = level;
    }

    public int getRepAffectLevel() {
        return repAffectLevel;
    }

    public static ReputationAffectingEnum getRepAffectEnum(int level) {
        switch (level) {
            case 0:
                return LOW_REP_AFFECT;
            case 1:
                return MEDIUM_REP_AFFECT;
            case 2:
                return HIGH_REP_AFFECT;
            default:
                return null;
        }
    }
}
