package game;

import chatBot.outMessageListener;
import game.Map.Map;
import game.creatures.Creature;
import game.creatures.CreatureFactory;
import game.creatures.Player;
import graphic.graphListener;
import jogamp.opengl.gl4.GL4bcImpl;
import util.Prop;

import java.util.*;

public class Game implements Runnable, GameEventListener, GameData {
    //chatConnector;
    int counter = 0;

    private java.util.Map<String, Creature> creatures;
    private final outMessageListener chat;
    private graphListener gListener;
    private Map gameMap;
    private CreatureFactory creatureFactory;

    public Game(outMessageListener chatListener) {
        creatures = Collections.synchronizedMap(new HashMap<String, Creature>());
        chat = chatListener;

        String mapSize = Prop.getProp("mapSize");
        gameMap = new Map(Integer.parseInt(mapSize));
        creatureFactory = new CreatureFactory(this, chatListener);
    }

    @Override
    public java.util.Map<String,Creature> getCreatures() {
        return creatures;
    }

    private void Clean() {
        creatures.entrySet().removeIf(e -> {
            Creature c = e.getValue();
            if(c.isDead()) {
                chat.Write("Умер: " + c.toFullString());
                return true;
            }
            return false;
        });
    }

    public void setgListener(graphListener g) {
        gListener = g;
    }

    public List getPlayersList(){
        List<Creature> players = new ArrayList<>();
        creatures.forEach((key, c) -> {
            if(c.isPlayer()) {
                players.add(c);
            }
        });
        return players;
    }

    @Override
    public void run() {
        while (true) {
            counter++;
            creatures.forEach((key, c) -> c.Tick());
            this.Clean();

            creatureFactory.SavePopulationPlayer();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void GameEvent(Object sender, String command) {
        //System.out.println(sender.getClass().toString());
        if (sender.getClass() == GL4bcImpl.class) {
            gameMap.Draw((GL4bcImpl) sender);

            creatures.forEach((key, c) -> c.Draw((GL4bcImpl) sender));
        }
        switch (command) {
            case "kill":
                if (sender.getClass() == Player.class) {
                    creatureFactory.Kill(((Player) sender).getNickName());
                }
                break;
            case "red":
            case "blue":
            case "green":
                gListener.AwesomeEvent(command);
                break;
        }
    }
}
