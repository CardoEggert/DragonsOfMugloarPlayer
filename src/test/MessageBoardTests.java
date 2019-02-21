package test;

import main.game.Game;
import main.game.messageboard.Message;
import main.game.probability.SuccessRateEnum;
import main.game.reputation.ReputationAffectingEnum;
import org.junit.*;

import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.*;
import static main.game.probability.SuccessRateEnum.*;
import static main.game.reputation.ReputationAffectingEnum.*;
import static test.TestTools.generateMessages;

public class MessageBoardTests {

    private Game testGame;

    @Before
    public void setUp() {
        testGame = new Game();
        testGame.fillMessageBoard(generateMessages());
    }

    @Test
    public void mapperNeverNull() {
        for (ReputationAffectingEnum reputationAffectingEnum : ReputationAffectingEnum.values()) {
            for (SuccessRateEnum successRateEnum : SuccessRateEnum.values()) {
                assertNotNull(testGame.getMessageBoard().get(reputationAffectingEnum).get(successRateEnum));
            }
        }
    }

    @Test
    public void firstElementIsMostRewarding() {
        for (ReputationAffectingEnum reputationAffectingEnum : ReputationAffectingEnum.values()) {
            for (SuccessRateEnum successRateEnum : SuccessRateEnum.values()) {
                assertEquals(testGame.getMessageBoard().get(reputationAffectingEnum).get(successRateEnum).get(0).getReward(),
                        findBiggestReward(testGame.getMessageBoard().get(reputationAffectingEnum).get(successRateEnum)));
            }
        }
    }

    /*
    Test plan:
    REP: LOW_REP_AFFECT(0), MID_REP_AFFECT(1), HIGH_REP_AFFECT(2)
    SUCCESS_RATE: VERY_HIGH_SUCCESS_RATE(5), HIGH_SUCCESS_RATE(4), HIGHER_THAN_MEDIUM_SUCCESS_RATE(3), LOWER_THAN_MEDIUM_SUCCESS_RATE(2), LOW_SUCCESS_RATE(1), ZERO_SUCCESS_RATE(0);
    It should pick in the following orders if nothing else is possible
    REP | SUCCESS
    1. 0 | 5
    2. 1 | 5
    3. 0 | 4
    4. 1 | 4
    5. 0 | 3
    6. 1 | 3
    7. 2 | 5
    8. 2 | 4
    9. 2 | 3
    10. 0 | 2
    11. 1 | 2
    12. 2 | 2
    13. 0 | 1
    14. 1 | 1
    15. 2 | 1
    16. 0 | 0
    17. 1 | 0
    18. 2 | 0

     */

    //    1. 0 | 5
    @Test
    public void searchCase1() throws IOException {
        assertEquals(testGame.findBestAd(), findBestChoice(LOW_REP_AFFECT, VERY_HIGH_SUCCESS_RATE));
    }

    //    2. 1 | 5

    @Test
    public void searchCase2() throws IOException {
        removeBestChoicesFromMap(LOW_REP_AFFECT, VERY_HIGH_SUCCESS_RATE);
        assertEquals(testGame.findBestAd(), findBestChoice(MEDIUM_REP_AFFECT, VERY_HIGH_SUCCESS_RATE));
    }

    //    3. 0 | 4
    @Test
    public void searchCase3() throws IOException {
        removeBestChoicesFromMap(LOW_REP_AFFECT, VERY_HIGH_SUCCESS_RATE);
        removeBestChoicesFromMap(MEDIUM_REP_AFFECT, VERY_HIGH_SUCCESS_RATE);
        assertEquals(testGame.findBestAd(), findBestChoice(LOW_REP_AFFECT, HIGH_SUCCESS_RATE));
    }
    //    4. 1 | 4
    @Test
    public void searchCase4() throws IOException {
        removeBestChoicesFromMap(LOW_REP_AFFECT, HIGH_SUCCESS_RATE);
        removeBestChoicesFromMap(MEDIUM_REP_AFFECT, VERY_HIGH_SUCCESS_RATE);
        assertEquals(testGame.findBestAd(), findBestChoice(MEDIUM_REP_AFFECT, HIGH_SUCCESS_RATE));
    }
    //    5. 0 | 3
    @Test
    public void searchCase5() throws IOException {
        removeBestChoicesFromMap(LOW_REP_AFFECT, HIGH_SUCCESS_RATE);
        removeBestChoicesFromMap(MEDIUM_REP_AFFECT, HIGH_SUCCESS_RATE);
        assertEquals(testGame.findBestAd(), findBestChoice(LOW_REP_AFFECT, HIGHER_THAN_MEDIUM_SUCCESS_RATE));
    }
    //    6. 1 | 3
    @Test
    public void searchCase6() throws IOException {
        removeBestChoicesFromMap(LOW_REP_AFFECT, HIGHER_THAN_MEDIUM_SUCCESS_RATE);
        removeBestChoicesFromMap(MEDIUM_REP_AFFECT, HIGH_SUCCESS_RATE);
        assertEquals(testGame.findBestAd(), findBestChoice(MEDIUM_REP_AFFECT, HIGHER_THAN_MEDIUM_SUCCESS_RATE));
    }
    //    7. 2 | 5
    @Test
    public void searchCase7() throws IOException {
        removeBestChoicesFromMap(LOW_REP_AFFECT, HIGHER_THAN_MEDIUM_SUCCESS_RATE);
        removeBestChoicesFromMap(MEDIUM_REP_AFFECT, HIGHER_THAN_MEDIUM_SUCCESS_RATE);
        assertEquals(testGame.findBestAd(), findBestChoice(HIGH_REP_AFFECT, VERY_HIGH_SUCCESS_RATE));
    }
    //    8. 2 | 4
    @Test
    public void searchCase8() throws IOException {
        removeBestChoicesFromMap(LOW_REP_AFFECT, HIGHER_THAN_MEDIUM_SUCCESS_RATE);
        removeBestChoicesFromMap(MEDIUM_REP_AFFECT, HIGHER_THAN_MEDIUM_SUCCESS_RATE);
        removeBestChoicesFromMap(HIGH_REP_AFFECT, VERY_HIGH_SUCCESS_RATE);
        assertEquals(testGame.findBestAd(), findBestChoice(HIGH_REP_AFFECT, HIGH_SUCCESS_RATE));
    }
    //    9. 2 | 3
    @Test
    public void searchCase9() throws IOException {
        removeBestChoicesFromMap(LOW_REP_AFFECT, HIGHER_THAN_MEDIUM_SUCCESS_RATE);
        removeBestChoicesFromMap(MEDIUM_REP_AFFECT, HIGHER_THAN_MEDIUM_SUCCESS_RATE);
        removeBestChoicesFromMap(HIGH_REP_AFFECT, HIGH_SUCCESS_RATE);
        assertEquals(testGame.findBestAd(), findBestChoice(HIGH_REP_AFFECT, HIGHER_THAN_MEDIUM_SUCCESS_RATE));
    }
    //    10. 0 | 2
    @Test
    public void searchCase10() throws IOException {
        removeBestChoicesFromMap(LOW_REP_AFFECT, HIGHER_THAN_MEDIUM_SUCCESS_RATE);
        removeBestChoicesFromMap(MEDIUM_REP_AFFECT, HIGHER_THAN_MEDIUM_SUCCESS_RATE);
        removeBestChoicesFromMap(HIGH_REP_AFFECT, HIGHER_THAN_MEDIUM_SUCCESS_RATE);
        assertEquals(testGame.findBestAd(), findBestChoice(LOW_REP_AFFECT, LOWER_THAN_MEDIUM_SUCCESS_RATE));
    }
    //    11. 1 | 2
    @Test
    public void searchCase11() throws IOException {
        removeBestChoicesFromMap(LOW_REP_AFFECT, LOWER_THAN_MEDIUM_SUCCESS_RATE);
        removeBestChoicesFromMap(MEDIUM_REP_AFFECT, HIGHER_THAN_MEDIUM_SUCCESS_RATE);
        removeBestChoicesFromMap(HIGH_REP_AFFECT, HIGHER_THAN_MEDIUM_SUCCESS_RATE);
        assertEquals(testGame.findBestAd(), findBestChoice(MEDIUM_REP_AFFECT, LOWER_THAN_MEDIUM_SUCCESS_RATE));
    }
    //    12. 2 | 2
    @Test
    public void searchCase12() throws IOException {
        removeBestChoicesFromMap(LOW_REP_AFFECT, LOWER_THAN_MEDIUM_SUCCESS_RATE);
        removeBestChoicesFromMap(MEDIUM_REP_AFFECT, LOWER_THAN_MEDIUM_SUCCESS_RATE);
        removeBestChoicesFromMap(HIGH_REP_AFFECT, HIGHER_THAN_MEDIUM_SUCCESS_RATE);
        assertEquals(testGame.findBestAd(), findBestChoice(HIGH_REP_AFFECT, LOWER_THAN_MEDIUM_SUCCESS_RATE));
    }
    //    13. 0 | 1
    @Test
    public void searchCase13() throws IOException {
        removeBestChoicesFromMap(LOW_REP_AFFECT, LOWER_THAN_MEDIUM_SUCCESS_RATE);
        removeBestChoicesFromMap(MEDIUM_REP_AFFECT, LOWER_THAN_MEDIUM_SUCCESS_RATE);
        removeBestChoicesFromMap(HIGH_REP_AFFECT, LOWER_THAN_MEDIUM_SUCCESS_RATE);
        assertEquals(testGame.findBestAd(), findBestChoice(LOW_REP_AFFECT, LOW_SUCCESS_RATE));
    }
    //    14. 1 | 1
    @Test
    public void searchCase14() throws IOException {
        removeBestChoicesFromMap(LOW_REP_AFFECT, LOW_SUCCESS_RATE);
        removeBestChoicesFromMap(MEDIUM_REP_AFFECT, LOWER_THAN_MEDIUM_SUCCESS_RATE);
        removeBestChoicesFromMap(HIGH_REP_AFFECT, LOWER_THAN_MEDIUM_SUCCESS_RATE);
        assertEquals(testGame.findBestAd(), findBestChoice(MEDIUM_REP_AFFECT, LOW_SUCCESS_RATE));
    }
    //    15. 2 | 1
    @Test
    public void searchCase15() throws IOException {
        removeBestChoicesFromMap(LOW_REP_AFFECT, LOW_SUCCESS_RATE);
        removeBestChoicesFromMap(MEDIUM_REP_AFFECT, LOW_SUCCESS_RATE);
        removeBestChoicesFromMap(HIGH_REP_AFFECT, LOWER_THAN_MEDIUM_SUCCESS_RATE);
        assertEquals(testGame.findBestAd(), findBestChoice(HIGH_REP_AFFECT, LOW_SUCCESS_RATE));
    }
    //    16. 0 | 0
    @Test
    public void searchCase16() throws IOException {
        removeBestChoicesFromMap(LOW_REP_AFFECT, LOW_SUCCESS_RATE);
        removeBestChoicesFromMap(MEDIUM_REP_AFFECT, LOW_SUCCESS_RATE);
        removeBestChoicesFromMap(HIGH_REP_AFFECT, LOW_SUCCESS_RATE);
        assertEquals(testGame.findBestAd(), findBestChoice(LOW_REP_AFFECT, ZERO_SUCCESS_RATE));
    }
    //    17. 1 | 0
    @Test
    public void searchCase17() throws IOException {
        removeBestChoicesFromMap(LOW_REP_AFFECT, ZERO_SUCCESS_RATE);
        removeBestChoicesFromMap(MEDIUM_REP_AFFECT, LOW_SUCCESS_RATE);
        removeBestChoicesFromMap(HIGH_REP_AFFECT, LOW_SUCCESS_RATE);
        assertEquals(testGame.findBestAd(), findBestChoice(MEDIUM_REP_AFFECT, ZERO_SUCCESS_RATE));
    }
    //    18. 2 | 0
    @Test
    public void searchCase18() throws IOException {
        removeBestChoicesFromMap(LOW_REP_AFFECT, ZERO_SUCCESS_RATE);
        removeBestChoicesFromMap(MEDIUM_REP_AFFECT, ZERO_SUCCESS_RATE);
        removeBestChoicesFromMap(HIGH_REP_AFFECT, LOW_SUCCESS_RATE);
        assertEquals(testGame.findBestAd(), findBestChoice(HIGH_REP_AFFECT, ZERO_SUCCESS_RATE));
    }

    private void removeBestChoicesFromMap(ReputationAffectingEnum repAffect, SuccessRateEnum endSuccessRate) {
        TestTools.removeBestChoicesFromMap(testGame, repAffect, endSuccessRate);
    }


    private Message findBestChoice(ReputationAffectingEnum repAffect, SuccessRateEnum successRate) {
        return testGame.getMessageBoard().get(repAffect).get(successRate).get(0);
    }

    private String findBiggestReward(List<Message> messages) {
        Message biggestRewardMessage = messages.get(0);
        for (Message message : messages) {
            if (Integer.parseInt(biggestRewardMessage.getReward()) <= Integer.parseInt(message.getReward())) {
                biggestRewardMessage = message;
            }
        }
        return biggestRewardMessage.getReward();
    }
}
