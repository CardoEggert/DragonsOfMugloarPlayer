package main.game.messageboard;

import main.game.probability.SuccessRateEnum;
import main.game.reputation.ReputationAffectingEnum;

public class Message {
    private String id;
    private SuccessRateEnum successRateEnum;
    private ReputationAffectingEnum reputationAffectingEnum;
    private String probabilityString;
    private String message;
    private String reward;
    private int expiresIn;

    public Message(String id, SuccessRateEnum successRateEnum, ReputationAffectingEnum reputationAffectingEnum,
                   String probabilityString, String message, String reward, int expiresIn) {
        this.id = id;
        this.successRateEnum = successRateEnum;
        this.reputationAffectingEnum = reputationAffectingEnum;
        this.probabilityString = probabilityString;
        this.message = message;
        this.reward = reward;
        this.expiresIn = expiresIn;
    }

    // For tests
    public Message(String id, SuccessRateEnum successRateEnum, ReputationAffectingEnum reputationAffectingEnum, String reward) {
        this.id = id;
        this.successRateEnum = successRateEnum;
        this.reputationAffectingEnum = reputationAffectingEnum;
        this.reward = reward;
    }

    public String getId() {
        return id;
    }

    public SuccessRateEnum getSuccessRateEnum() {
        return successRateEnum;
    }

    public String getReward() {
        return reward;
    }

    public ReputationAffectingEnum getReputationAffectingEnum() {
        return reputationAffectingEnum;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", successRateEnum=" + successRateEnum +
                ", reputationAffectingEnum=" + reputationAffectingEnum +
                ", probabilityString='" + probabilityString + '\'' +
                ", message='" + message + '\'' +
                ", reward='" + reward + '\'' +
                ", expiresIn=" + expiresIn +
                '}';
    }
}
