package main.game;

import java.io.*;
import java.util.*;

import main.game.messageboard.Message;
import main.game.probability.SuccessRateEnum;
import main.game.reputation.ReputationAffectingEnum;
import main.game.shop.Shop;
import main.game.shop.storage.Item;
import main.game.tools.Mapper;
import main.game.tools.SortByReward;
import main.game.tools.WebTools;

import static main.game.probability.SuccessRateEnum.*;
import static main.game.reputation.ReputationAffectingEnum.*;
import static main.game.tools.Consts.*;

public class Game {

    private static WebTools webService = new WebTools();
    private static Mapper mapper = new Mapper();

    private Map<ReputationAffectingEnum, Map<SuccessRateEnum, List<Message>>> messageBoard = new HashMap<>();
    private String gameId;
    private int lives;
    private int gold;
    private int level;
    private int score;
    private int highScore;
    private int turn;
    private List<String> logs = new ArrayList<>();

    public Game(){
        initializeMessageMap();
    }

    public void startGame() throws IOException {
        mapper.mapGame(webService.sendPostRequest("/game/start"), this);
        outputMessage("Starting new game " + this.gameId);
        updateMessages();
        playGame();
    }

    private void initializeMessageMap() {
        for (ReputationAffectingEnum repAffect : ReputationAffectingEnum.values()) {
            messageBoard.putIfAbsent(repAffect, new HashMap<>());
            for (SuccessRateEnum successRateEnum : SuccessRateEnum.values()) {
                messageBoard.get(repAffect).putIfAbsent(successRateEnum, new ArrayList<>());
            }
        }
    }

    private void updateMessages() throws IOException {
        outputMessage("Updating messageboard");
        List<Message> messages = mapper.mapMessageBoard(webService.sendGetRequest(this.gameId.concat("/messages")));
        emptyMessageBoard();
        fillMessageBoard(messages);
    }

    public void outputMessage(String message) {
        logs.add(message);
        System.out.println(message);
    }

    public void fillMessageBoard(List<Message> messages) {
        if (messages != null && !messages.isEmpty()) {
            for (Message message : messages) {
                if (message != null) {
                    messageBoard.get(message.getReputationAffectingEnum()).get(message.getSuccessRateEnum()).add(message);
                }
            }
            sortMessageMap();
        }
    }

    private void emptyMessageBoard() {
        for (ReputationAffectingEnum repAffect : ReputationAffectingEnum.values()) {
            messageBoard.putIfAbsent(repAffect, new HashMap<>());
            for (SuccessRateEnum successRateEnum : SuccessRateEnum.values()) {
                messageBoard.get(repAffect).get(successRateEnum).clear();
            }
        }
    }

    private void sortMessageMap() {
        if (messageBoard != null) {
            for (ReputationAffectingEnum repEnum : ReputationAffectingEnum.values()) {
                Map<SuccessRateEnum, List<Message>> successRateMap = messageBoard.getOrDefault(repEnum, null);
                if (successRateMap != null) {
                    for (SuccessRateEnum successRateEnum : SuccessRateEnum.values()) {
                        List<Message> messages = messageBoard.get(repEnum).getOrDefault(successRateEnum, null);
                        if (messages != null && messages.size() > 1) {
                            messages.sort(new SortByReward());
                        }
                    }
                }
            }
        }
    }

    private void solveMessage(Message mostRewardingMessage) throws IOException {
        outputMessage("Solving message: " + mostRewardingMessage);
        mapper.mapGameAfterSolving(webService.sendPostRequest(this.gameId + "/solve/" + mostRewardingMessage.getId()), this);
        updateMessages();
    }

    private Shop browseShop() throws IOException {
        return mapper.mapShop(webService.sendGetRequest(gameId.concat("/shop")));
    }


    private boolean buyItem(Item item, int gold) throws IOException {
        if (item != null && item.getCost() != 0 && item.getCost() <= gold) {
            outputMessage("Buying item: " + item);
            mapper.mapGameAfterTransaction(webService.sendPostRequest(gameId + "/shop/buy/" + item.getId()), this);
            return true;
        }
        return false;
    }

    private Item findMostExpensiveItemWithGoldAmount(Shop shop, Integer goldAmount) {
        Item mostExpensiveItem = new Item(null, null, 0);
        for (Item storageItem : shop.getStorage()) {
            if (goldAmount >= storageItem.getCost() && storageItem.getCost() >= mostExpensiveItem.getCost()) {
                mostExpensiveItem = storageItem;
            }
        }
        return mostExpensiveItem;
    }

    private void replenishLivesIfNecessery() throws IOException {
        boolean boughtHealthPotion = false;
        while (isHealthPotionsNeeded()) {
            if (!buyHealthPotion()) {
                boughtHealthPotion = true;
                break;
            }
        }
        if (boughtHealthPotion) updateMessages();
    }

    public boolean isHealthPotionsNeeded() {
        return this.lives < MAX_LIVES_TO_KEEP && this.gold >= HEALTH_POTION_COST;
    }

    private boolean buyHealthPotion() throws IOException {
        Shop shopInventory = browseShop();
        Item healthPotion = findHealthPotion(shopInventory);
        if (healthPotion != null) {
            return buyItem(healthPotion, this.gold);
        }
        return false;
    }

    private void buyUpgrades() throws IOException {
        if (isBuyingUpgradesPossible()) {
            Item bestShopItemWithGold;
            bestShopItemWithGold = findMostExpensiveItemWithGoldAmount(browseShop(), this.gold);
            if (bestShopItemWithGold != null && isTwoHealthPotionsAffordableAfterPurchase(bestShopItemWithGold.getCost())) {
                buyItem(bestShopItemWithGold, this.gold);
                updateMessages();
            }
        }
    }

    public boolean isBuyingUpgradesPossible()  throws IOException{
        return this.gold >= LOWEST_COSTING_UPGRADE && (isHighProbabilityMessagesAvailable() || this.gold >= SAVE_FOR_UPGRADES);
    }

    private boolean isHighProbabilityMessagesAvailable() throws IOException {
        if (messageBoard == null) updateMessages();
        for (ReputationAffectingEnum reputationAffectingEnum : Arrays.asList(LOW_REP_AFFECT, MEDIUM_REP_AFFECT)) {
            Map<SuccessRateEnum, List<Message>> successRateEnumListMap = messageBoard.getOrDefault(reputationAffectingEnum, null);
            if (successRateEnumListMap != null) {
                return anyHighSuccessMessagesPresent(successRateEnumListMap.getOrDefault(VERY_HIGH_SUCCESS_RATE, null))
                 && anyHighSuccessMessagesPresent(successRateEnumListMap.getOrDefault(HIGH_SUCCESS_RATE, null));
            }
        }
        return false;
    }

    private boolean anyHighSuccessMessagesPresent(List<Message> messages) {
        if (messages != null) {
            return messages.isEmpty();
        }
        return true;
    }

    private boolean isTwoHealthPotionsAffordableAfterPurchase(int cost) {
        return (cost + 2 * HEALTH_POTION_COST) <= this.gold;
    }

    private Item findHealthPotion(Shop shopInventory) {
        Optional<Item> returnableItem = shopInventory.getStorage().stream().filter(x -> x.getName().equals(HEALING_POTION)).findFirst();
        return returnableItem.orElse(null);
    }

    public List<String> getLogs() {
        return logs;
    }

    private void playGame() throws IOException {
        int timeToLive = 200;
        while (isAlive() && timeToLive > 0) {
            findBestAdsToSolve();
            buyUpgradesOptionally();
            timeToLive--;
        }
    }

    private boolean isAlive() {
        return this.lives > OUT_OF_LIVES;
    }

    private void findBestAdsToSolve() throws IOException {
        Message bestMessage = findBestAd();
        if (bestMessage != null) {
            solveMessage(bestMessage);
        }
    }

    public Message findBestAd() throws IOException {
        Message bestMessage;
        //Find easy ones firs
        bestMessage = stepThroughAds(LOW_REP_AFFECT, MEDIUM_REP_AFFECT, VERY_HIGH_SUCCESS_RATE, HIGHER_THAN_MEDIUM_SUCCESS_RATE);
        // If no more easy ones, check if, HIGH_AFFECT ONEs are available
        if (bestMessage == null) {
            bestMessage = stepThroughAds(HIGH_REP_AFFECT, HIGH_REP_AFFECT, VERY_HIGH_SUCCESS_RATE, HIGHER_THAN_MEDIUM_SUCCESS_RATE);
            // If nothing is found, suicide
            if (bestMessage == null) {
                bestMessage = stepThroughAds(LOW_REP_AFFECT, HIGH_REP_AFFECT, HIGHER_THAN_MEDIUM_SUCCESS_RATE, ZERO_SUCCESS_RATE);
            }
        }
        return bestMessage;
    }

    private Message stepThroughAds(ReputationAffectingEnum lowRepAffect, ReputationAffectingEnum maxRepAffect, SuccessRateEnum successRate, SuccessRateEnum maxSuccessRate) throws IOException {
        Message bestMessage;
        for (int successRateIndex = successRate.getSuccessRate(); successRateIndex >= maxSuccessRate.getSuccessRate(); successRateIndex--) {
            for (int reputationAffectIndex = lowRepAffect.getRepAffectLevel(); reputationAffectIndex <= maxRepAffect.getRepAffectLevel(); reputationAffectIndex++) {
                bestMessage = findMessage(reputationAffectIndex, successRateIndex);
                if (bestMessage != null) {
                    return bestMessage;
                }
            }
        }
        return null;
    }

    private Message findMessage(int repIndx, int successIndx) throws IOException {
        ReputationAffectingEnum repEnum = ReputationAffectingEnum.getRepAffectEnum(repIndx);
        SuccessRateEnum succesEnum = SuccessRateEnum.getSuccessRateEnum(successIndx);

        if (messageBoard == null || messageBoard.isEmpty()) updateMessages();

        if (repEnum != null && succesEnum != null) {
            Map<SuccessRateEnum, List<Message>> successRateMap = messageBoard.getOrDefault(repEnum, null);
            if (successRateMap != null) {
                List<Message> messages = successRateMap.getOrDefault(succesEnum, null);
                if (messages != null && !messages.isEmpty()) {
                    return messages.get(0); // First one is sorted the most rewarding
                }
            }
        }
        return null;
    }

    private void buyUpgradesOptionally() throws IOException {
        replenishLivesIfNecessery();
        buyUpgrades();
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public Map<ReputationAffectingEnum, Map<SuccessRateEnum, List<Message>>> getMessageBoard() {
        return messageBoard;
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameId='" + gameId + '\'' +
                ", lives=" + lives +
                ", gold=" + gold +
                ", level=" + level +
                ", score=" + score +
                ", highScore=" + highScore +
                ", turn=" + turn +
                '}';
    }
}