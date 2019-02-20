package main.game.tools;

import main.game.Game;
import main.game.messageboard.Message;
import main.game.probability.SuccessRateEnum;
import main.game.probability.ProbabilityValidator;
import main.game.reputation.ReputationValidator;
import main.game.shop.Shop;
import main.game.shop.storage.Item;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Mapper {

    public Mapper() {
    }

    public List<Message> mapMessageBoard(Object allMessagesFromMessageBoard) {
        List<Message> messages;
        if (allMessagesFromMessageBoard instanceof JSONObject) {
            messages = new ArrayList<>(1);
            messages.add(mapMessage((JSONObject) allMessagesFromMessageBoard));
            return messages;
        }
        JSONArray messageBoard = (JSONArray) allMessagesFromMessageBoard;
        int nrOfMessages = messageBoard.length();
        if (nrOfMessages > 0) {
            messages = new ArrayList<>(nrOfMessages);
            for (int messageIndex = 0; messageIndex < nrOfMessages; messageIndex++) {
                JSONObject messageJSON = (JSONObject) messageBoard.get(messageIndex);
                messages.add(mapMessage(messageJSON));
            }
            return messages;
        }
        return null;
    }


    private static Message mapMessage(JSONObject messageJSON) {
        if (messageJSON.length() != 1) {
            SuccessRateEnum probabilityValidator = ProbabilityValidator.validateProbability(messageJSON.get(Consts.PROBABILITY).toString());
            if (probabilityValidator != null) {
                return new Message(
                        messageJSON.get(Consts.AD_ID).toString(),
                        probabilityValidator,
                        ReputationValidator.validateReputationAffect(messageJSON.get(Consts.MESSAGE).toString()),
                        messageJSON.get(Consts.PROBABILITY).toString(),
                        messageJSON.get(Consts.MESSAGE).toString(),
                        messageJSON.get(Consts.REWARD).toString(),
                        Integer.parseInt(messageJSON.get(Consts.EXPIRES_IN).toString())
                );
            }
        }
        return null;
    }

    public void mapGameAfterTransaction(JSONObject afterTransaction, Game game) {
        game.setGold(Integer.parseInt(afterTransaction.get(Consts.GOLD).toString()));
        game.setLives(Integer.parseInt(afterTransaction.get(Consts.LIVES).toString()));
        game.setLevel(Integer.parseInt(afterTransaction.get(Consts.LEVEL).toString()));
    }


    private Item mapItem(JSONObject itemJSON) {
        if (itemJSON.length() == 3) {
            return new Item(itemJSON.get(Consts.ID).toString(),
                    itemJSON.get(Consts.NAME).toString(),
                    Integer.parseInt(itemJSON.get(Consts.COST).toString()));
        }
        return null;
    }


    public Shop mapShop(Object storage) {
        if (storage instanceof JSONObject) {
            List<Item> items = new ArrayList<>(1);
            Item item = mapItem((JSONObject) storage);
            if (item != null) items.add(item);
            return new Shop(items);
        } else if (storage instanceof JSONArray) {
            JSONArray shopStorage = (JSONArray) storage;
            int nrOfItems = shopStorage.length();
            if (nrOfItems > 0) {
                List<Item> items = new ArrayList<>(nrOfItems);
                for (int itemIndex = 0; itemIndex < nrOfItems; itemIndex++) {
                    JSONObject itemJSON = (JSONObject) shopStorage.get(itemIndex);
                    Item item = mapItem(itemJSON);
                    if (item != null) items.add(item);
                }
                return new Shop(items);
            }
        }
        return new Shop(null);
    }

    public void mapGameAfterSolving(JSONObject afterSolvingUpdate, Game game) {
        game.outputMessage("Result: " + afterSolvingUpdate);
        if (afterSolvingUpdate.length() == 7) {
            game.getLogs().add("Result: " + afterSolvingUpdate);
            game.setGold(Integer.parseInt(afterSolvingUpdate.get(Consts.GOLD).toString()));
            game.setLives(Integer.parseInt(afterSolvingUpdate.get(Consts.LIVES).toString()));
            game.setScore(Integer.parseInt(afterSolvingUpdate.get(Consts.SCORE).toString()));
            game.setHighScore(Integer.parseInt(afterSolvingUpdate.get(Consts.HIGH_SCORE).toString()));
            game.setTurn(Integer.parseInt(afterSolvingUpdate.get(Consts.TURN).toString()));
        }
    }

    public void mapGame(JSONObject returnBody, Game game) {
        game.setGameId(returnBody.get(Consts.GAME_ID).toString());
        game.setLives(Integer.parseInt(returnBody.get(Consts.LIVES).toString()));
        game.setGold(Integer.parseInt(returnBody.get(Consts.GOLD).toString()));
        game.setLevel(Integer.parseInt(returnBody.get(Consts.LEVEL).toString()));
        game.setScore(Integer.parseInt(returnBody.get(Consts.SCORE).toString()));
        game.setHighScore(Integer.parseInt(returnBody.get(Consts.HIGH_SCORE).toString()));
        game.setTurn(Integer.parseInt(returnBody.get(Consts.TURN).toString()));
    }
}
