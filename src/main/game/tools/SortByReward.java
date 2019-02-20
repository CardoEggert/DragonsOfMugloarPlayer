package main.game.tools;

import main.game.messageboard.Message;

import java.util.Comparator;

public class SortByReward implements Comparator<Message> {

    public int compare(Message m1, Message m2)
    {
        Integer m1Reward = Integer.parseInt(m1.getReward());
        Integer m2Reward = Integer.parseInt(m2.getReward());

        if (m1Reward > m2Reward) {
            return -1;
        }
        if (m1Reward < m2Reward) {
            return 1;
        }
        return 0;
    }

}
