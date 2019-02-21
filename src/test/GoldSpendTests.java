package test;

import main.game.Game;
import main.game.probability.SuccessRateEnum;
import main.game.reputation.ReputationAffectingEnum;
import org.junit.*;

import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static main.game.tools.Consts.HEALTH_POTION_COST;
import static main.game.tools.Consts.LOWEST_COSTING_UPGRADE;
import static main.game.tools.Consts.SAVE_FOR_UPGRADES;
import static org.junit.Assert.assertFalse;
import static test.TestTools.generateMessages;
import static test.TestTools.removeBestChoicesFromMap;

public class GoldSpendTests {

    private Game testGame;

    @Before
    public void setUp() {
        testGame = new Game();
        testGame.fillMessageBoard(generateMessages());
    }

    @Test
    public void buyingUpgradesWithLotsOfMoney() throws IOException {
        testGame.setGold(SAVE_FOR_UPGRADES); // If enough money saved, buy upgrades
        assertTrue(testGame.isBuyingUpgradesPossible());
    }

    @Test
    public void buyingUpgradesWithHighSuccessMessages() throws IOException {
        testGame.setGold(SAVE_FOR_UPGRADES - 100); // Gotta save more or wait until high success messages are done
        assertFalse(testGame.isBuyingUpgradesPossible());
    }

    @Test
    public void buyingUpgradesWithSomeGoldAndNoHighMessages() throws IOException {
        testGame.setGold(LOWEST_COSTING_UPGRADE);
        removeBestChoicesFromMap(testGame, ReputationAffectingEnum.LOW_REP_AFFECT, SuccessRateEnum.HIGH_SUCCESS_RATE);
        removeBestChoicesFromMap(testGame, ReputationAffectingEnum.MEDIUM_REP_AFFECT, SuccessRateEnum.HIGH_SUCCESS_RATE);
        assertTrue(testGame.isBuyingUpgradesPossible());
    }

    @Test
    public void buyingUpgradesWithNoGoldAndNoHighMessages() throws IOException {
        testGame.setGold(0); // If no money and no high success messages possible, don't let the player buy upgrades
        removeBestChoicesFromMap(testGame, ReputationAffectingEnum.LOW_REP_AFFECT, SuccessRateEnum.HIGH_SUCCESS_RATE);
        removeBestChoicesFromMap(testGame, ReputationAffectingEnum.MEDIUM_REP_AFFECT, SuccessRateEnum.HIGH_SUCCESS_RATE);
        assertFalse(testGame.isBuyingUpgradesPossible());
    }

    @Test
    public void buyingUpgradesWithNoGold() throws IOException {
        testGame.setGold(0); // Do not let the player buy upgrades with no money
        assertFalse(testGame.isBuyingUpgradesPossible());
    }

    @Test
    public void healthPotionRequirementCheck() {
        testGame.setLives(2); // Make the player buy health potions if lives go under 2
        testGame.setGold(HEALTH_POTION_COST);
        assertTrue(testGame.isHealthPotionsNeeded());
    }

}
