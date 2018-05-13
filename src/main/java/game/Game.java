package game;

import chatBot.outMessageListener;
import game.Map.Map;
import game.creatures.Creature;
import game.creatures.CreatureFactory;
import game.creatures.Player;
import graphic.JoglCanvas;
import graphic.graphListener;
import util.Prop;
import util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Game implements Runnable, GameEventListener {
    private final java.util.Map<String, Creature> creatures;
    private graphListener gListener;
    private final Map gameMap;
    private final CreatureFactory creatureFactory;

    private Creature focusedCreature;

    public Game(outMessageListener chatListener) {
        creatures = Collections.synchronizedMap(new HashMap<String, Creature>());
        int mapSize = Prop.getInt("mapSize");
        gameMap = new Map(mapSize, this);
        creatureFactory = new CreatureFactory(chatListener);
        //focusedCreature = null;
    }

    public void setgListener(graphListener g) {
        gListener = g;
    }

    @Deprecated
    public List<Creature> getPlayersList() {
        List<Creature> players = new ArrayList<>();
        creatures.forEach((key, c) -> {
            if (c.isPlayer()) {
                players.add(c);
            }
        });
        return players;
    }

    public List<Creature> getCreatureList() {
        List<Creature> creatureArrayList = new ArrayList<>();
        creatures.forEach((key, c) -> {
            if (!c.isPlayer()) {
                creatureArrayList.add(c);
            }
        });
        return creatureArrayList;
    }

    public java.util.Map<String, Creature> getCreatures() {
        return creatures;
    }

    public Map getMap() {
        return gameMap;
    }

    @Override
    public void run() {
        while (true) {
            //gameMap.Tick();
            creatureFactory.SavePopulationPlayer(creatures, this);
            creatures.forEach((key, c) -> c.Tick());
            creatureFactory.CleanDead(creatures);
            Utils.sleep(Prop.getInt("tickDelay"));
        }
    }

    @Override
    public void GameEvent(Object sender, String command) {
        switch (command) {
            case "Draw":
                if (sender.getClass() == JoglCanvas.class) {
                    try {
                        gameMap.Draw((JoglCanvas) sender);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return;
            case "focusCreature":
                if (sender.getClass() == Player.class) {
                    gListener.AwesomeEvent("setFocusedCreature", sender);
                }
                break;
            case "red":
            case "blue":
            case "green":
                gListener.AwesomeEvent(command, sender);
                break;
        }
    }
}
