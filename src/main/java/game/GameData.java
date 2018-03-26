package game;

import game.creatures.Creature;

import java.util.Map;

public interface GameData {
    Map<String, Creature> getCreatures();
}
