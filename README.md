# [Dragons of Mugloar](https://www.dragonsofmugloar.com/) Player

This is my proposed solution to the Bigbank AS test task. The proposed solution is for the scripting adventure.

## Notes

### Time to develop
* 242 minutes - Analysis and experimentation
* 988 minutes - Developing, debugging, testing, documenting
Total: 1230 minutes ( 20h 30 min )

## The solution
### Reputation and Success rate validator
The main solution uses two validators that take into account the probability value of the message and the message information.
The message information has been analyzed (using about a hundred games worth of data) and been used to determine specific words that might affect reputation. 
The probability value has been analyzed (using about a hundred games worth of data) and been used to determine probability values for a chance of success.
### MessageBoard Map
The solution uses a map to pick out the best choice. It maps the message firstly by reputation affecting enumerator then using probability affecting enumerator. 
Then the message is put into a list with all the other elements that should belong there. 
The list is sorted according to the reward (the Biggest rewarding element is first in the list). 
### Gameplay
The game firstly analyzes all the messages then tries to solve easily solvable messages (messages with very high or high success rate). 
If the player starts losing lives and still has some easy messages to solve then the player will attempt to buy some health potions.
If the player has been successful in the easy missions or just has a lot of money to spare, then he will get a chance to buy some upgrades (not before buying health potions if needed).
The game then loops, looking for the best messages to solve and optionally buys upgrades. 

### Why is a score of at least 1000 points not always reachable
The game revolves around probability. There is not always a guarantee that a message with a very high success rate will be successfully solved. 
There may be games where the player has failed multiple very high success rate messages. Even if the player has gotten enough money for health potions, 
he still might fail next messages and not have enough money for new potions. This is the reason, why the score of at least 1000 cannot be always reached.

### Future notes
In the future, there could be different options to try
 * keeping more of the data in memory and not needing to update messages after every message solving or store purchase.
 * benefitting the player more with certain upgrades, for example checking what kind of benefits do certain items give (instead of choosing the most expensive one).
 * finding out more reputation affecting words (underworld reputation effects did not appear when I used to analyze the data).
 * logging the data in some database (for some machine learning application)

## Prerequisites

* [Java 1.8](http://www.oracle.com)
* [JUnit 4](https://junit.org/junit4/)
* [Apache HttpComponents](http://hc.apache.org/)
* [JSON in Java] (https://github.com/stleary/JSON-java/)
* A stable internet connection

## Running the tests

Using JUnit4, run test classes in test folder.

### Tested

* GoldSpendTests - tests if the item buying logic works
```
For example, if I have a lot of money, should I buy some upgrade?
```

* MessageBoardTest - tests if the best message is chosen from the map
```
For example, if I have two messages, then the easier message would be choosed.
```

## Built With

* [Java 1.8](http://www.oracle.com) - Programming language used for the task
* [JUnit 4](https://junit.org/junit4/) - Test framework used for tests
* [Apache HttpComponents](http://hc.apache.org/) - Library used for POST and GET request
* [JSON in Java] (https://github.com/stleary/JSON-java) - Library used for JSON in Java

## Author

Cardo Kambla
