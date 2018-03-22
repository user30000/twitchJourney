package game;

import chatBot.outMessageListener;
import game.Map.Map;
import game.creatures.Creature;
import game.creatures.Player;
import graphic.graphListener;
import jogamp.opengl.gl4.GL4bcImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game implements Runnable, GameEventListener {
    //chatConnector;
    int counter = 0;

    private ArrayList<Player> players;
    private ArrayList<Creature> creatures;
    private final outMessageListener chat;
    private graphListener gListener;
    private Map gameMap;

    private boolean lock = false;

    public Game(outMessageListener chatListener) {
        players = new ArrayList<Player>();
                //.synchronizedList(new ArrayList<Player>());
        creatures = new ArrayList<>();
        chat = chatListener;

        gameMap = new Map(65);
    }

    private void AddCreature(String Name) {
        if (creatures.size() < 10) {
            Creature c = new Creature(15);
            c.setGameEventListener(this);
            c.setName(Name);
            c.setTextureName(Name);
            creatures.add(c);
            //chat.Write("Создан: " + p.getNickName());
        }
    }

    public void removeCreature(String Name) {
        for (int i = 0; i < creatures.size(); i++) {
            if (Name.equals(creatures.get(i).getName())) {
                chat.Write("Удален: " + Name);
                creatures.remove(i);
                return;
            }
        }
    }

    private void AddPlayer(String Name) {
        if (players.size() < 10) {
            Player p = new Player(Name);
            p.setGameEventListener(this);
            players.add(p);
            chat.Write("Создан: " + p.getNickName());
        } else
            chat.Write("so many players");
    }

    private void removePlayer(String Name) {
        for (int i = 0; i < players.size(); i++) {
            if (Name.equals(players.get(i).getNickName())) {
                chat.Write("Удален: " + Name);
                players.remove(i);
                return;
            }
        }
    }

    private void Clean() {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).isDead()) {
                chat.Write("Удален: " + players.get(i).getNickName());
                players.remove(i);
            }
        }

        for (int i = 0; i < creatures.size(); i++) {
            if (creatures.get(i).isDead()) {
                chat.Write("Удален: " + creatures.get(i).getName());
                creatures.remove(i);
            }
        }
    }

    public void setgListener(graphListener g) {
        gListener = g;
    }

    public List getPlayersList(){
        return players;
    }

    @Override
    public void run() {
        while (true) {
            lock = true;
            counter++;
            for (Player player : players) {
                player.Tick();
            }
            for (Creature creature : creatures) {
                creature.Tick();
            }
            this.Clean();

            if (counter == 5) {
                counter = 0;
                AddPlayer("Чувачок" + String.valueOf(new Random().nextInt()));
            }

            AddCreature("zombie");

            lock = false;

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
            if (!lock) {
                gameMap.Draw((GL4bcImpl) sender);

                for (Player player : players) {
                    player.Draw((GL4bcImpl) sender);
                }

                for (Creature creature : creatures) {
                    creature.Draw((GL4bcImpl) sender);
                }
            }
            return;
        }
        switch (command) {
            case "kill":
                if (sender.getClass() == Player.class) {
                    removePlayer(((Player) sender).getNickName());
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
