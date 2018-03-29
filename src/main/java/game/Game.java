package game;

import chatBot.outMessageListener;
import game.Map.Map;
import game.creatures.Creature;
import game.creatures.CreatureFactory;
import graphic.graphListener;
import jogamp.opengl.gl4.GL4bcImpl;
import util.Prop;
import util.Utils;

import java.util.*;

public class Game implements Runnable, GameEventListener {
    private final java.util.Map<String, Creature> creatures;
    private graphListener gListener;
    private final Map gameMap;
    private final CreatureFactory creatureFactory;

    public Game(outMessageListener chatListener) {
        creatures = Collections.synchronizedMap(new HashMap<String, Creature>());
        int mapSize = Prop.getInt("mapSize");
        gameMap = new Map(mapSize, this);
        creatureFactory = new CreatureFactory(chatListener);
    }

    public void setgListener(graphListener g) {
        gListener = g;
    }

    public List<Creature> getPlayersList() {
        List<Creature> players = new ArrayList<>();
        creatures.forEach((key, c) -> {
            if (c.isPlayer()) {
                players.add(c);
            }
        });
        return players;
    }

    @Override
    public void run() {
        while (true) {
            gameMap.Tick();

            Utils.sleep(Integer.parseInt(Prop.getProp("tickDelay")));
        }
    }

    @Override
    public void GameEvent(Object sender, String command) {
        if (sender.getClass() == GL4bcImpl.class) {
            gameMap.Draw((GL4bcImpl) sender);

            creatures.forEach((key, c) -> c.Draw((GL4bcImpl) sender));
            return;
        }
        switch (command) {
            case "red":
            case "blue":
            case "green":
                gListener.AwesomeEvent(command);
                break;
        }
    }
}
